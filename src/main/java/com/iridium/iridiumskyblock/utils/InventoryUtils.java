package com.iridium.iridiumskyblock.utils;

import java.util.Iterator;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.iridium.iridiumskyblock.support.material.Background;
import com.iridium.iridiumskyblock.support.material.Item;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.support.material.IridiumMaterial;

public class InventoryUtils {
    public static int getAmount(Inventory inventory, IridiumMaterial material) {
        int total = 0;
        ItemStack[] var3 = inventory.getContents();
        int var4 = var3.length;
  
        for(int var5 = 0; var5 < var4; ++var5) {
           ItemStack item = var3[var5];
           if (item != null && material.isSimilar(item)) {
              total += item.getAmount();
           }
        }
  
        return total;
     }
  
     public static void removeAmount(Inventory inventory, IridiumMaterial material, int amount) {
        int removed = 0;
        int index = 0;
        ItemStack[] var5 = inventory.getContents();
        int var6 = var5.length;
  
        for(int var7 = 0; var7 < var6; ++var7) {
           ItemStack itemStack = var5[var7];
           if (itemStack == null) {
              ++index;
           } else {
              if (removed >= amount) {
                 break;
              }
  
              if (material.isSimilar(itemStack)) {
                 if (removed + itemStack.getAmount() <= amount) {
                    removed += itemStack.getAmount();
                    inventory.setItem(index, (ItemStack)null);
                 } else {
                    itemStack.setAmount(itemStack.getAmount() - (amount - removed));
                    removed += amount;
                 }
              }
  
              ++index;
           }
        }
  
     }
  
     public static boolean hasEmptySlot(Inventory inventory) {
        return inventory.firstEmpty() != -1;
     }
  
     public static void fillInventory(Inventory inventory, Background background) {
        for(int i = 0; i < inventory.getSize(); ++i) {
           inventory.setItem(i, ItemStackUtils.makeItem(background.filler));
        }
  
        if (background.items != null) {
           Iterator var4 = background.items.keySet().iterator();
  
           while(var4.hasNext()) {
              int slot = (Integer)var4.next();
              if (slot < inventory.getSize()) {
                 inventory.setItem(slot, ItemStackUtils.makeItem((Item)background.items.get(slot)));
              }
           }
  
        }
     }
  
     public static void fillInventory(Inventory inventory, Background background, List<com.iridium.iridiumcore.utils.Placeholder> placeholders) {
        for(int i = 0; i < inventory.getSize(); ++i) {
           inventory.setItem(i, ItemStackUtils.makeItem(background.filler, placeholders));
        }
  
        Iterator var5 = background.items.keySet().iterator();
  
        while(var5.hasNext()) {
           int slot = (Integer)var5.next();
           if (slot < inventory.getSize()) {
              inventory.setItem(slot, ItemStackUtils.makeItem((Item)background.items.get(slot), placeholders));
           }
        }
  
     }
}
