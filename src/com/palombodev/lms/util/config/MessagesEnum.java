package com.palombodev.lms.util.config;

import com.palombodev.lms.LMS;
import org.bukkit.ChatColor;

public enum MessagesEnum {

    USAGE("usage"),
    ADMINUSAGE("adminusage"),
    JOINED("joined"),
    LEFT("left"),
    ALREADYJOINED("alreadyjoined"),
    NOTJOINED("notjoined"),
    STARTED("started"),
    ENDED("ended"),
    COUNTDOWN("countdown"),
    WAITING("waiting"),
    STARTING("starting"),
    HOVERCLICK("hoverclick"),
    NOTJOINABLE("notjoinable"),
    ALREADYSTARTED("alreadystarted"),
    NOTSTARTED("notstarted"),
    KITSET("kitset"),
    LOBBYSET("lobbyset"),
    SPAWNSET("spawnset"),
    PREGAME("pregame"),
    PVP("pvp"),
    PVPDISABLED("pvpdisabled"),
    COMMANDDISABLED("commanddisabled"),
    MAXPLAYERS("maxplayers"),
    NOTENOUGH("notenough"),
    PICKKIT("pickkit"),
    KITTYPE("kittype"),
    VOTED("voted"),
    ALREADYVOTED("alreadyvoted"),
    NOPERMISSION("nopermission");

    private String path;

    MessagesEnum(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', LMS.getConfiguration().getMessages().getString(getPath()));
    }
}
