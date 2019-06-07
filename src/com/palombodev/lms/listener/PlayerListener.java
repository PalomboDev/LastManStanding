package com.palombodev.lms.listener;

import com.palombodev.lms.LMS;
import com.palombodev.lms.game.GameState;
import com.palombodev.lms.game.customevents.GameQuitEvent;
import com.palombodev.lms.util.config.MessagesEnum;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getItemInHand();

        if (item == null || item.getType() == Material.AIR) return;
        if (item.getType() != Material.MUSHROOM_SOUP) return;

        if (LMS.getGameManager().getGame().getGameState() != GameState.INGAME) return;
        if (!LMS.getGameManager().getGame().getPlayers().contains(player.getUniqueId())) return;

        if (player.getHealth() >= player.getMaxHealth()) return;

        event.setCancelled(true);

        player.setHealth(player.getHealth() + 7 >= player.getMaxHealth() ? player.getMaxHealth() : player.getHealth() + 7);

        player.setItemInHand(new ItemStack(Material.AIR));

        player.playSound(player.getLocation(), Sound.BURP, 1, 1);

        player.updateInventory();

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamagePartyFix(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        if (LMS.getGameManager().getGame().getGameState() != GameState.INGAME) return;

        if (!LMS.getGameManager().getGame().getPlayers().contains(damaged.getUniqueId())) return;
        if (!LMS.getGameManager().getGame().getPlayers().contains(damager.getUniqueId())) return;

        event.setCancelled(false);

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getDamager() instanceof Player)) return;

        Player damager = (Player) event.getDamager();
        Player damaged = (Player) event.getEntity();

        if (LMS.getGameManager().getGame().getGameState() != GameState.INGAME) return;

        if (!LMS.getGameManager().getGame().getPlayers().contains(damaged.getUniqueId())) return;
        if (LMS.getGameManager().getGame().getPlayers().contains(damager.getUniqueId())) return;

        if (!damager.isOp()) return;

        damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&5&lLMS&8] &aThis Player is &c" + damaged.getName() + "&a."));

        event.setCancelled(true);

    }

    @EventHandler
    public void onChange(FoodLevelChangeEvent event) {

        if (LMS.getGameManager().getGame().getPlayers().contains(event.getEntity().getUniqueId())) {

            if (LMS.getGameManager().getGame().getGameState() != GameState.INGAME) {

                event.setCancelled(true);

            }

        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (LMS.getGameManager().getGame().getPlayers().contains(event.getPlayer().getUniqueId())) {
            if (event.getItemDrop().getItemStack().getType() != Material.POTION && event.getItemDrop().getItemStack().getType() != Material.GLASS_BOTTLE) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (LMS.getGameManager().getGame().getPlayers().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getPlayer().hasPermission("lms.admin")) return;

        if (LMS.getGameManager().getGame().getPlayers().contains(event.getPlayer().getUniqueId())) {
            if (event.getMessage().contains("/lms") || event.getMessage().contains("/lastmanstanding")) return;
            if (LMS.getGameManager().getGame().getAllowedCommands().contains(event.getMessage())) return;

            event.setCancelled(true);
            event.getPlayer().sendMessage(MessagesEnum.COMMANDDISABLED.getMessage());
        }
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent event) {
        if (LMS.getGameManager().getGame().getPlayers().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (LMS.getGameManager().getGame().getPlayers().contains(event.getPlayer().getUniqueId())) {
            LMS.getPlugin().getServer().getPluginManager().callEvent(new GameQuitEvent(event.getPlayer()));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (LMS.getGameManager().getGame().getPlayers().contains(event.getEntity().getUniqueId())) {

            if (LMS.getGameManager().getGame().getGameState() == GameState.INGAME) {

                /*if (event.getEntity().getKiller() != null) {

                    event.setDeathMessage("§8[§5§lLMS§8] §c" + event.getEntity().getName() + " §7was slain by §c" + event.getEntity().getKiller().getName() + "§7.");

                } else {
                    event.setDeathMessage("§8[§5§lLMS§8] §c" + event.getEntity().getName() + " §7has died.");
                }*/

                event.getDrops().clear();
                event.getEntity().spigot().respawn();

            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (LMS.getGameManager().getGame().getPlayers().contains(event.getPlayer().getUniqueId())) {
            new BukkitRunnable() {
                public void run() {
                    LMS.getPlugin().getServer().getPluginManager().callEvent(new GameQuitEvent(event.getPlayer()));
                }
            }.runTaskLater(LMS.getPlugin(), 3L);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getTo().getWorld() != LMS.getGameManager().getGame().getSpawnLocation().getWorld()) return;

        if (!LMS.getGameManager().getGame().getPlayers().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou cannot teleport into the LMS world.");
        }
    }
}