package com.palombodev.lms.listener;

import com.palombodev.lms.LMS;
import com.palombodev.lms.game.GameState;
import com.palombodev.lms.util.config.MessagesEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityListener implements Listener {

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();

            if (LMS.getGameManager().getGame().getPlayers().contains(damager.getUniqueId())) {
                if (LMS.getGameManager().getGame().getGameState() == GameState.INGAME) return;

                event.setCancelled(true);
                damager.sendMessage(MessagesEnum.PVPDISABLED.getMessage());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (LMS.getGameManager().getGame().getPlayers().contains(player.getUniqueId())) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
                    event.setCancelled(true);
            }
        }
    }
}
