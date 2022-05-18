package com.tungsten.hmclpe.launcher.mod.curse;

import com.tungsten.hmclpe.launcher.mod.ModListBean;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CurseAddon implements ModListBean.IMod {
    private final int id;
    private final String name;
    private final List<Author> authors;
    private final Links links;
    private final Logo logo;
    private final int gameId;
    private final String summary;
    private final int defaultFileId;
    private final List<LatestFile> latestFiles;
    private final List<Category> categories;
    private final int status;
    private final int primaryCategoryId;
    private final String slug;
    private final List<GameVersionLatestFile> gameVersionLatestFiles;
    private final boolean isFeatured;
    private final double popularityScore;
    private final int gamePopularityRank;
    private final String primaryLanguage; // e.g. enUS
    private final List<String> modLoaders;
    private final boolean isAvailable;
    private final boolean isExperimental;

    public CurseAddon(int id, String name, List<Author> authors, Links links, Logo logo, int gameId, String summary, int defaultFileId, List<LatestFile> latestFiles, List<Category> categories, int status, int primaryCategoryId, String slug, List<GameVersionLatestFile> gameVersionLatestFiles, boolean isFeatured, double popularityScore, int gamePopularityRank, String primaryLanguage, List<String> modLoaders, boolean isAvailable, boolean isExperimental) {
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.links = links;
        this.logo = logo;
        this.gameId = gameId;
        this.summary = summary;
        this.defaultFileId = defaultFileId;
        this.latestFiles = latestFiles;
        this.categories = categories;
        this.status = status;
        this.primaryCategoryId = primaryCategoryId;
        this.slug = slug;
        this.gameVersionLatestFiles = gameVersionLatestFiles;
        this.isFeatured = isFeatured;
        this.popularityScore = popularityScore;
        this.gamePopularityRank = gamePopularityRank;
        this.primaryLanguage = primaryLanguage;
        this.modLoaders = modLoaders;
        this.isAvailable = isAvailable;
        this.isExperimental = isExperimental;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public Links getLinks() {
        return links;
    }

    public Logo getLogo() {
        return logo;
    }

    public int getGameId() {
        return gameId;
    }

    public String getSummary() {
        return summary;
    }

    public int getDefaultFileId() {
        return defaultFileId;
    }

    public List<LatestFile> getLatestFiles() {
        return latestFiles;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getStatus() {
        return status;
    }

    public int getPrimaryCategoryId() {
        return primaryCategoryId;
    }

    public String getSlug() {
        return slug;
    }

    public List<GameVersionLatestFile> getGameVersionLatestFiles() {
        return gameVersionLatestFiles;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public double getPopularityScore() {
        return popularityScore;
    }

    public int getGamePopularityRank() {
        return gamePopularityRank;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public List<String> getModLoaders() {
        return modLoaders;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isExperimental() {
        return isExperimental;
    }

    @Override
    public List<ModListBean.Mod> loadDependencies(List<ModListBean.Version> versions) throws IOException {
        Set<Integer> dependencies = latestFiles.stream()
                .flatMap(latestFile -> latestFile.getDependencies().stream())
                .filter(dep -> dep.getRelationType() == 3)
                .map(Dependency::getModId)
                .collect(Collectors.toSet());
        List<ModListBean.Mod> mods = new ArrayList<>();
        for (int dependencyId : dependencies) {
            mods.add(CurseModManager.getAddon(dependencyId).toMod());
        }
        return mods;
    }

    @Override
    public Stream<ModListBean.Version> loadVersions() throws IOException {
        return CurseModManager.getFiles(this).stream()
                .map(CurseAddon.LatestFile::toVersion);
    }

    public ModListBean.Mod toMod() {
        String iconUrl = logo.getThumbnailUrl();

        return new ModListBean.Mod(
                slug,
                "",
                name,
                summary,
                categories.stream().map(category -> Integer.toString(category.getId())).collect(Collectors.toList()),
                new ArrayList<>(),
                links.websiteUrl,
                iconUrl,
                this
        );
    }

    public static class Author {
        private final String name;
        private final String url;
        private final int projectId;
        private final int id;
        private final int userId;
        private final int twitchId;

        public Author(String name, String url, int projectId, int id, int userId, int twitchId) {
            this.name = name;
            this.url = url;
            this.projectId = projectId;
            this.id = id;
            this.userId = userId;
            this.twitchId = twitchId;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public int getProjectId() {
            return projectId;
        }

        public int getId() {
            return id;
        }

        public int getUserId() {
            return userId;
        }

        public int getTwitchId() {
            return twitchId;
        }
    }

    public static class Links {
        private final String websiteUrl;
        private final String wikiUrl;
        private final String issuesUrl;
        private final String sourceUrl;

        public Links () {
            this(null,null,null,null);
        }

        public Links (String websiteUrl,String wikiUrl,String issuesUrl,String sourceUrl) {
            this.websiteUrl = websiteUrl;
            this.wikiUrl = wikiUrl;
            this.issuesUrl = issuesUrl;
            this.sourceUrl = sourceUrl;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public String getWikiUrl() {
            return wikiUrl;
        }

        public String getIssuesUrl() {
            return issuesUrl;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }
    }

    public static class Logo {
        private final int id;
        private final int modId;
        private final String title;
        private final String description;
        private final String thumbnailUrl;
        private final String url;

        public Logo () {
            this(0,0,"","","","");
        }

        public Logo (int id,int modId,String title,String description,String thumbnailUrl,String url) {
            this.id = id;
            this.modId = modId;
            this.title = title;
            this.description = description;
            this.thumbnailUrl = thumbnailUrl;
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public int getModId() {
            return modId;
        }

        public String getDescription() {
            return description;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }
    }

    public static class Dependency {
        private final int modId;
        private final int relationType;

        public Dependency() {
            this(0, 1);
        }

        public Dependency(int modId, int relationType) {
            this.modId = modId;
            this.relationType = relationType;
        }

        public int getModId() {
            return modId;
        }

        public int getRelationType() {
            return relationType;
        }
    }

    public static class SortableGameVersions {
        private final String gameVersionName;
        private final String gameVersionPadded;
        private final String gameVersion;
        private final Instant gameVersionReleaseDate;
        private final int gameVersionTypeId;

        public SortableGameVersions () {
            this("","","",null,0);
        }

        public SortableGameVersions (String gameVersionName,String gameVersionPadded,String gameVersion,Instant gameVersionReleaseDate,int gameVersionTypeId) {
            this.gameVersionName = gameVersionName;
            this.gameVersionPadded = gameVersionPadded;
            this.gameVersion = gameVersion;
            this.gameVersionReleaseDate = gameVersionReleaseDate;
            this.gameVersionTypeId = gameVersionTypeId;
        }

        public String getGameVersionName() {
            return gameVersionName;
        }

        public String getGameVersionPadded() {
            return gameVersionPadded;
        }

        public String getGameVersion() {
            return gameVersion;
        }

        public Instant getGameVersionReleaseDate() {
            return gameVersionReleaseDate;
        }

        public int getGameVersionTypeId() {
            return gameVersionTypeId;
        }
    }

    public static class LatestFile {
        private final int id;
        private final String displayName;
        private final String fileName;
        private final String fileDate;
        private final int fileLength;
        private final int releaseType;
        private final int fileStatus;
        private final String downloadUrl;
        private final boolean isAlternate;
        private final int alternateFileId;
        private final List<Dependency> dependencies;
        private final boolean isAvailable;
        private final List<String> gameVersions;
        private final List<SortableGameVersions> sortableGameVersions;
        private final boolean hasInstallScript;
        private final boolean isCompatibleWIthClient;
        private final int categorySectionPackageType;
        private final int restrictProjectFileAccess;
        private final int projectStatus;
        private final int projectId;
        private final boolean isServerPack;
        private final int serverPackFileId;

        private transient Instant fileDataInstant;

        public LatestFile(int id, String displayName, String fileName, String fileDate, int fileLength, int releaseType, int fileStatus, String downloadUrl, boolean isAlternate, int alternateFileId, List<Dependency> dependencies, boolean isAvailable, List<String> gameVersions, List<SortableGameVersions> sortableGameVersions, boolean hasInstallScript, boolean isCompatibleWIthClient, int categorySectionPackageType, int restrictProjectFileAccess, int projectStatus, int projectId, boolean isServerPack, int serverPackFileId) {
            this.id = id;
            this.displayName = displayName;
            this.fileName = fileName;
            this.fileDate = fileDate;
            this.fileLength = fileLength;
            this.releaseType = releaseType;
            this.fileStatus = fileStatus;
            this.downloadUrl = downloadUrl;
            this.isAlternate = isAlternate;
            this.alternateFileId = alternateFileId;
            this.dependencies = dependencies;
            this.isAvailable = isAvailable;
            this.gameVersions = gameVersions;
            this.sortableGameVersions = sortableGameVersions;
            this.hasInstallScript = hasInstallScript;
            this.isCompatibleWIthClient = isCompatibleWIthClient;
            this.categorySectionPackageType = categorySectionPackageType;
            this.restrictProjectFileAccess = restrictProjectFileAccess;
            this.projectStatus = projectStatus;
            this.projectId = projectId;
            this.isServerPack = isServerPack;
            this.serverPackFileId = serverPackFileId;
        }

        public int getId() {
            return id;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileDate() {
            return fileDate;
        }

        public int getFileLength() {
            return fileLength;
        }

        public int getReleaseType() {
            return releaseType;
        }

        public int getFileStatus() {
            return fileStatus;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public boolean isAlternate() {
            return isAlternate;
        }

        public int getAlternateFileId() {
            return alternateFileId;
        }

        public List<Dependency> getDependencies() {
            return dependencies;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public List<String> getGameVersions() {
            return gameVersions;
        }

        public boolean isHasInstallScript() {
            return hasInstallScript;
        }

        public boolean isCompatibleWIthClient() {
            return isCompatibleWIthClient;
        }

        public int getCategorySectionPackageType() {
            return categorySectionPackageType;
        }

        public int getRestrictProjectFileAccess() {
            return restrictProjectFileAccess;
        }

        public int getProjectStatus() {
            return projectStatus;
        }

        public int getProjectId() {
            return projectId;
        }

        public boolean isServerPack() {
            return isServerPack;
        }

        public int getServerPackFileId() {
            return serverPackFileId;
        }

        public Instant getParsedFileDate() {
            if (fileDataInstant == null) {
                fileDataInstant = Instant.parse(fileDate);
            }
            return fileDataInstant;
        }

        public ModListBean.Version toVersion() {
            ModListBean.VersionType versionType;
            switch (getReleaseType()) {
                case 2:
                    versionType = ModListBean.VersionType.Beta;
                    break;
                case 3:
                    versionType = ModListBean.VersionType.Alpha;
                    break;
                default:
                    versionType = ModListBean.VersionType.Release;
                    break;
            }

            List<String> sortableVersions = new ArrayList<>();
            for (SortableGameVersions versions : sortableGameVersions) {
                if (versions.gameVersionName.startsWith("1.") || versions.gameVersionName.contains("w")) {
                    sortableVersions.add(versions.gameVersionName);
                }
            }

            return new ModListBean.Version(
                    this,
                    getDisplayName(),
                    null,
                    null,
                    getParsedFileDate(),
                    versionType,
                    new ModListBean.File(Collections.emptyMap(), getDownloadUrl(), getFileName()),
                    Collections.emptyList(),
                    sortableVersions,
                    Collections.emptyList()
            );
        }
    }

    public static class Category {
        private final int id;
        private final int gameId;
        private String name;
        private final String slug;
        private final String iconUrl;
        private final int parentCategoryId;
        private final boolean isClass;
        private final int classId;
        private final List<CurseModManager.Category> subcategories;

        public Category() {
            this(0, "", "", "", 0, 0, true, 0,new ArrayList<>());
        }

        public Category(int id, String name, String slug, String iconUrl, int parentGameCategoryId, int gameId,boolean isClass,int classId,List<CurseModManager.Category> subcategories) {
            this.id = id;
            this.name = name;
            this.slug = slug;
            this.iconUrl = iconUrl;
            this.parentCategoryId = parentGameCategoryId;
            this.gameId = gameId;
            this.isClass = isClass;
            this.classId = classId;
            this.subcategories = subcategories;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSlug() {
            return slug;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public int getParentCategoryId() {
            return parentCategoryId;
        }

        public int getGameId() {
            return gameId;
        }

        public void setName (String name) {
            this.name = name;
        }

        public boolean isClass() {
            return isClass;
        }

        public int getClassId() {
            return classId;
        }

        public List<CurseModManager.Category> getSubcategories() {
            return subcategories;
        }
    }

    public static class GameVersionLatestFile {
        private final String gameVersion;
        private final String projectFileId;
        private final String projectFileName;
        private final int fileType;
        private final Integer modLoader; // optional

        public GameVersionLatestFile(String gameVersion, String projectFileId, String projectFileName, int fileType, Integer modLoader) {
            this.gameVersion = gameVersion;
            this.projectFileId = projectFileId;
            this.projectFileName = projectFileName;
            this.fileType = fileType;
            this.modLoader = modLoader;
        }

        public String getGameVersion() {
            return gameVersion;
        }

        public String getProjectFileId() {
            return projectFileId;
        }

        public String getProjectFileName() {
            return projectFileName;
        }

        public int getFileType() {
            return fileType;
        }

        public Integer getModLoader() {
            return modLoader;
        }
    }
}
