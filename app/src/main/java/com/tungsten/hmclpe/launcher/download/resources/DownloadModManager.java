package com.tungsten.hmclpe.launcher.download.resources;

import com.tungsten.hmclpe.launcher.download.resources.modrinth.Modrinth;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class DownloadModManager {
    private DownloadModManager() {
    }

    public interface IMod {
        List<Mod> loadDependencies() throws IOException;

        Stream<Version> loadVersions() throws IOException;
    }

    public static class Mod {
        private final String slug;
        private final String author;
        private final String title;
        private final String description;
        private final List<String> categories;
        private final String pageUrl;
        private final String iconUrl;
        private final IMod data;

        public Mod(String slug, String author, String title, String description, List<String> categories, String pageUrl, String iconUrl, IMod data) {
            this.slug = slug;
            this.author = author;
            this.title = title;
            this.description = description;
            this.categories = categories;
            this.pageUrl = pageUrl;
            this.iconUrl = iconUrl;
            this.data = data;
        }

        public String getSlug() {
            return slug;
        }

        public String getAuthor() {
            return author;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getCategories() {
            return categories;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public IMod getData() {
            return data;
        }
    }

    public enum VersionType {
        Release,
        Beta,
        Alpha
    }

    public static class Version {
        private final Object self;
        private final String name;
        private final String version;
        private final String changelog;
        private final Instant datePublished;
        private final VersionType versionType;
        private final File file;
        private final List<String> dependencies;
        private final List<String> gameVersions;
        private final List<String> loaders;

        public Version(Object self, String name, String version, String changelog, Instant datePublished, VersionType versionType, File file, List<String> dependencies, List<String> gameVersions, List<String> loaders) {
            this.self = self;
            this.name = name;
            this.version = version;
            this.changelog = changelog;
            this.datePublished = datePublished;
            this.versionType = versionType;
            this.file = file;
            this.dependencies = dependencies;
            this.gameVersions = gameVersions;
            this.loaders = loaders;
        }

        public Object getSelf() {
            return self;
        }

        public String getName() {
            return name;
        }

        public String getVersion() {
            return version;
        }

        public String getChangelog() {
            return changelog;
        }

        public Instant getDatePublished() {
            return datePublished;
        }

        public VersionType getVersionType() {
            return versionType;
        }

        public File getFile() {
            return file;
        }

        public List<String> getDependencies() {
            return dependencies;
        }

        public List<String> getGameVersions() {
            return gameVersions;
        }

        public List<String> getLoaders() {
            return loaders;
        }
    }

    public static class File {
        private final Map<String, String> hashes;
        private final String url;
        private final String filename;

        public File(Map<String, String> hashes, String url, String filename) {
            this.hashes = hashes;
            this.url = url;
            this.filename = filename;
        }

        public Map<String, String> getHashes() {
            return hashes;
        }

        public String getUrl() {
            return url;
        }

        public String getFilename() {
            return filename;
        }
    }
}