package com.tungsten.hmclpe.launcher.mod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModInfo {
    private Map<String, List<ModListBean.Version>> map;
    private List<ModListBean.Mod> dependencies;
    public ModInfo(ModListBean.Mod mod) throws Exception{
        map=new HashMap<>();
        dependencies=new ArrayList<>();
        List<ModListBean.Version> versions = mod.getData().loadVersions().collect(Collectors.toList());
        for (ModListBean.Version version:versions){
            add(version);
        }
        for (ModListBean.Mod m:mod.getData().loadDependencies()){
            dependencies.add(m);
        }
    }
    public List<ModListBean.Version> getVersionByGameVersion(String gameVersion){
        return map.get(gameVersion);
    }
    public List<ModListBean.Mod> getDependencies(){
        return dependencies;
    }
    public List<String> getAllSupportedGameVersion(){
        List<String> list=new ArrayList<>(map.keySet());
        list.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] valueSplit1 = o1.split("[.]");
                String[] valueSplit2 = o2.split("[.]");
                int minLength = valueSplit1.length;
                if (minLength > valueSplit2.length) {
                    minLength = valueSplit2.length;
                }
                for (int i = 0; i < minLength; i++) {
                    int value1 = Integer.parseInt(valueSplit1[i]);
                    int value2 = Integer.parseInt(valueSplit2[i]);
                    if(value1 > value2){
                        return -1;
                    }else if(value1 < value2){
                        return 1;
                    }
                }
                return valueSplit2.length-valueSplit1.length;
            }
        });
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        for (String s:map.keySet()){
            sb.append("GameVersion:"+s+"\n");
            for (ModListBean.Version v:map.get(s)){
                sb.append("Name:"+v.getFile().getFilename()+"\n");
                sb.append("Url:"+v.getFile().getUrl()+"\n");
            }
        }
        return sb.toString();
    }
    private void add(ModListBean.Version version){
        for (String s:version.getGameVersions()){
            List<ModListBean.Version> list=map.get(s);
            if (list==null){
                list=new ArrayList<>();
            }
            list.add(version);
            map.put(s,list);
        }
    }
}
