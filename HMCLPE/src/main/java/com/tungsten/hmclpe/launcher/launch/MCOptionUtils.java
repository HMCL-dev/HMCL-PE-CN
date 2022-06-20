package com.tungsten.hmclpe.launcher.launch;
import static org.lwjgl.glfw.CallbackBridge.windowHeight;
import static org.lwjgl.glfw.CallbackBridge.windowWidth;

import android.os.Build;
import android.os.FileObserver;

import androidx.annotation.Nullable;

import net.kdt.pojavlaunch.utils.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MCOptionUtils
{
    private static final HashMap<String,String> parameterMap = new HashMap<>();
    private static final ArrayList<WeakReference<MCOptionListener>> optionListeners = new ArrayList<>();
    private static FileObserver fileObserver;
    public interface MCOptionListener {
         /** Called when an option is changed. Don't know which one though */
        void onOptionChanged();
    }
    
    public static void load(String gameDir) {
        if(fileObserver == null){
            setupFileObserver(gameDir);
        }

        parameterMap.clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(gameDir + "/options.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                int firstColonIndex = line.indexOf(':');
                if(firstColonIndex < 0) {
                    continue;
                }
                parameterMap.put(line.substring(0,firstColonIndex), line.substring(firstColonIndex+1));
            }
            reader.close();
        } catch (IOException e) {

        }
    }
    
    public static void set(String key, String value) {
        parameterMap.put(key,value);
    }

    /** Set an array of String, instead of a simple value. Not supported on all options */
    public static void set(String key, List<String> values){
        parameterMap.put(key, values.toString());
    }

    public static String get(String key){
        return parameterMap.get(key);
    }

    /** @return A list of values from an array stored as a string */
    public static List<String> getAsList(String key){
        String value = get(key);

        // Fallback if the value doesn't exist
        if (value == null) return new ArrayList<>();

        // Remove the edges
        value = value.replace("[", "").replace("]", "");
        if (value.isEmpty()) return new ArrayList<>();

        return Arrays.asList(value.split(","));
    }
    
    public static void save(String gameDir) {
        StringBuilder result = new StringBuilder();
        for(String key : parameterMap.keySet())
            result.append(key)
                    .append(':')
                    .append(parameterMap.get(key))
                    .append('\n');
        
        try {
            Tools.write(gameDir + "/options.txt", result.toString());
        } catch (IOException e) {

        }
    }

    /** @return The stored Minecraft GUI scale, also auto-computed if on auto-mode or improper setting */
    public static int getMcScale(String gameDir) {
        MCOptionUtils.load(gameDir);
        String str = MCOptionUtils.get("guiScale");
        int guiScale = (str == null ? 0 :Integer.parseInt(str));

        int scale = Math.max(Math.min(windowWidth / 320, windowHeight / 240), 1);
        if(scale < guiScale || guiScale == 0){
            guiScale = scale;
        }

        return guiScale;
    }

    /** Add a file observer to reload options on file change
     * Listeners get notified of the change */
    private static void setupFileObserver(String gameDir){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            fileObserver = new FileObserver(new File(gameDir + "/options.txt"), FileObserver.MODIFY) {
                @Override
                public void onEvent(int i, @Nullable String s) {
                    MCOptionUtils.load(gameDir);
                    notifyListeners();
                }
            };
        }else{
            fileObserver = new FileObserver(gameDir + "/options.txt", FileObserver.MODIFY) {
                @Override
                public void onEvent(int i, @Nullable String s) {
                    MCOptionUtils.load(gameDir);
                    notifyListeners();
                }
            };
        }

        fileObserver.startWatching();
    }

    /** Notify the option listeners */
    public static void notifyListeners(){
        for(WeakReference<MCOptionListener> weakReference : optionListeners){
            MCOptionListener optionListener = weakReference.get();
            if(optionListener == null) continue;

            optionListener.onOptionChanged();
        }
    }

    /** Add an option listener, notice how we don't have a reference to it */
    public static void addMCOptionListener(MCOptionListener listener){
        optionListeners.add(new WeakReference<>(listener));
    }

    /** Remove a listener from existence, or at least, its reference here */
    public static void removeMCOptionListener(MCOptionListener listener){
        for(WeakReference<MCOptionListener> weakReference : optionListeners){
            MCOptionListener optionListener = weakReference.get();
            if(optionListener == null) continue;
            if(optionListener == listener){
                optionListeners.remove(weakReference);
                return;
            }
        }
    }

}
