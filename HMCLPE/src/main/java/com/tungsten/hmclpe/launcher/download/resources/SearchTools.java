package com.tungsten.hmclpe.launcher.download.resources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.tungsten.hmclpe.R;
import com.tungsten.hmclpe.launcher.download.resources.curse.CurseAddon;
import com.tungsten.hmclpe.launcher.download.resources.curse.CurseModManager;
import com.tungsten.hmclpe.launcher.download.resources.modrinth.Modrinth;
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Stream<DownloadModManager.Mod> searchImpl(String source, String gameVersion, int category, int section, int pageOffset, String searchFilter, int sort) throws Exception {
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
        } else {
            return search(source,gameVersion, category, section, pageOffset, searchFilter, sort);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Stream<DownloadModManager.Mod> search(String source, String gameVersion, int category, int section, int pageOffset, String searchFilter, int sort) throws Exception {
        if (source.equals("Modrinth")) {
            return Modrinth.searchPaginated(gameVersion, pageOffset, searchFilter).stream().map(Modrinth.ModResult::toMod);
        } else {
            return CurseModManager.searchPaginated(gameVersion, category, section, pageOffset, searchFilter, sort).stream().map(CurseAddon::toMod);
        }
    }

    public static int getCategoryID(int position){
        int id = 0;
        switch (position){
            case 0:
                id = 0;
                break;
            case 1:
                id = 423;
                break;
            case 2:
                id = 426;
                break;
            case 3:
                id = 4485;
                break;
            case 4:
                id = 429;
                break;
            case 5:
                id = 428;
                break;
            case 6:
                id = 4486;
                break;
            case 7:
                id = 432;
                break;
            case 8:
                id = 4773;
                break;
            case 9:
                id = 430;
                break;
            case 10:
                id = 433;
                break;
            case 11:
                id = 4545;
                break;
            case 12:
                id = 427;
                break;
            case 13:
                id = 434;
                break;
            case 14:
                id = 420;
                break;
            case 15:
                id = 419;
                break;
            case 16:
                id = 412;
                break;
            case 17:
                id = 414;
                break;
            case 18:
                id = 415;
                break;
            case 19:
                id = 418;
                break;
            case 20:
                id = 413;
                break;
            case 21:
                id = 416;
                break;
            case 22:
                id = 417;
                break;
            case 23:
                id = 4843;
                break;
            case 24:
                id = 4671;
                break;
            case 25:
                id = 422;
                break;
            case 26:
                id = 425;
                break;
            case 27:
                id = 421;
                break;
            case 28:
                id = 424;
                break;
            case 29:
                id = 4558;
                break;
            case 30:
                id = 406;
                break;
            case 31:
                id = 409;
                break;
            case 32:
                id = 408;
                break;
            case 33:
                id = 411;
                break;
            case 34:
                id = 407;
                break;
            case 35:
                id = 410;
                break;
            case 36:
                id = 435;
                break;
            case 37:
                id = 436;
                break;
            case 38:
                id = 4906;
                break;
            case 39:
                id = 4780;
                break;
        }
        return id;
    }

    public static String getCategoryFromID(Context context,String id){
        String category = "";
        int categoryID = Integer.parseInt(id);
        String[] strings = context.getResources().getStringArray(R.array.download_mod_categories);
        switch (categoryID){
            case 423:
                category = strings[1];
                break;
            case 426:
                category = strings[2];
                break;
            case 4485:
                category = strings[3];
                break;
            case 429:
                category = strings[4];
                break;
            case 428:
                category = strings[5];
                break;
            case 4486:
                category = strings[6];
                break;
            case 432:
                category = strings[7];
                break;
            case 4773:
                category = strings[8];
                break;
            case 430:
                category = strings[9];
                break;
            case 443:
                category = strings[10];
                break;
            case 4545:
                category = strings[11];
                break;
            case 427:
                category = strings[12];
                break;
            case 434:
                category = strings[13];
                break;
            case 420:
                category = strings[14];
                break;
            case 419:
                category = strings[15];
                break;
            case 412:
                category = strings[16];
                break;
            case 414:
                category = strings[17];
                break;
            case 415:
                category = strings[18];
                break;
            case 418:
                category = strings[19];
                break;
            case 413:
                category = strings[20];
                break;
            case 416:
                category = strings[21];
                break;
            case 417:
                category = strings[22];
                break;
            case 4843:
                category = strings[23];
                break;
            case 4671:
                category = strings[24];
                break;
            case 422:
                category = strings[25];
                break;
            case 425:
                category = strings[26];
                break;
            case 421:
                category = strings[27];
                break;
            case 424:
                category = strings[28];
                break;
            case 4558:
                category = strings[29];
                break;
            case 406:
                category = strings[30];
                break;
            case 409:
                category = strings[31];
                break;
            case 408:
                category = strings[32];
                break;
            case 411:
                category = strings[33];
                break;
            case 407:
                category = strings[34];
                break;
            case 410:
                category = strings[35];
                break;
            case 435:
                category = strings[36];
                break;
            case 436:
                category = strings[37];
                break;
            case 4906:
                category = strings[38];
                break;
            case 4780:
                category = strings[39];
                break;
        }
        return category;
    }

}
