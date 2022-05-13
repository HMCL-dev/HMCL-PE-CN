package com.tungsten.hmclpe.launcher.mod.modrinth;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.tungsten.hmclpe.launcher.mod.ModListBean;
import com.tungsten.hmclpe.utils.Lang;
import com.tungsten.hmclpe.utils.io.HttpRequest;
import com.tungsten.hmclpe.utils.io.NetworkUtils;
import com.tungsten.hmclpe.utils.string.StringUtils;

import static com.tungsten.hmclpe.utils.Lang.mapOf;
import static com.tungsten.hmclpe.utils.Pair.pair;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public final class Modrinth {
    private Modrinth() {
    }

    public static List<ModResult> searchPaginated(String gameVersion, int pageOffset, String searchFilter) throws IOException {
        Map<String, String> query = mapOf(
                pair("query", searchFilter),
                pair("offset", Integer.toString(pageOffset)),
                pair("limit", "50")
        );
        if (StringUtils.isNotBlank(gameVersion)) {
            query.put("version", "versions=" + gameVersion);
        }
        Response<ModResult> response = HttpRequest.GET(NetworkUtils.withQuery("https://api.modrinth.com/api/v1/mod", query))
                .getJson(new TypeToken<Response<ModResult>>() {
                }.getType());
        return response.getHits();
    }

    public static Stream<ModListBean.Version> getRemoteVersionsById(String id) throws IOException {
        id = StringUtils.removePrefix(id, "local-");
        List<ModVersion> versions = HttpRequest.GET("https://api.modrinth.com/api/v1/mod/" + id + "/version")
                .getJson(new TypeToken<List<ModVersion>>() {
                }.getType());
        return versions.stream().map(ModVersion::toVersion).flatMap(Lang::toStream);
    }

    public static List<ModVersion> getFiles(ModResult mod) throws IOException {
        String id = StringUtils.removePrefix(mod.getModId(), "local-");
        List<ModVersion> versions = HttpRequest.GET("https://api.modrinth.com/api/v1/mod/" + id + "/version")
                .getJson(new TypeToken<List<ModVersion>>() {
                }.getType());
        return versions;
    }

    public static List<String> getCategories() throws IOException {
        List<String> categories = HttpRequest.GET("https://api.modrinth.com/api/v1/tag/category").getJson(new TypeToken<List<String>>() {
        }.getType());
        return categories;
    }

    public static class Mod {
        private final String id;

        private final String slug;

        private final String team;

        private final String title;

        private final String description;

        private final Instant published;

        private final Instant updated;

        private final List<String> categories;

        private final List<String> versions;

        private final int downloads;

        @SerializedName("icon_url")
        private final String iconUrl;

        public Mod(String id, String slug, String team, String title, String description, Instant published, Instant updated, List<String> categories, List<String> versions, int downloads, String iconUrl) {
            this.id = id;
            this.slug = slug;
            this.team = team;
            this.title = title;
            this.description = description;
            this.published = published;
            this.updated = updated;
            this.categories = categories;
            this.versions = versions;
            this.downloads = downloads;
            this.iconUrl = iconUrl;
        }

        public String getId() {
            return id;
        }

        public String getSlug() {
            return slug;
        }

        public String getTeam() {
            return team;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public Instant getPublished() {
            return published;
        }

        public Instant getUpdated() {
            return updated;
        }

        public List<String> getCategories() {
            return categories;
        }

        public List<String> getVersions() {
            return versions;
        }

        public int getDownloads() {
            return downloads;
        }

        public String getIconUrl() {
            return iconUrl;
        }
    }

    public static class ModVersion {
        private final String id;

        @SerializedName("mod_id")
        private final String modId;

        @SerializedName("author_id")
        private final String authorId;

        private final String name;

        @SerializedName("version_number")
        private final String versionNumber;

        private final String changelog;

        @SerializedName("date_published")
        private final Instant datePublished;

        private final int downloads;

        @SerializedName("version_type")
        private final String versionType;

        private final List<ModVersionFile> files;

        private final List<String> dependencies;

        @SerializedName("game_versions")
        private final List<String> gameVersions;

        private final List<String> loaders;

        public ModVersion(String id, String modId, String authorId, String name, String versionNumber, String changelog, Instant datePublished, int downloads, String versionType, List<ModVersionFile> files, List<String> dependencies, List<String> gameVersions, List<String> loaders) {
            this.id = id;
            this.modId = modId;
            this.authorId = authorId;
            this.name = name;
            this.versionNumber = versionNumber;
            this.changelog = changelog;
            this.datePublished = datePublished;
            this.downloads = downloads;
            this.versionType = versionType;
            this.files = files;
            this.dependencies = dependencies;
            this.gameVersions = gameVersions;
            this.loaders = loaders;
        }

        public String getId() {
            return id;
        }

        public String getModId() {
            return modId;
        }

        public String getAuthorId() {
            return authorId;
        }

        public String getName() {
            return name;
        }

        public String getVersionNumber() {
            return versionNumber;
        }

        public String getChangelog() {
            return changelog;
        }

        public Instant getDatePublished() {
            return datePublished;
        }

        public int getDownloads() {
            return downloads;
        }

        public String getVersionType() {
            return versionType;
        }

        public List<ModVersionFile> getFiles() {
            return files;
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

        public Optional<ModListBean.Version> toVersion() {
            ModListBean.VersionType type;
            if ("release".equals(versionType)) {
                type = ModListBean.VersionType.Release;
            } else if ("beta".equals(versionType)) {
                type = ModListBean.VersionType.Beta;
            } else if ("alpha".equals(versionType)) {
                type = ModListBean.VersionType.Alpha;
            } else {
                type = ModListBean.VersionType.Release;
            }

            if (files.size() == 0) {
                return Optional.empty();
            }

            return Optional.of(new ModListBean.Version(
                    this,
                    name,
                    versionNumber,
                    changelog,
                    datePublished,
                    type,
                    files.get(0).toFile(),
                    dependencies,
                    gameVersions,
                    loaders
            ));
        }
    }

    public static class ModVersionFile {
        private final Map<String, String> hashes;
        private final String url;
        private final String filename;

        public ModVersionFile(Map<String, String> hashes, String url, String filename) {
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

        public ModListBean.File toFile() {
            return new ModListBean.File(hashes, url, filename);
        }
    }

    public static class ModResult implements ModListBean.IMod {
        @SerializedName("mod_id")
        private final String modId;

        private final String slug;

        private final String author;

        private final String title;

        private final String description;

        private final List<String> categories;

        private final List<String> versions;

        private final int downloads;

        @SerializedName("page_url")
        private final String pageUrl;

        @SerializedName("icon_url")
        private final String iconUrl;

        @SerializedName("author_url")
        private final String authorUrl;

        @SerializedName("date_created")
        private final Instant dateCreated;

        @SerializedName("date_modified")
        private final Instant dateModified;

        @SerializedName("latest_version")
        private final String latestVersion;

        public ModResult(String modId, String slug, String author, String title, String description, List<String> categories, List<String> versions, int downloads, String pageUrl, String iconUrl, String authorUrl, Instant dateCreated, Instant dateModified, String latestVersion) {
            this.modId = modId;
            this.slug = slug;
            this.author = author;
            this.title = title;
            this.description = description;
            this.categories = categories;
            this.versions = versions;
            this.downloads = downloads;
            this.pageUrl = pageUrl;
            this.iconUrl = iconUrl;
            this.authorUrl = authorUrl;
            this.dateCreated = dateCreated;
            this.dateModified = dateModified;
            this.latestVersion = latestVersion;
        }

        public String getModId() {
            return modId;
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

        public List<String> getVersions() {
            return versions;
        }

        public int getDownloads() {
            return downloads;
        }

        public String getPageUrl() {
            return pageUrl;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public String getAuthorUrl() {
            return authorUrl;
        }

        public Instant getDateCreated() {
            return dateCreated;
        }

        public Instant getDateModified() {
            return dateModified;
        }

        public String getLatestVersion() {
            return latestVersion;
        }

        public List<ModListBean.Mod> loadDependencies() throws IOException {
            return Collections.emptyList();
        }

        public Stream<ModListBean.Version> loadVersions() throws IOException {
            return Modrinth.getFiles(this).stream()
                    .map(ModVersion::toVersion)
                    .flatMap(Lang::toStream);
        }

        public ModListBean.Mod toMod() {
            return new ModListBean.Mod(
                    slug,
                    author,
                    title,
                    description,
                    categories,
                    pageUrl,
                    iconUrl,
                    (ModListBean.IMod) this
            );
        }
    }

    public static class Response<T> {
        private final int offset;

        private final int limit;

        @SerializedName("total_hits")
        private final int totalHits;

        private final List<T> hits;

        public Response() {
            this(0, 0, Collections.emptyList());
        }

        public Response(int offset, int limit, List<T> hits) {
            this.offset = offset;
            this.limit = limit;
            this.totalHits = hits.size();
            this.hits = hits;
        }

        public int getOffset() {
            return offset;
        }

        public int getLimit() {
            return limit;
        }

        public int getTotalHits() {
            return totalHits;
        }

        public List<T> getHits() {
            return hits;
        }
    }
}
