package com.tungsten.hmclpe.launcher.mod.curse;

import static com.tungsten.hmclpe.utils.Lang.mapOf;
import static com.tungsten.hmclpe.utils.Pair.pair;

import com.tungsten.hmclpe.utils.gson.JsonUtils;
import com.tungsten.hmclpe.utils.io.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public final class CurseModManager {
    private CurseModManager() {
    }

    public static List<CurseAddon> searchPaginated(String gameVersion, int category, int section, int pageOffset, String searchFilter, int sort) throws IOException {
        String response = NetworkUtils.doGetCF(new URL(NetworkUtils.withQuery("https://api.curseforge.com/v1/mods/search", mapOf(
                pair("categoryId", Integer.toString(category)),
                pair("gameId", "432"),
                pair("gameVersion", gameVersion),
                pair("index", Integer.toString(pageOffset)),
                pair("pageSize", "50"),
                pair("searchFilter", searchFilter),
                pair("classId", Integer.toString(section)),
                pair("sortField", Integer.toString(sort + 1)),
                pair("sortOrder","desc")
        ))));
        return JsonUtils.fromNonNullJson(response, SearchResult.class).data;
    }

    public static CurseAddon getAddon(int id) throws IOException {
        String response = NetworkUtils.doGetCF(new URL(NetworkUtils.withQuery("https://api.curseforge.com/v1/mods/" + id,mapOf(
                pair("pageSize", "10000")
        ))));
        return JsonUtils.fromNonNullJson(response, ModResult.class).data;
    }

    public static List<CurseAddon.LatestFile> getFiles(CurseAddon addon) throws IOException {
        String response = NetworkUtils.doGetCF(new URL(NetworkUtils.withQuery("https://api.curseforge.com/v1/mods/" + addon.getId() + "/files",mapOf(
                pair("pageSize", "10000")
        ))));
        return JsonUtils.fromNonNullJson(response, ModFilesResult.class).data;
    }

    public static List<Category> getCategories(int section) throws IOException {
        String response = NetworkUtils.doGetCF(new URL(NetworkUtils.withQuery("https://api.curseforge.com/v1/categories",mapOf(
                pair("gameId", "432"),
                pair("classId", Integer.toString(section))
        ))));
        List<Category> categories = JsonUtils.fromNonNullJson(response, CategoryResult.class).data;
        return reorganizeCategories(categories, section);
    }

    private static List<Category> reorganizeCategories(List<Category> categories, int rootId) {
        List<Category> result = new ArrayList<>();

        Map<Integer, Category> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.id, category);
        }
        for (Category category : categories) {
            if (category.parentCategoryId == rootId) {
                result.add(category);
            } else {
                Category parentCategory = categoryMap.get(category.parentCategoryId);
                if (parentCategory == null) {
                    // Category list is not correct, so we ignore this item.
                    continue;
                }
                parentCategory.subcategories.add(category);
            }
        }
        return result;
    }

    public static final int SECTION_BUKKIT_PLUGIN = 5;
    public static final int SECTION_MOD = 6;
    public static final int SECTION_RESOURCE_PACK = 12;
    public static final int SECTION_WORLD = 17;
    public static final int SECTION_MODPACK = 4471;
    public static final int SECTION_CUSTOMIZATION = 4546;
    public static final int SECTION_ADDONS = 4559; // For Pocket Edition
    public static final int SECTION_UNKNOWN1 = 4944;
    public static final int SECTION_UNKNOWN2 = 4979;
    public static final int SECTION_UNKNOWN3 = 4984;

    public static class SearchResult {
        public List<CurseAddon> data;
    }

    public static class ModResult {
        public CurseAddon data;
    }

    public static class ModFilesResult {
        public List<CurseAddon.LatestFile> data;
    }

    public static class CategoryResult {
        public List<Category> data;
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
        private final List<Category> subcategories;

        public Category() {
            this(0, "", "", "", 0, 0, true, 0,new ArrayList<>());
        }

        public Category(int id, String name, String slug, String iconUrl, int parentGameCategoryId, int gameId,boolean isClass,int classId,List<Category> subcategories) {
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

        public List<Category> getSubcategories() {
            return subcategories;
        }
    }
}
