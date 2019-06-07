package com.palombodev.lms.game;

import com.palombodev.lms.LMS;
import com.palombodev.lms.util.SerializationUtil;
import com.palombodev.lms.util.config.MessagesEnum;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class GameManager {

    private Game game;

    public HashMap<UUID, ItemStack[]> playerInventory;
    public HashMap<UUID, ItemStack[]> playerArmor;
    public HashMap<UUID, Location> playerLocation;
    public HashMap<UUID, Integer> playerEXP;

    private TextComponent textComponent;

    public GameManager() {
        YamlConfiguration config = LMS.getConfiguration().getConfig();

        Location lobbyLocation = SerializationUtil.stringToLocation(config.getString("locations.lobby"));
        Location spawnLocation = SerializationUtil.stringToLocation(config.getString("locations.spawn"));
        Location knockbackLocation = SerializationUtil.stringToLocation(config.getString("locations.knockback"));

        this.game = new Game(
                config.getInt("settings.autostart"),
                config.getInt("settings.minplayers"),
                config.getInt("settings.maxplayers"),
                config.getStringList("settings.allowed-commands"),
                lobbyLocation,
                spawnLocation,
                knockbackLocation,
                config.getStringList("rewards.commands")
        );

        playerInventory = new HashMap<>();
        playerArmor = new HashMap<>();
        playerLocation = new HashMap<>();
        playerEXP = new HashMap<>();

        textComponent = new TextComponent(MessagesEnum.STARTING.getMessage());
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lms join"));
        textComponent.setHoverEvent(new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessagesEnum.HOVERCLICK.getMessage()).create()));
    }

    public void gameBroadcast(String message) {
        for (UUID uuid : getGame().getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);

            player.sendMessage(message);
        }
    }

    public Game getGame() {
        return game;
    }

    public HashMap<UUID, ItemStack[]> getPlayerInventory() {
        return playerInventory;
    }

    public HashMap<UUID, ItemStack[]> getPlayerArmor() {
        return playerArmor;
    }

    public HashMap<UUID, Location> getPlayerLocation() {
        return playerLocation;
    }

    public HashMap<UUID, Integer> getPlayerEXP() {
        return playerEXP;
    }

    public TextComponent getTextComponent() {
        return textComponent;
    }
}
