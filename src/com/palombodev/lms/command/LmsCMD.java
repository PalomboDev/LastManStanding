package com.palombodev.lms.command;

import com.palombodev.lms.LMS;
import com.palombodev.lms.game.GameState;
import com.palombodev.lms.game.customevents.GameJoinEvent;
import com.palombodev.lms.game.customevents.GameQuitEvent;
import com.palombodev.lms.game.customevents.GameStartEvent;
import com.palombodev.lms.game.customevents.GameStopEvent;
import com.palombodev.lms.kits.KitType;
import com.palombodev.lms.util.PlayerUtil;
import com.palombodev.lms.util.SerializationUtil;
import com.palombodev.lms.util.config.MessagesEnum;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class LmsCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                if (player.hasPermission("lms.admin")) {
                    player.sendMessage(MessagesEnum.ADMINUSAGE.getMessage());
                } else {
                    player.sendMessage(MessagesEnum.USAGE.getMessage());
                }
            } else {

                if (args.length == 2) {

                    if (player.isOp()) {

                        if (args[0].equalsIgnoreCase("override")) {

                            KitType kitType = KitType.valueOf(args[1].toUpperCase());

                            if (kitType == null) {

                                player.sendMessage(ChatColor.RED + "That is not a valid kit!");

                                return true;
                            }

                            LMS.getVoteGUI().setOverrideKit(kitType);

                            player.sendMessage(ChatColor.GREEN + "The Next LMS kit will be " + kitType);

                        }

                    }

                }

                switch (args[0].toLowerCase()) {
                    case "join": {

                        if (!PlayerUtil.isInventoryEmpty(player)) {

                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[§c&lLMS&8] &cYour inventory must be empty to join an LMS."));

                            return true;
                        }

                        if (LMS.getGameManager().getGame().getPlayers().contains(player.getUniqueId())) {
                            player.sendMessage(MessagesEnum.ALREADYJOINED.getMessage());
                        } else {
                            if (LMS.getGameManager().getGame().getGameState() == GameState.LOBBY) {
                                if (LMS.getGameManager().getGame().getPlayers().size() >= LMS.getGameManager().getGame().getMaxplayers()) {
                                    player.sendMessage(MessagesEnum.MAXPLAYERS.getMessage());
                                } else {
                                    LMS.getPlugin().getServer().getPluginManager().callEvent(new GameJoinEvent(player));
                                    player.sendMessage(MessagesEnum.JOINED.getMessage());
                                }
                            } else {
                                player.sendMessage(MessagesEnum.NOTJOINABLE.getMessage());
                            }
                        }

                        break;
                    }

                    case "leave": {
                        if (LMS.getGameManager().getGame().getPlayers().contains(player.getUniqueId())) {
                            LMS.getPlugin().getServer().getPluginManager().callEvent(new GameQuitEvent(player));
                            player.sendMessage(MessagesEnum.LEFT.getMessage());
                        } else {
                            player.sendMessage(MessagesEnum.NOTJOINED.getMessage());
                        }

                        break;
                    }

                    case "start": {
                        if (!player.hasPermission("lms.admin")) return true;

                        if (LMS.getGameManager().getGame().getGameState() != GameState.WAITING) {
                            player.sendMessage(MessagesEnum.ALREADYSTARTED.getMessage());
                        } else {
                            LMS.getPlugin().getServer().getPluginManager().callEvent(new GameStartEvent());
                        }


                        break;
                    }

                    case "stop": {
                        if (!player.hasPermission("lms.admin")) return true;

                        if (LMS.getGameManager().getGame().getGameState() != GameState.WAITING) {
                            LMS.getPlugin().getServer().getPluginManager().callEvent(new GameStopEvent(null));
                        } else {
                            player.sendMessage(MessagesEnum.NOTSTARTED.getMessage());
                        }

                        break;
                    }

                    case "setlobby": {
                        if (!player.hasPermission("lms.admin")) return true;

                        YamlConfiguration config = LMS.getConfiguration().getConfig();

                        config.set("locations.lobby", SerializationUtil.locationToString(player.getLocation()));

                        try {
                            config.save(LMS.getConfiguration().getConfigFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        LMS.getGameManager().getGame().setLobbyLocation(player.getLocation());

                        player.sendMessage(MessagesEnum.LOBBYSET.getMessage());

                        break;
                    }

                    case "setspawn": {
                        if (!player.hasPermission("lms.admin")) return true;

                        YamlConfiguration config = LMS.getConfiguration().getConfig();

                        config.set("locations.spawn", SerializationUtil.locationToString(player.getLocation()));

                        try {
                            config.save(LMS.getConfiguration().getConfigFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        LMS.getGameManager().getGame().setSpawnLocation(player.getLocation());

                        player.sendMessage(MessagesEnum.SPAWNSET.getMessage());

                        break;
                    }

                    case "setkit": {
                        if (!player.hasPermission("lms.admin")) return true;

                        if (args.length < 2) {
                            player.sendMessage(MessagesEnum.USAGE.getMessage());
                            player.sendMessage(ChatColor.GREEN + "§8[§c§lLMS§8] §7ARCHER, POTION, SOUP, KNOCKBACK <- Current Kit Types");
                            return true;
                        }

                        String kitType = args[1].toUpperCase();
                        KitType type = KitType.valueOf(kitType);


                        if (type == null) {
                            player.sendMessage("§8[§c§lLMS§8] §cInvalid kit.");
                            return true;
                        }
                        
                        YamlConfiguration kit = LMS.getConfiguration().getKit();

                        kit.set(type.toString().toUpperCase() + ".armor", SerializationUtil.itemStackArrayToString(player.getInventory().getArmorContents()));
                        kit.set(type.toString().toUpperCase() + ".inventory", SerializationUtil.itemStackArrayToString(player.getInventory().getContents()));

                        try {
                            kit.save(LMS.getConfiguration().getKitFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        player.sendMessage(MessagesEnum.KITSET.getMessage());

                        break;
                    }

                    case "knockback": {
                        if (!player.hasPermission("lms.admin")) return true;

                        YamlConfiguration config = LMS.getConfiguration().getConfig();

                        config.set("locations.knockback", SerializationUtil.locationToString(player.getLocation()));

                        try {
                            config.save(LMS.getConfiguration().getConfigFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        LMS.getGameManager().getGame().setKnockbackLocation(player.getLocation());

                        player.sendMessage("§8[§c§lLMS§8] §aKnockback position set.");

                        break;
                    }

                    case "vote": {

                        if (!LMS.getGameManager().getGame().getPlayers().contains(player.getUniqueId())) {
                            player.sendMessage("§8[§c§lLMS§8] §cYou must be in LMS to vote.");
                            return true;
                        }

                        if (LMS.getVoteGUI().isVoting()) {
                            player.openInventory(LMS.getVoteGUI().getInventory());
                        } else {
                            player.sendMessage("§8[§c§lLMS§8] §cYou may not vote at this time.");
                        }

                        break;
                    }

                    default: {
                        if (player.hasPermission("lms.admin")) {
                            player.sendMessage(MessagesEnum.ADMINUSAGE.getMessage());
                        } else {
                            player.sendMessage(MessagesEnum.USAGE.getMessage());
                        }

                        break;
                    }
                }
            }

        } else {

        }

        return true;
    }
}
