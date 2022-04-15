package com.tungsten.hmclpe.launcher.download.forge;

import com.google.gson.JsonParseException;
import com.tungsten.hmclpe.launcher.game.Artifact;
import com.tungsten.hmclpe.launcher.game.Library;
import com.tungsten.hmclpe.utils.gson.tools.TolerableValidationException;
import com.tungsten.hmclpe.utils.gson.tools.Validation;

import java.util.*;
import java.util.stream.Collectors;

public class ForgeNewInstallProfile implements Validation {

    private final int spec;
    private final String minecraft;
    private final String json;
    private final String version;
    private final Artifact path;
    private final List<Library> libraries;
    private final List<Processor> processors;
    private final Map<String, Datum> data;

    public ForgeNewInstallProfile(int spec, String minecraft, String json, String version, Artifact path, List<Library> libraries, List<Processor> processors, Map<String, Datum> data) {
        this.spec = spec;
        this.minecraft = minecraft;
        this.json = json;
        this.version = version;
        this.path = path;
        this.libraries = libraries;
        this.processors = processors;
        this.data = data;
    }

    /**
     * Specification for install_profile.json.
     */
    public int getSpec() {
        return spec;
    }

    /**
     * Vanilla game version that this installer supports.
     */
    public String getMinecraft() {
        return minecraft;
    }

    /**
     * Version json to be installed.
     * @return path of the version json relative to the installer JAR file.
     */
    public String getJson() {
        return json;
    }

    /**
     *
     * @return forge version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Maven artifact path for the main jar to install.
     * @return artifact path of the main jar.
     */
    public Optional<Artifact> getPath() {
        return Optional.ofNullable(path);
    }

    /**
     * Libraries that processors depend on.
     * @return the required dependencies.
     */
    public List<Library> getLibraries() {
        return libraries == null ? Collections.emptyList() : libraries;
    }

    /**
     * Tasks to be executed to setup modded environment.
     */
    public List<Processor> getProcessors() {
        if (processors == null) return Collections.emptyList();
        return processors.stream().filter(p -> p.isSide("client")).collect(Collectors.toList());
    }

    /**
     * Data for processors.
     *
     * @return a mutable data map for processors.
     */
    public Map<String, String> getData() {
        if (data == null)
            return new HashMap<>();

        return data.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getClient()));
    }

    @Override
    public void validate() throws JsonParseException, TolerableValidationException {
        if (minecraft == null || json == null || version == null)
            throw new JsonParseException("ForgeNewInstallProfile is malformed");
    }

    public static class Processor implements Validation {
        private final List<String> sides;
        private final Artifact jar;
        private final List<Artifact> classpath;
        private final List<String> args;
        private final Map<String, String> outputs;

        public Processor(List<String> sides, Artifact jar, List<Artifact> classpath, List<String> args, Map<String, String> outputs) {
            this.sides = sides;
            this.jar = jar;
            this.classpath = classpath;
            this.args = args;
            this.outputs = outputs;
        }

        /**
         * Check which side this processor should be run on. We only support client install currently.
         * @param side can be one of "client", "server", "extract".
         * @return true if the processor can run on the side.
         */
        public boolean isSide(String side) {
            return sides == null || sides.contains(side);
        }

        /**
         * The executable jar of this processor task. Will be executed in installation process.
         * @return the artifact path of executable jar.
         */
        public Artifact getJar() {
            return jar;
        }

        /**
         * The dependencies of this processor task.
         * @return the artifact path of dependencies.
         */
        public List<Artifact> getClasspath() {
            return classpath == null ? Collections.emptyList() : classpath;
        }

        public List<String> getArgs() {
            return args == null ? Collections.emptyList() : args;
        }

        public Map<String, String> getOutputs() {
            return outputs == null ? Collections.emptyMap() : outputs;
        }

        @Override
        public void validate() throws JsonParseException, TolerableValidationException {
            if (jar == null)
                throw new JsonParseException("Processor::jar cannot be null");
        }
    }

    public static class Datum {
        private final String client;

        public Datum(String client) {
            this.client = client;
        }

        /**
         * Can be in the following formats:
         * [value]: An artifact path.
         * 'value': A string literal.
         * value: A file in the installer package, to be extracted to a temp folder, and then have the absolute path in replacements.
         * @return Value to use for the client install
         */
        public String getClient() {
            return client;
        }
    }
}