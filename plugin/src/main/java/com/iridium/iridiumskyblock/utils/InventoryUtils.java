package com.iridium.iridiumskyblock.utils;

import com.cryptomorin.xseries.XMaterial;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Various utils which perform operations on {@link Inventory}'s.
 */
public class InventoryUtils {

    /**
     * Counts the amount of the specified Material in the Inventory.
     *
     * @param inventory The inventory which should be searched
     * @param material  The material which should be counted
     * @return The amount of the material in the inventory
     */
    public static int getAmount(Inventory inventory, XMaterial material) {
        int total = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;

            if (material.isSimilar(item)) {
                total += item.getAmount();
            }
        }
        return total;
    }

    /**
     * Removes the amount of the specified Material from the Inventory.
     *
     * @param inventory The inventory where the items should be taken from
     * @param material  The material which should be removed
     * @param amount    The amount of items of the specified material which should be removed
     */
    public static void removeAmount(Inventory inventory, XMaterial material, int amount) {
        int removed = 0;
        int index = 0;

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack == null) {
                index++;
                continue;
            }

            // Don't continue to search if there were enough items removed
            if (removed >= amount) break;

            if (material.isSimilar(itemStack)) {
                // true if there are not enough items in this slot
                if (removed + itemStack.getAmount() <= amount) {
                    removed += itemStack.getAmount();
                    inventory.setItem(index, null);
                } else {
                    // Just remove the items that should actually be removed, not the whole stack
                    itemStack.setAmount(itemStack.getAmount() - (amount - removed));
                    removed += amount;
                }
            }
            index++;
        }
    }

    /**
     * Checks if the Inventory has an empty slot where ItemStacks of any type can be stored.
     *
     * @param inventory The Inventory which should be checked
     * @return true if there is at least one empty slot
     */
    public static boolean hasEmptySlot(Inventory inventory) {
        return inventory.firstEmpty() != -1;
    }

    /**
     * Fills the provided Inventory with the filler material (black stained glass panes by default).
     * Replaces existing items.
     *
     * @param inventory The inventory which should be filled
     */
    public static void fillInventory(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().filler));
        }
    }

}
