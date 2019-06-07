package com.palombodev.lms.game;

import com.palombodev.lms.LMS;
import com.palombodev.lms.game.customevents.GameJoinEvent;
import com.palombodev.lms.game.customevents.GameQuitEvent;
import com.palombodev.lms.game.customevents.GameStartEvent;
import com.palombodev.lms.game.customevents.GameStopEvent;
import com.palombodev.lms.util.SerializationUtil;
import com.palombodev.lms.util.config.MessagesEnum;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;

import java.util.Iterator;
import java.util.UUID;

public class GameListener implements Listener {

    @EventHandler
    public void onGameJoin(GameJoinEvent event) {
        Game game = event.getGame();
        GameManager gameManager = LMS.getGameManager();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        game.getPlayers().add(player.getUniqueId());

        gameManager.getPlayerInventory().put(uuid, player.getInventory().getContents());
        gameManager.getPlayerArmor().put(uuid, player.getInventory().getArmorContents());
        gameManager.getPlayerEXP().put(uuid, player.getLevel());
        gameManager.getPlayerLocation().put(uuid, player.getLocation());

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setExp(0);
        player.teleport(game.getLobbyLocation());
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20L);
        player.setFoodLevel(20);

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }


        LMS.getDisguiseAPI().undisguise(player);

        player.sendMessage(MessagesEnum.PICKKIT.getMessage());

    }

    @EventHandler
    public void onGameQuit(GameQuitEvent event) {
        Game game = event.getGame();
        GameManager gameManager = LMS.getGameManager();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        game.getPlayers().remove(uuid);

        player.teleport(gameManager.getPlayerLocation().get(uuid));
        player.getInventory().setContents(gameManager.getPlayerInventory().get(uuid));
        player.getInventory().setArmorContents(gameManager.getPlayerArmor().get(uuid));
        player.setLevel(gameManager.getPlayerEXP().get(uuid));

        player.updateInventory();

        if (game.getGameState() != GameState.PREGAME && game.getGameState() != GameState.INGAME) return;

        if (game.getPlayers().size() < 2) {

            if (game.getPlayers().size() == 1) {
                for (UUID u : game.getPlayers()) {
                    Player winner = Bukkit.getPlayer(u);

                    LMS.getDisguiseAPI().undisguise(winner);

                    for (PotionEffect potionEffect : winner.getActivePotionEffects()) {
                        winner.removePotionEffect(potionEffect.getType());
                    }

                    LMS.getPlugin().getServer().getPluginManager().callEvent(new GameStopEvent(winner));
                }
            } else {
                LMS.getPlugin().getServer().getPluginManager().callEvent(new GameStopEvent(null));
            }
        }
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        LMS.getDisguiseAPI().undisguise(player);
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Game game = event.getGame();

        Location lobbyLocation = SerializationUtil.stringToLocation(LMS.getPlugin().getConfig().getString("locations.lobby"));
        Location spawnLocation = SerializationUtil.stringToLocation(LMS.getPlugin().getConfig().getString("locations.spawn"));

        game.setLobbyLocation(lobbyLocation);
        game.setSpawnLocation(spawnLocation);

        game.setGameState(GameState.LOBBY);

        LMS.getVoteGUI().setVoting(true);

        Bukkit.broadcastMessage(MessagesEnum.STARTED.getMessage());


    }

    @EventHandler
    public void onGameStop(GameStopEvent event) {
        Game game = event.getGame();
        GameManager gameManager = LMS.getGameManager();

        game.setGameState(GameState.ENDING);

        Iterator<UUID> iterator = game.getPlayers().iterator();

        while (iterator.hasNext()) {
            Player player = Bukkit.getPlayer(iterator.next());

            player.teleport(gameManager.getPlayerLocation().get(player.getUniqueId()));
            player.getInventory().setContents(gameManager.getPlayerInventory().get(player.getUniqueId()));
            player.getInventory().setArmorContents(gameManager.getPlayerArmor().get(player.getUniqueId()));
            player.setExp(gameManager.getPlayerEXP().get(player.getUniqueId()));
            player.setHealth(20);
            player.setFoodLevel(20);

            player.updateInventory();
        }

        if (event.getWinner() == null) {
            Bukkit.broadcastMessage(MessagesEnum.ENDED.getMessage().replace("%winner%", "none"));
        } else {
            Bukkit.broadcastMessage(MessagesEnum.ENDED.getMessage().replace("%winner%", event.getWinner().getName()));

            for (String commands : game.getRewardCommands()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.replace("/", "").replace("%player%", event.getWinner().getName()));
            }
        }


        LMS.getGameManager().getPlayerLocation().clear();
        LMS.getGameManager().getPlayerArmor().clear();
        LMS.getGameManager().getPlayerInventory().clear();
        LMS.getGameManager().getPlayerEXP().clear();
        game.getPlayers().clear();
        LMS.getVoteGUI().reset();
    }


}
