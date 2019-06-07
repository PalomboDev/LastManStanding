package com.palombodev.lms.kits;

import com.palombodev.lms.LMS;
import com.palombodev.lms.util.SerializationUtil;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public enum KitType {

    ARCHER, POTION, SOUP, KNOCKBACK;

    public ItemStack[] getInventory() {
        try {
            return SerializationUtil.stringToItemStackArray(LMS.getConfiguration().getKit().getString(toString().toUpperCase() + ".inventory"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public ItemStack[] getArmor() {
        try {
            return SerializationUtil.stringToItemStackArray(LMS.getConfiguration().getKit().getString(toString().toUpperCase() + ".armor"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
