package com.palombodev.lms.util.config;

import com.palombodev.lms.LMS;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Configuration {

    private YamlConfiguration config;
    private YamlConfiguration messages;
    private YamlConfiguration kit;

    private File configFile;
    private File messagesFile;
    private File kitFile;

    public Configuration() {
        configFile = new File(LMS.getPlugin().getDataFolder(), "config.yml");
        messagesFile = new File(LMS.getPlugin().getDataFolder(), "messages.yml");
        kitFile = new File(LMS.getPlugin().getDataFolder(), "kit.yml");

        if (!(configFile.exists()))
            LMS.getPlugin().saveResource("config.yml", false);
        config = YamlConfiguration.loadConfiguration(configFile);

        if (!(messagesFile.exists()))
            LMS.getPlugin().saveResource("messages.yml", false);
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        if (!(kitFile.exists()))
            LMS.getPlugin().saveResource("kit.yml", false);
        kit = YamlConfiguration.loadConfiguration(kitFile);
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public YamlConfiguration getMessages() {
        return messages;
    }

    public YamlConfiguration getKit() {
        return kit;
    }

    public File getConfigFile() {
        return configFile;
    }

    public File getMessagesFile() {
        return messagesFile;
    }

    public File getKitFile() {
        return kitFile;
    }

}
