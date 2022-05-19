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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Modrinth {
    private Modrinth() {
    }

    private static String convertSortType(int sortType) {
        switch (sortType) {
            case 0:
            case 6:
            case 7:
                return "newest";
            case 1:
            case 2:
            case 3:
                return "relevance";
            case 4:
                return "updated";
            case 5:
                return "downloads";
            default:
                throw new IllegalArgumentException("Unsupported sort type " + sortType);
        }
    }

    public static List<ModResult> searchPaginated(String gameVersion,String category, int pageOffset, String searchFilter,int sort) throws IOException {
        Map<String, String> query = mapOf(
                pair("query", searchFilter),
                pair("offset", Integer.toString(pageOffset)),
                pair("limit", "50"),
                pair("index", convertSortType(sort))
        );
        if ((StringUtils.isNotBlank(category) && !category.equals("all")) || StringUtils.isNotBlank(gameVersion)) {
            query.put("facets","[" + ((StringUtils.isNotBlank(category) && !category.equals("all")) ? "[\"categories:" + category + "\"]" : "") + (((StringUtils.isNotBlank(category) && !category.equals("all")) && StringUtils.isNotBlank(gameVersion)) ? "," : "") + (StringUtils.isNotBlank(gameVersion) ? "[\"versions:" + gameVersion + "\"]" : "") + "]");
        }
        Response<ModResult> response = HttpRequest.GET(NetworkUtils.withQuery("https://api.modrinth.com/api/v1/mod", query))
                .getJson(new TypeToken<Response<ModResult>>() {
                }.getType());
        return response.getHits();
    }

    public static Mod getModById(String id) throws IOException {
        id = StringUtils.removePrefix(id, "local-");
        System.out.println(HttpRequest.GET("https://api.modrinth.com/api/v1/mod/" + id).getString());
        return HttpRequest.GET("https://api.modrinth.com/api/v1/mod/" + id)
                .getJson(Mod.class);
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

    public static List<ModVersion> getFiles(Mod mod) throws IOException {
        String id = StringUtils.removePrefix(mod.getId(), "local-");
        List<ModVersion> versions = HttpRequest.GET("https://api.modrinth.com/api/v1/mod/" + id + "/version")
                .getJson(new TypeToken<List<ModVersion>>() {
                }.getType());
        return versions;
    }

    public static List<String> getCategories() throws IOException {
        return HttpRequest.GET("https://api.modrinth.com/api/v1/tag/category").getJson(new TypeToken<List<String>>() {}.getType());
    }

    public static class Mod implements ModListBean.IMod {
        private final String id;

        private final String slug;

        private final String team;

        private final String title;

        private final String description;

        private final Date published;

        private final Date updated;

        private final List<String> categories;

        private final List<String> versions;

        private final int downloads;

        @SerializedName("icon_url")
        private final String iconUrl;

        public Mod(String id, String slug, String team, String title, String description, Date published, Date updated, List<String> categories, List<String> versions, int downloads, String iconUrl) {
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

        public Date getPublished() {
            return published;
        }

        public Date getUpdated() {
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

        public ModListBean.Mod toMod() {
            return new ModListBean.Mod(
                    slug,
                    "",
                    title,
                    description,
                    new ArrayList<>(),
                    categories,
                    null,
                    iconUrl,
                    (ModListBean.IMod) this
            );
        }

        @Override
        public List<ModListBean.Mod> loadDependencies(List<ModListBean.Version> versions) throws IOException {
            Set<String> dependencies = versions.stream()
                    .flatMap(version -> version.getDependencies().stream())
                    .collect(Collectors.toSet());
            List<ModListBean.Mod> mods = new ArrayList<>();
            for (String dependencyId : dependencies) {
                mods.add(getModById(dependencyId).toMod());
            }
            return mods;
        }

        @Override
        public Stream<ModListBean.Version> loadVersions() throws IOException {
            return Modrinth.getFiles(this).stream()
                    .map(ModVersion::toVersion)
                    .flatMap(Lang::toStream);
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
        private final Date datePublished;

        private final int downloads;

        @SerializedName("version_type")
        private final String versionType;

        private final List<ModVersionFile> files;

        private final List<Dependency> dependencies;

        @SerializedName("game_versions")
        private final List<String> gameVersions;

        private final List<String> loaders;

        public ModVersion(String id, String modId, String authorId, String name, String versionNumber, String changelog, Date datePublished, int downloads, String versionType, List<ModVersionFile> files, List<Dependency> dependencies, List<String> gameVersions, List<String> loaders) {
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

        public Date getDatePublished() {
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

        public List<Dependency> getDependencies() {
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

            List<String> strDependencies = new ArrayList<>();
            for (Dependency dependency : dependencies) {
                if (dependency.dependencyType.equals("required")) {
                    strDependencies.add(dependency.projectId);
                }
            }

            return Optional.of(new ModListBean.Version(
                    this,
                    name,
                    versionNumber,
                    changelog,
                    datePublished.toInstant(),
                    type,
                    files.get(0).toFile(),
                    strDependencies,
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

    public static class Dependency {
        @SerializedName("version_id")
        private final String versionId;

        @SerializedName("project_id")
        private final String projectId;

        @SerializedName("dependency_type")
        private final String dependencyType;

        public Dependency () {
            this(null,null,null);
        }

        public Dependency (String versionId,String projectId,String dependencyType) {
            this.versionId = versionId;
            this.projectId = projectId;
            this.dependencyType = dependencyType;
        }

        public String getVersionId() {
            return versionId;
        }

        public String getProjectId() {
            return projectId;
        }

        public String getDependencyType() {
            return dependencyType;
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
        private final Date dateCreated;

        @SerializedName("date_modified")
        private final Date dateModified;

        @SerializedName("latest_version")
        private final String latestVersion;

        public ModResult(String modId, String slug, String author, String title, String description, List<String> categories, List<String> versions, int downloads, String pageUrl, String iconUrl, String authorUrl, Date dateCreated, Date dateModified, String latestVersion) {
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

        public Date getDateCreated() {
            return dateCreated;
        }

        public Date getDateModified() {
            return dateModified;
        }

        public String getLatestVersion() {
            return latestVersion;
        }

        public List<ModListBean.Mod> loadDependencies(List<ModListBean.Version> versions) throws IOException {
            Set<String> dependencies = versions.stream()
                    .flatMap(version -> version.getDependencies().stream())
                    .collect(Collectors.toSet());
            List<ModListBean.Mod> mods = new ArrayList<>();
            for (String dependencyId : dependencies) {
                if (dependencyId != null && StringUtils.isNotBlank(dependencyId)) {
                    mods.add(getModById(dependencyId).toMod());
                }
            }
            return mods;
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
                    new ArrayList<>(),
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
