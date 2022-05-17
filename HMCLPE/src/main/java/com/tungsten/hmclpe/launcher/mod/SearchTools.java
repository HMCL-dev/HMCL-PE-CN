package com.tungsten.hmclpe.launcher.mod;

import android.annotation.SuppressLint;

import com.tungsten.hmclpe.launcher.mod.curse.CurseAddon;
import com.tungsten.hmclpe.launcher.mod.curse.CurseModManager;
import com.tungsten.hmclpe.launcher.mod.modrinth.Modrinth;
import com.tungsten.hmclpe.utils.string.ModTranslations;
import com.tungsten.hmclpe.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SearchTools {

    public static final String RESOURCE_DOWNLOAD_SOURCE_CURSE_FORGE = "CurseForge";
    public static final String RESOURCE_DOWNLOAD_SOURCE_MODRINTH = "Modrinth";
    public static final int SECTION_MOD = 6;
    public static final int SECTION_RESOURCE_PACK = 12;
    public static final int SECTION_WORLD = 17;
    public static final int SECTION_PACKAGE = 4471;
    public static final int SECTION_CUSTOMIZATION = 4546;
    public static final int SECTION_ADDONS = 4559; // For Pocket Edition
    public static final int DEFAULT_PAGE_OFFSET = 0;

    @SuppressLint("NewApi")
    public static Stream<ModListBean.Mod> searchImpl(String source, String gameVersion, int category, int section, int pageOffset, String searchFilter, int sort) throws Exception {
        if (StringUtils.CHINESE_PATTERN.matcher(searchFilter).find()) {
            List<ModTranslations.Mod> mods = ModTranslations.searchMod(searchFilter);
            List<String> searchFilters = new ArrayList<>();
            int count = 0;
            for (ModTranslations.Mod mod : mods) {
                String englishName = mod.getName();
                if (StringUtils.isNotBlank(mod.getSubname())) {
                    englishName = mod.getSubname();
                }
                searchFilters.add(englishName);
                count++;
                if (count >= 3) break;
            }
            return search(source,gameVersion, category, section, pageOffset, String.join(" ", searchFilters), sort);
        }
        else {
            return search(source,gameVersion, category, section, pageOffset, searchFilter, sort);
        }
    }

    public static Stream<ModListBean.Mod> search(String source, String gameVersion, int category, int section, int pageOffset, String searchFilter, int sort) throws Exception {
        if (source.equals("Modrinth")) {
            return Modrinth.searchPaginated(gameVersion, pageOffset, searchFilter,sort).stream().map(Modrinth.ModResult::toMod);
        }
        else {
            return CurseModManager.searchPaginated(gameVersion, category, section, pageOffset, searchFilter, sort).stream().map(CurseAddon::toMod);
        }
    }

}
