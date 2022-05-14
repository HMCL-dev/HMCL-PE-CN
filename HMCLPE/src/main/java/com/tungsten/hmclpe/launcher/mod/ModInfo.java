package com.tungsten.hmclpe.launcher.mod;

import java.io.IOException;
import java.util.ArrayList;
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
        dependencies.addAll(mod.getData().loadDependencies());
    }

    public List<ModListBean.Version> getVersionByGameVersion(String gameVersion){
        return map.get(gameVersion);
    }

    public List<ModListBean.Mod> getDependencies(){
        return dependencies;
    }

    public List<String> getAllSupportedGameVersion(){
        List<String> list = new ArrayList<>(map.keySet());
        list.sort((o1, o2) -> {
            String[] valueSplit1 = o1.split("[.]");
            String[] valueSplit2 = o2.split("[.]");
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
                        if (valueSplit1[i].length() < valueSplit2[i].length()) {
                            return -1;
                        }
                        else {
                            return 1;
                        }
                    }
                }
                else if (value1 < value2) {
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
}
