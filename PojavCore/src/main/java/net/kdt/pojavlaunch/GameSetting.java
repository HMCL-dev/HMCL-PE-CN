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

    public class Account {
        public int loginType;
        public String email;
        public String password;
        public String user_type;
        public String auth_session;
        public String auth_player_name;
        public String auth_uuid;
        public String auth_access_token;
        public String auth_client_token;
        public String loginServer;

        public Account (int loginType,String email,String password,String user_type,String auth_session,String auth_player_name,String auth_uuid,String auth_access_token,String auth_client_token,String loginServer){
            this.loginType = loginType;
            this.email = email;
            this.password = password;
            this.user_type = user_type;
            this.auth_session = auth_session;
            this.auth_player_name = auth_player_name;
            this.auth_uuid = auth_uuid;
            this.auth_access_token = auth_access_token;
            this.auth_client_token = auth_client_token;
            this.loginServer = loginServer;
        }
    }

}
