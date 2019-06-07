package com.palombodev.lms.kits;

import com.palombodev.lms.util.ItemBuilder;
import com.palombodev.lms.util.config.MessagesEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Karandeep on 2017-08-05.
 */
public class VoteGUI {

    private Inventory inventory;

    private HashMap<KitType, Integer> votes;
    private ArrayList<String> votedPlayers;

    private boolean voting;

    private KitType kitType;

    private KitType overrideKit;

    public static final String VOTE_GUI_NAME = "&aVote For Kit";

    public VoteGUI() {

        inventory = Bukkit.createInventory(null, 9 * 3, ChatColor.translateAlternateColorCodes('&', VOTE_GUI_NAME));

        votes = new HashMap<>();
        votedPlayers = new ArrayList<>();
        voting = false;

        kitType = KitType.POTION;

        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).name("  ").addGlow().build();

        glass.setDurability((short) 14);

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, glass);
        }

        // 11, 13, 15

        ItemStack gapple, soup, pot, knockback;

        gapple = new ItemBuilder(Material.BOW).name("&aVote for Archer Kit").build();
        soup = new ItemBuilder(Material.POTION).name("&aVote for Potion Kit").build();
        pot = new ItemBuilder(Material.MUSHROOM_SOUP).name("&aVote for Soup Kit").build();
        knockback = new ItemBuilder(Material.STICK).name("&aVote for Knockback").build();

        inventory.setItem(10, gapple);
        inventory.setItem(12, soup);
        inventory.setItem(14, pot);
        inventory.setItem(16, knockback);

    }

    public Inventory getInventory() {
        return inventory;
    }
    
    public void reset() {
        voting = false;
        overrideKit = null;
        kitType = KitType.POTION;
        votes.clear();
        votedPlayers.clear();
    }

    public void setOverrideKit(KitType kitType) {
        this.overrideKit = kitType;
    }

    public void addVote(Player player, KitType kitType) {

        if (votedPlayers.contains(player.getName())) {
            player.sendMessage(MessagesEnum.ALREADYVOTED.getMessage());
            return;
        }

        votedPlayers.add(player.getName());

        if (!votes.containsKey(kitType)) {
            votes.put(kitType, 1);
        } else {
            votes.put(kitType, votes.get(kitType) + 1);
        }

        player.sendMessage(MessagesEnum.VOTED.getMessage());


    }

    public KitType getMostVotedKit() {

        if (overrideKit != null) {
            return overrideKit;
        }

        KitType mostVotedKit = KitType.POTION;

        if (votedPlayers.isEmpty()) {
            return mostVotedKit;
        }

        for (KitType type : votes.keySet()) {

            if (!votes.containsKey(type)) continue;

            if (!votes.containsKey(mostVotedKit)) {
                mostVotedKit = type;
                continue;
            }

            if (votes.get(type) > votes.get(mostVotedKit)) {
                mostVotedKit = type;
            }

        }


        return mostVotedKit;

    }

    public boolean isVoting() {
        return voting;
    }

    public void setVoting(boolean voting) {
        this.voting = voting;
    }

    public HashMap<KitType, Integer> getVotes() {
        return votes;
    }

}
