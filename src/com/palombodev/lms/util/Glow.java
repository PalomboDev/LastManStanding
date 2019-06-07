package com.palombodev.lms.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;


public class Glow extends Enchantment {
 

  public Glow(int id) {
      super(id);
}


@Override
  public boolean canEnchantItem(ItemStack arg0) {
      return true;
  }
 
  @Override
  public boolean conflictsWith(Enchantment arg0) {
      return false;
  }
 
  @Override
  public EnchantmentTarget getItemTarget() {
      return EnchantmentTarget.ALL;
  }
 
  @Override
  public int getMaxLevel() {
      return 1;
  }
 
  @Override
  public String getName() {
      return "Glow";
  }
 
  @Override
  public int getStartLevel() {
      return 1;
  }
  
//  public void registerGlow() {
//      try {
//          java.lang.reflect.Field f = Enchantment.class.getDeclaredField("acceptingNew");
//          f.setAccessible(true);
//          f.set(null, true);
//      }
//      catch (Exception e) {
//          e.printStackTrace();
//      }
//      try {
//          Glow glow = new Glow(70);
//          Enchantment.registerEnchantment(glow);
//      }
//      catch (IllegalArgumentException e){
//      }
//      catch(Exception e){
//          e.printStackTrace();
//      }
//  }

 
}