package com.tungsten.hmclpe.multiplayer;

import com.tungsten.hmclpe.utils.DigestUtils;

import java.util.Random;

import wang.switchy.hin2n.service.N2NService;
import wang.switchy.hin2n.storage.db.base.model.N2NSettingModel;

public class Hin2nService extends N2NService {

    public static final int VPN_REQUEST_CODE_CREATE = 10000;
    public static final int VPN_REQUEST_CODE_JOIN = 10001;

    public static ServerType SERVER_TYPE;

    public static String COMMUNITY_CODE;
    public static String IP_PORT;

    public static N2NSettingModel getCreatorModel() {
        SERVER_TYPE = ServerType.SERVER;
        COMMUNITY_CODE = DigestUtils.encryptToMD5(Long.toString(System.currentTimeMillis())).substring(DigestUtils.encryptToMD5(Long.toString(System.currentTimeMillis())).length() - 10);
        return new N2NSettingModel(null,
                1,
                "HMCL-PE-Local-Server-Setting",
                0,
                "1.1.1.1",
                "255.255.255.0",
                COMMUNITY_CODE,
                "HMCL-PE-Password",
                "",
                "hin2n.wang:10086",
                false,
                "",
                "",
                1386,
                "auto",
                20,
                false,
                0,
                false,
                true,
                false,
                2,
                true,
                "",
                "",
                "Twofish",
                false);
    }

    public static N2NSettingModel getPlayerModel() {
        SERVER_TYPE = ServerType.CLIENT;
        String ip = randomNumber() + "." + randomNumber() + "." + randomNumber() + "." + randomNumber();
        return new N2NSettingModel(null,
                1,
                "HMCL-PE-Local-Server-Setting",
                0,
                ip,
                "255.255.255.0",
                COMMUNITY_CODE,
                "HMCL-PE-Password",
                "",
                "hin2n.wang:10086",
                false,
                "",
                "",
                1386,
                "auto",
                20,
                false,
                0,
                false,
                true,
                false,
                2,
                true,
                "",
                "",
                "Twofish",
                false);
    }

    private static int randomNumber() {
        Random random = new Random();
        return random.nextInt(254) + 1;
    }

}
