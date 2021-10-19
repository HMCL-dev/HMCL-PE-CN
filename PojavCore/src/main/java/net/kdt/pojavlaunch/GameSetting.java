package net.kdt.pojavlaunch;

public class GameSetting {

    public Account account;
    public String runtimePath;
    public String currentVersion;
    public String gameFileDirectory;
    public String gameDirectory;
    public String renderer;
    public String tmpDirectory;
    public int minRam;
    public int width;
    public int height;

    public GameSetting (Account account,String runtimePath,String currentVersion,String gameFileDirectory,String gameDirectory,String renderer,String tmpDirectory,int minRam,int width,int height){
        this.account = account;
        this.runtimePath = runtimePath;
        this.currentVersion = currentVersion;
        this.gameFileDirectory = gameFileDirectory;
        this.gameDirectory = gameDirectory;
        this.renderer = renderer;
        this.tmpDirectory = tmpDirectory;
        this.minRam = minRam;
        this.width = width;
        this.height = height;
    }

}
