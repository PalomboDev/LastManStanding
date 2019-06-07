package com.palombodev.lms.game;

import com.palombodev.lms.LMS;
import com.palombodev.lms.game.customevents.GameQuitEvent;
import com.palombodev.lms.game.customevents.GameStartEvent;
import com.palombodev.lms.kits.KitType;
import com.palombodev.lms.util.NumberUtil;
import com.palombodev.lms.util.config.MessagesEnum;
import de.robingrether.idisguise.disguise.PlayerDisguise;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.UUID;

public class GameRunnable extends BukkitRunnable {

    private Game game;
    private int time;

    public GameRunnable() {
        this.game = LMS.getGameManager().getGame();
        time = getGame().getAutostart();
    }

    @Override
    public void run() {
        Game game = getGame();

        switch (game.getGameState()) {
            case WAITING: {

                time--;

                if (time % 3600 == 0 || time == 60 || time == 30 || (time < 6 && time > 0)) {
                    Bukkit.broadcastMessage(MessagesEnum.WAITING.getMessage()
                            .replace("%time%", NumberUtil.formatTime(time)));
                }

                if (time == 0) {
                    time = 61;
                    LMS.getPlugin().getServer().getPluginManager().callEvent(new GameStartEvent());
                }

                break;
            }

            case LOBBY: {
                time--;

                if (time > 61) {
                    time = 60;
                }

                if (time == 60 || time == 30 || (time < 6 && time > 0)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        LMS.getGameManager().getTextComponent().setText(MessagesEnum.STARTING.getMessage().replace("%time%", NumberUtil.formatTime(time)));

                        player.spigot().sendMessage(LMS.getGameManager().getTextComponent());
                    }
                }

                if (time == 0) {
                    if (game.getPlayers().size() < game.getMinplayers()) {
                        Bukkit.broadcastMessage(MessagesEnum.NOTENOUGH.getMessage());

                        for (UUID uuid : game.getPlayers()) {
                            LMS.getPlugin().getServer().getPluginManager().callEvent(new GameQuitEvent(Bukkit.getPlayer(uuid)));
                        }

                        game.setGameState(GameState.WAITING);
                        time = game.getAutostart();
                    } else {
                        time = 11;
                        game.setGameState(GameState.COUNTDOWN);
                    }
                }

                break;
            }

            case COUNTDOWN: {
                time--;

                if (time == 10) {

                    for (UUID uuid : getGame().getPlayers()) {
                        Bukkit.getPlayer(uuid).closeInventory();
                    }

                    LMS.getVoteGUI().setVoting(false);

                    game.setKitType(LMS.getVoteGUI().getMostVotedKit());

                    LMS.getGameManager().gameBroadcast(MessagesEnum.KITTYPE.getMessage()
                            .replace("%kit%", game.getKitType().toString()));

                    LMS.getGameManager().gameBroadcast(MessagesEnum.COUNTDOWN.getMessage()
                            .replace("%time%", NumberUtil.formatTime(time)));
                }

                if (time == 0) {
                    time = 10;
                    game.setGameState(GameState.PREGAME);

                    LMS.getGameManager().gameBroadcast(MessagesEnum.PREGAME.getMessage()
                            .replace("%time%", NumberUtil.formatTime(time)));

                    UUID randomUUID = random(game.getPlayers());

                    Player disguisePlayer = Bukkit.getPlayer(randomUUID);

                    game.setPlayerDisguise(new PlayerDisguise(disguisePlayer.getName(), false));

                    for (UUID uuid : game.getPlayers()) {

                        Player player = Bukkit.getPlayer(uuid);

                        LMS.getDisguiseAPI().disguise(player, game.getPlayerDisguise());

                        if (game.getKitType() == KitType.KNOCKBACK) {
                            player.teleport(game.getKnockbackLocation());
                        } else {
                            player.teleport(game.getSpawnLocation());
                        }
                    }
                }

                break;
            }

            case PREGAME: {
                time--;

                if (time == 0) {

                    time = game.getAutostart();
                    game.setGameState(GameState.INGAME);

                    for (UUID uuid : game.getPlayers()) {

                        Player player = Bukkit.getPlayer(uuid);

                        player.getInventory().setContents(game.getKitType().getInventory());
                        player.getInventory().setArmorContents(game.getKitType().getArmor());

                        if (game.getKitType() == KitType.KNOCKBACK) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                        } else {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                        }

                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));

                    }

                    LMS.getGameManager().gameBroadcast(MessagesEnum.PVP.getMessage()
                            .replace("%time%", NumberUtil.formatTime(time)));
                }

                break;
            }

            case INGAME: {

//                if (getGame().getPlayers().size() <= 10) {
//
//                    if (getGame().isDeathmatch()) return;
//
//                    getGame().setDeathmatch(true);
//
//                    for (UUID uuid : getGame().getPlayers()) {
//
//                        Player player = Bukkit.getPlayer(uuid);
//
//                        if (game.getKitType() == KitType.KNOCKBACK) {
//                            player.teleport(game.getKnockbackLocation());
//                        } else {
//                            player.teleport(game.getSpawnLocation());
//                        }
//
//                    }
//
//
//                    LMS.getGameManager().gameBroadcast(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&8[&5&lLMS&8] &aDeathMatch has started....."));
//
//                }

                break;
            }

            case ENDING: {

                time = game.getAutostart();
                game.setDeathmatch(false);
                game.setGameState(GameState.WAITING);

                break;
            }

            default: {
                break;
            }
        }
    }

    public Game getGame() {
        return game;
    }

    public static <T> T random(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for (T t : coll) if (--num < 0) return t;
        throw new AssertionError();
    }
}
