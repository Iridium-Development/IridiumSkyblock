package com.iridium.iridiumskyblock.utils;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static int getAmount(Inventory inventory, XMaterial materials) {
        int total = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;
            if (materials.isSimilar(item)) {
                total += item.getAmount();
            }
        }
        return total;
    }

    public static void removeAmount(Inventory inventory, XMaterial material, int amount) {
        int removed = 0;
        int index = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) {
                index++;
                continue;
            }
            if (removed >= amount) break;
            if (itemStack != null) {
                if (material.isSimilar(itemStack)) {
                    if (removed + itemStack.getAmount() <= amount) {
                        removed += itemStack.getAmount();
                        inventory.setItem(index, null);
                    } else {
                        itemStack.setAmount(itemStack.getAmount() - (amount - removed));
                        removed += amount;
                    }
                }
            }
            index++;
        }
    }

    public static boolean hasOpenSlot(Inventory inv) {
        return inv.firstEmpty() == -1;
    }
}
