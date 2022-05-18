package com.tungsten.hmclpe.launcher.mod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModInfo {

    private final Map<String, List<ModListBean.Version>> map;
    private final List<ModListBean.Mod> dependencies;

    public ModInfo(ModListBean.Mod mod) throws IOException {
        map = new HashMap<>();
        dependencies = new ArrayList<>();
        List<ModListBean.Version> versions = mod.getData().loadVersions().collect(Collectors.toList());
        for (ModListBean.Version version : versions){
            add(version);
        }
        dependencies.addAll(mod.getData().loadDependencies(versions));
    }

    public List<ModListBean.Version> getVersionByGameVersion(String gameVersion){
        List<ModListBean.Version> list = map.get(gameVersion);
        if (list != null) {
            list.sort(new ModVersionCompareTool());
        }
        return list;
    }

    public List<ModListBean.Mod> getDependencies(){
        return dependencies;
    }

    public static String replaceAllChar(String raw) {
        return raw.replaceAll("[^\\d]", "");
    }

    public List<String> getAllSupportedGameVersion(){
        List<String> list = new ArrayList<>(map.keySet());
        list.sort((o1, o2) -> {
            String f1 = o1;
            String f2 = o2;
            if (o1.contains("w")) {
                f1 = "1." + (Integer.parseInt(o1.split("w")[0]) - 3) + "-" + replaceAllChar(o1.split("w")[1]) + "-" + o1.substring(5,6);
            }
            if (o2.contains("w")) {
                f2 = "1." + (Integer.parseInt(o2.split("w")[0]) - 3) + "-" + replaceAllChar(o2.split("w")[1]) + "-" + o2.substring(5,6);
            }
            String[] valueSplit1 = f1.split("[.]");
            String[] valueSplit2 = f2.split("[.]");
            int minLength = valueSplit1.length;
            if (minLength > valueSplit2.length) {
                minLength = valueSplit2.length;
            }
            for (int i = 0; i < minLength; i++) {
                int value1 = Integer.parseInt(valueSplit1[i].split("-")[0]);
                int value2 = Integer.parseInt(valueSplit2[i].split("-")[0]);
                if (value1 > value2) {
                    return -1;
                }
                else if (value1 == value2) {
                    if (i == minLength - 1 && valueSplit1.length == valueSplit2.length) {
                        if (valueSplit1[i].split("-").length < valueSplit2[i].split("-").length) {
                            return -1;
                        }
                        if (valueSplit1[i].split("-").length == valueSplit2[i].split("-").length) {
                            if (valueSplit1[i].split("-").length == 2) {
                                if (valueSplit1[i].split("-")[1].length() < valueSplit2[i].split("-")[1].length()) {
                                    return -1;
                                }
                                else if (valueSplit1[i].split("-")[1].length() == valueSplit2[i].split("-")[1].length()) {
                                    return Integer.compare(Integer.parseInt(replaceAllChar(valueSplit2[i].split("-")[1])),Integer.parseInt(replaceAllChar(valueSplit1[i].split("-")[1])));
                                }
                                else {
                                    return 1;
                                }
                            }
                            else {
                                if (Integer.parseInt(valueSplit1[i].split("-")[1]) > Integer.parseInt(valueSplit2[i].split("-")[1])) {
                                    return -1;
                                }
                                else if (Integer.parseInt(valueSplit1[i].split("-")[1]) == Integer.parseInt(valueSplit2[i].split("-")[1])) {
                                    return Character.compare(valueSplit2[i].split("-")[1].charAt(0),valueSplit1[i].split("-")[1].charAt(0));
                                }
                                else {
                                    return 1;
                                }
                            }
                        }
                        else {
                            return 1;
                        }
                    }
                }
                else {
                    return 1;
                }
            }
            return valueSplit2.length - valueSplit1.length;
        });
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : map.keySet()){
            sb.append("GameVersion:").append(s).append("\n");
            for (ModListBean.Version v : Objects.requireNonNull(map.get(s))){
                sb.append("Name:").append(v.getFile().getFilename()).append("\n");
                sb.append("Url:").append(v.getFile().getUrl()).append("\n");
            }
        }
        return sb.toString();
    }

    private void add(ModListBean.Version version){
        for (String s : version.getGameVersions()){
            List<ModListBean.Version> list = map.get(s);
            if (list == null){
                list = new ArrayList<>();
            }
            list.add(version);
            map.put(s,list);
        }
    }

    private static class ModVersionCompareTool implements Comparator<ModListBean.Version> {
        @Override
        public int compare(ModListBean.Version versionPri, ModListBean.Version versionSec) {
            return Long.compare(versionSec.getDatePublished().toEpochMilli(), versionPri.getDatePublished().toEpochMilli());
        }
    }
}
