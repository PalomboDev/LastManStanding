package com.palombodev.lms.util;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {

    public static void sendActionBar(Player player, String message) {
        PacketPlayOutChat actionbar = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer
                .a("{\"text\":\"ACTIONBARMESSAGE\"}".replace("ACTIONBARMESSAGE", message)), (byte) 2);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionbar);
    }

    public static boolean isInventoryEmpty(Player player) {

        for (ItemStack itemStack : player.getInventory()) {

            if (itemStack == null || itemStack.getType() == Material.AIR) continue;

            return false;

        }

        if (player.getInventory().getHelmet() != null
                || player.getInventory().getChestplate() != null
                || player.getInventory().getLeggings() != null
                || player.getInventory().getBoots() != null) {
            return false;
        }

        return true;

    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

}
