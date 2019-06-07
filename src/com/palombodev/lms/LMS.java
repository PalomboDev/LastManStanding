package com.palombodev.lms;

import com.palombodev.lms.command.LmsCMD;
import com.palombodev.lms.game.GameListener;
import com.palombodev.lms.game.GameManager;
import com.palombodev.lms.game.GameRunnable;
import com.palombodev.lms.kits.VoteGUI;
import com.palombodev.lms.listener.BlockListener;
import com.palombodev.lms.listener.EntityListener;
import com.palombodev.lms.listener.InventoryListener;
import com.palombodev.lms.listener.PlayerListener;
import com.palombodev.lms.util.config.Configuration;
import de.robingrether.idisguise.api.DisguiseAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class LMS extends JavaPlugin {

    private static LMS plugin;
    private static Configuration configuration;
    private static GameManager gameManager;
    private static VoteGUI voteGUI;
    private static DisguiseAPI disguiseAPI;

    public void onEnable() {
        plugin = this;

        configuration = new Configuration();
        gameManager = new GameManager();
        voteGUI = new VoteGUI();

        disguiseAPI = getServer().getServicesManager().getRegistration(DisguiseAPI.class).getProvider();

        getCommand("lms").setExecutor(new LmsCMD());

        getServer().getPluginManager().registerEvents(new GameListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new EntityListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getScheduler().runTaskTimer(this, new GameRunnable(), 20L, 20L);

    }

    public void onDisable() {
        for (UUID u : getGameManager().getGame().getPlayers()) {
            Player player = Bukkit.getPlayer(u);

            player.teleport(gameManager.getPlayerLocation().get(u));
            player.getInventory().setContents(gameManager.getPlayerInventory().get(u));
            player.getInventory().setArmorContents(gameManager.getPlayerArmor().get(u));
            player.setExp(gameManager.getPlayerEXP().get(u));

            getDisguiseAPI().undisguise(Bukkit.getPlayer(u));

        }
    }

    public static LMS getPlugin() {
        return plugin;
    }

    public static DisguiseAPI getDisguiseAPI() {
        return disguiseAPI;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static VoteGUI getVoteGUI() {
        return voteGUI;
    }
}
