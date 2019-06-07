package com.palombodev.lms.listener;

import com.palombodev.lms.LMS;
import com.palombodev.lms.kits.KitType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getInventory().getName() == null) return;
        if (!event.getInventory().getTitle().equalsIgnoreCase(LMS.getVoteGUI().getInventory().getTitle())) return;


        ItemStack item = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (item == null || item.getType() == Material.AIR) return;
        if (item.getType() == Material.STAINED_GLASS_PANE) return;

        player.closeInventory();

        switch (item.getType()) {

            case BOW:

                LMS.getVoteGUI().addVote(player, KitType.ARCHER);

                break;

            case POTION:

                LMS.getVoteGUI().addVote(player, KitType.POTION);

                break;

            case MUSHROOM_SOUP:

                LMS.getVoteGUI().addVote(player, KitType.SOUP);

                break;

            case STICK:

                LMS.getVoteGUI().addVote(player, KitType.KNOCKBACK);

                break;
        }


    }

}
