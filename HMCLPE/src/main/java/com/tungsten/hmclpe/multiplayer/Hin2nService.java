package com.tungsten.hmclpe.multiplayer;

import com.tungsten.hmclpe.utils.DigestUtils;

import java.util.Random;
import java.util.UUID;

import wang.switchy.hin2n.service.N2NService;
import wang.switchy.hin2n.storage.db.base.model.N2NSettingModel;

public class Hin2nService extends N2NService {

    public static final int VPN_REQUEST_CODE_CREATE = 10000;
    public static final int VPN_REQUEST_CODE_JOIN = 10001;

    public static String COMMUNITY_CODE;
    public static String IP_PORT;

    public static N2NSettingModel getCreatorModel() {
        COMMUNITY_CODE = DigestUtils.encryptToMD5(Long.toString(System.currentTimeMillis()));
        return new N2NSettingModel(null,
                1,
                "HMCL-PE-Local-Server-" + UUID.randomUUID(),
                0,
                "1.1.1.1",
                "255.255.255.0",
                COMMUNITY_CODE,
                "HMCL-PE-Password",
                "HMCL-PE Device",
                "hin2n.wang:10088",
                false,
                "",
                "05:5c:2c:af:52:b7",
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
        Random random = new Random();
        String ip = random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256) + "." + random.nextInt(256);
        return new N2NSettingModel(null,
                1,
                "HMCL-PE-Local-Server-" + UUID.randomUUID(),
                0,
                ip,
                "255.255.255.0",
                COMMUNITY_CODE,
                "HMCL-PE-Password",
                "HMCL-PE Device",
                "hin2n.wang:10088",
                false,
                "",
                "05:5c:2c:af:52:b7",
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

}
