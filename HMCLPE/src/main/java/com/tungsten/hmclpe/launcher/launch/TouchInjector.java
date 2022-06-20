package com.tungsten.hmclpe.launcher.launch;

import com.tungsten.hmclpe.launcher.MainActivity;
import com.tungsten.hmclpe.launcher.setting.game.GameLaunchSetting;
import com.tungsten.hmclpe.manifest.AppManifest;

import java.util.Vector;

public class TouchInjector {

    public static Vector<String> rebaseArguments(GameLaunchSetting gameLaunchSetting, Vector<String> args) {
        if (!gameLaunchSetting.touchInjector) {
            return args;
        }
        MainActivity.verifyFunc();
        Vector<String> newArgs = new Vector<>();
        if (args.contains("Forge") || args.contains("cpw.mods.fml.common.launcher.FMLTweaker") || args.contains("fmlclient") || args.contains("forgeclient")) {
            if (args.contains("cpw.mods.bootstraplauncher.BootstrapLauncher")) {
                boolean hit = false;
                for (int i = 0; i < args.size(); i++) {
                    if (hit) {
                        newArgs.add(args.get(i) + ":" + AppManifest.PLUGIN_DIR + "/touch/TouchInjector.jar");
                        hit = false;
                    }
                    else if (args.get(i).equals("cpw.mods.bootstraplauncher.BootstrapLauncher")) {
                        newArgs.add("--add-exports=cpw.mods.bootstraplauncher/cpw.mods.bootstraplauncher=ALL-UNNAMED");
                        newArgs.add("com.tungsten.touchinjector.launch.TouchBootstrapLauncher");
                    }
                    else if (args.get(i).equals("-cp")) {
                        hit = true;
                        newArgs.add(args.get(i));
                    }
                    else {
                        newArgs.add(args.get(i));
                    }
                }
            }
            else {
                for (int i = 0; i < args.size(); i++) {
                    if (args.get(i).startsWith("-Xms")) {
                        newArgs.add("-javaagent:" + AppManifest.PLUGIN_DIR + "/touch/TouchInjector.jar=forge");
                    }
                    newArgs.add(args.get(i));
                }
            }
            return newArgs;
        }
        else if (args.contains("optifine.OptiFineTweaker")) {
            for (int i = 0; i < args.size(); i++) {
                if (args.get(i).startsWith("-Xms")) {
                    newArgs.add("-javaagent:" + AppManifest.PLUGIN_DIR + "/touch/TouchInjector.jar=optifine");
                }
                newArgs.add(args.get(i));
            }
            return newArgs;
        }
        else if (args.contains("net.fabricmc.loader.impl.launch.knot.KnotClient")) {
            boolean hit = false;
            for (int i = 0; i < args.size(); i++) {
                if (hit) {
                    newArgs.add(args.get(i) + ":" + AppManifest.PLUGIN_DIR + "/touch/TouchInjector.jar");
                    hit = false;
                }
                else if (args.get(i).equals("net.fabricmc.loader.impl.launch.knot.KnotClient")) {
                    newArgs.add("com.tungsten.touchinjector.launch.TouchKnotClient");
                }
                else if (args.get(i).equals("-cp")) {
                    hit = true;
                    newArgs.add(args.get(i));
                }
                else {
                    newArgs.add(args.get(i));
                }
            }
            return newArgs;
        }
        else {
            for (int i = 0; i < args.size(); i++) {
                if (args.get(i).startsWith("-Xms")) {
                    newArgs.add("-javaagent:" + AppManifest.PLUGIN_DIR + "/touch/TouchInjector.jar=vanilla");
                }
                newArgs.add(args.get(i));
            }
            return newArgs;
        }
    }

}
