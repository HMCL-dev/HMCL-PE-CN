package com.tungsten.hmclpe.task.install;

import android.content.Context;
import android.os.AsyncTask;

import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.dialogs.install.DownloadDialog;
import com.tungsten.hmclpe.launcher.download.minecraft.optifine.OptifineVersion;
import com.tungsten.hmclpe.launcher.game.Arguments;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.LibrariesDownloadInfo;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.launcher.game.LibraryDownloadInfo;
import com.tungsten.hmclpe.launcher.game.Version;
import com.tungsten.hmclpe.launcher.install.LibraryAnalyzer;
import com.tungsten.hmclpe.launcher.manifest.AppManifest;
import com.tungsten.hmclpe.launcher.uis.game.download.DownloadUrlSource;
import com.tungsten.hmclpe.task.DownloadTask;
import com.tungsten.hmclpe.utils.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import cosine.boat.LoadMe;

public class InstallOptifineTask extends AsyncTask<OptifineVersion,Integer, Version> {

    private Context context;
    private MainActivity activity;
    private DownloadDialog dialog;

    private Library optiFineLibrary;
    private Library optiFineInstallerLibrary;

    public InstallOptifineTask(Context context,MainActivity activity,DownloadDialog dialog) {
        this.context = context;
        this.activity = activity;
        this.dialog = dialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Version doInBackground(OptifineVersion... optifineVersions) {
        OptifineVersion optifineVersion = optifineVersions[0];
        String mavenVersion = optifineVersion.mcVersion + "_" + optifineVersion.type + "_" + optifineVersion.patch;
        optiFineLibrary = new Library(new Artifact("optifine", "OptiFine", mavenVersion));
        optiFineInstallerLibrary = new Library(
                new Artifact("optifine", "OptiFine", mavenVersion, "installer"), null,
                new LibrariesDownloadInfo(new LibraryDownloadInfo(
                        "optifine/OptiFine/" + mavenVersion + "/OptiFine-" + mavenVersion + "-installer.jar",
                        ""))
        );
        List<Library> libraries = new ArrayList<>(4);
        libraries.add(optiFineLibrary);
        // Install launch wrapper modified by OptiFine
        boolean hasLaunchWrapper = false;
        String tempFold = AppManifest.INSTALL_DIR + "/optifine/installer/";
        try {
            FileUtils.copyFile(new File(AppManifest.INSTALL_DIR + "/optifine/" + optifineVersion.fileName).toPath(), new File(activity.launcherSetting.gameFileDirectory + "/libraries/" + optiFineInstallerLibrary.getPath()).toPath());
            if (new File(tempFold + "optifine/Patcher.class").exists()) {
                String javaPath = AppManifest.JAVA_DIR + "/default";
                String[] command = {
                        javaPath + "/bin/java",
                        "-cp",
                        AppManifest.INSTALL_DIR + "/optifine/" + optifineVersion.fileName,
                        "-Djava.library.path=" + javaPath + "/lib/aarch64/jli:" + javaPath + "/lib/aarch64",
                        "-Djava.io.tmpdir=" + AppManifest.INSTALL_DIR,
                        "optifine.Patcher",
                        activity.launcherSetting.gameFileDirectory + "/versions/" + dialog.name + "/" + dialog.name + ".jar",
                        AppManifest.INSTALL_DIR + "/optifine/" + optifineVersion.fileName,
                        activity.launcherSetting.gameFileDirectory + "/libraries/" + optiFineLibrary.getPath()
                };
                Vector<String> args = new Vector<String>();
                Collections.addAll(args, command);
                args.add("-Xms1024M");
                args.add("-Xmx1024M");
                int exitCode = LoadMe.launchJVM(javaPath,args,AppManifest.DEBUG_DIR);
                if (exitCode != 0){
                    throw new IOException("OptiFine patcher failed");
                }
            }
            else {
                FileUtils.copyFile(new File(AppManifest.INSTALL_DIR + "/optifine/" + optifineVersion.fileName).toPath(), new File(activity.launcherSetting.gameFileDirectory + "/libraries/" + optiFineLibrary.getPath()).toPath());
            }

            Path launchWrapper2 = new File(tempFold + "launchwrapper-2.0.jar").toPath();
            if (Files.exists(launchWrapper2)) {
                Library launchWrapper = new Library(new Artifact("optifine", "launchwrapper", "2.0"));
                File launchWrapperFile = new File(activity.launcherSetting.gameFileDirectory + "/libraries/" + launchWrapper.getPath());
                FileUtils.makeDirectory(launchWrapperFile.getAbsoluteFile().getParentFile());
                FileUtils.copyFile(launchWrapper2, launchWrapperFile.toPath());
                hasLaunchWrapper = true;
                libraries.add(launchWrapper);
            }

            Path launchWrapperVersionText = new File(tempFold + "launchwrapper-of.txt").toPath();
            if (Files.exists(launchWrapperVersionText)) {
                String launchWrapperVersion = FileUtils.readText(launchWrapperVersionText).trim();
                Path launchWrapperJar = new File(tempFold + "launchwrapper-of-" + launchWrapperVersion + ".jar").toPath();

                Library launchWrapper = new Library(new Artifact("optifine", "launchwrapper-of", launchWrapperVersion));

                if (Files.exists(launchWrapperJar)) {
                    File launchWrapperFile = new File(activity.launcherSetting.gameFileDirectory + "/libraries/" + launchWrapper.getPath());
                    FileUtils.makeDirectory(launchWrapperFile.getAbsoluteFile().getParentFile());
                    FileUtils.copyFile(launchWrapperJar, launchWrapperFile.toPath());

                    hasLaunchWrapper = true;
                    libraries.add(launchWrapper);
                }
            }
            if (!hasLaunchWrapper) {
                Library launchWrapperLib = new Library(new Artifact("net.minecraft", "launchwrapper", "1.12"));
                libraries.add(launchWrapperLib);
                DownloadTask.downloadFileMonitored(DownloadUrlSource.getSubUrl(DownloadUrlSource.getSource(activity.launcherSetting.downloadUrlSource),DownloadUrlSource.LIBRARIES) + "/" + launchWrapperLib.getPath(),activity.launcherSetting.gameFileDirectory + "/libraries/" + launchWrapperLib.getPath(),null);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return new Version(
                LibraryAnalyzer.LibraryType.OPTIFINE.getPatchId(),
                optifineVersion.type + "_" + optifineVersion.patch,
                10000,
                new Arguments().addGameArguments("--tweakClass", "optifine.OptiFineTweaker"),
                LibraryAnalyzer.LAUNCH_WRAPPER_MAIN,
                libraries
        );
    }

    @Override
    protected void onPostExecute(Version version) {
        super.onPostExecute(version);
        dialog.downloadTaskListAdapter.onComplete(dialog.downloadTaskListAdapter.getItem(0));
        dialog.gameVersionJson = dialog.gameVersionJson.addPatch(version);
        if (dialog.forgeVersion != null) {

        } else {
            if (dialog.liteLoaderVersion != null) {

            }
            else {
                dialog.installJson();
            }
        }
    }
}
