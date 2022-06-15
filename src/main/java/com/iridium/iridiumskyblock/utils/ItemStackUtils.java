package com.iridium.iridiumskyblock.utils;


import com.iridium.iridiumcore.IridiumCore;
import com.iridium.iridiumcore.dependencies.nbtapi.NBTCompound;
import com.iridium.iridiumcore.dependencies.nbtapi.NBTItem;
import com.iridium.iridiumcore.dependencies.nbtapi.NBTListCompound;
import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumcore.utils.Placeholder;
import com.iridium.iridiumcore.utils.SkinUtils;
import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.support.material.IridiumMaterial;
import com.iridium.iridiumskyblock.support.material.Item;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

public class ItemStackUtils {
   private static final boolean supports = XMaterial.supports(16);

   public static ItemStack makeItem(IridiumMaterial material, int amount, String name, List<String> lore) {
      ItemStack itemStack = material.parseItem();
      if (itemStack == null) {
         return null;
      } else {
         itemStack.setAmount(amount);
         ItemMeta itemMeta = itemStack.getItemMeta();
         if (itemMeta != null) {
            itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
            itemMeta.setLore(StringUtils.color(lore));
            itemMeta.setDisplayName(StringUtils.color(name));
            itemStack.setItemMeta(itemMeta);
         }

         return itemStack;
      }
   }

   public static ItemStack makeItem(Item item, List<Placeholder> placeholders) {
      ItemStack itemStack = makeItem(item.material, item.amount, StringUtils.processMultiplePlaceholders(item.displayName, placeholders), StringUtils.processMultiplePlaceholders(item.lore, placeholders));
      if (item.material == IridiumMaterial.PLAYER_HEAD && item.headData != null) {
         itemStack = setHeadData(item.headData, itemStack);
      } else if (item.material == IridiumMaterial.PLAYER_HEAD && item.headOwner != null) {
         UUID uuid;
         if (item.headOwnerUUID == null) {
            uuid = SkinUtils.getUUID(StringUtils.processMultiplePlaceholders(item.headOwner, placeholders));
         } else {
            uuid = item.headOwnerUUID;
         }

         itemStack = setHeadData(SkinUtils.getHeadData(uuid), itemStack);
      }

      return itemStack;
   }

   public static ItemStack makeItem(Item item) {
      return makeItem(item, Collections.emptyList());
   }

   public static String serialize(ItemStack itemStack) {
      try {
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
         bukkitObjectOutputStream.writeObject(itemStack);
         bukkitObjectOutputStream.flush();
         return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
      } catch (Exception var3) {
         return "";
      }
   }

   public static ItemStack deserialize(String string) {
      try {
         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(string));
         BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(byteArrayInputStream);
         return (ItemStack)bukkitObjectInputStream.readObject();
      } catch (Exception var3) {
         return XMaterial.AIR.parseItem();
      }
   }

   private static ItemStack setHeadData(String headData, ItemStack itemStack) {
      if (IridiumCore.getInstance().isTesting()) {
         return itemStack;
      } else if (headData == null) {
         return itemStack;
      } else {
         NBTItem nbtItem = new NBTItem(itemStack);
         NBTCompound skull = nbtItem.addCompound("SkullOwner");
         if (supports) {
            skull.setUUID("Id", UUID.randomUUID());
         } else {
            skull.setString("Id", UUID.randomUUID().toString());
         }

         NBTListCompound texture = skull.addCompound("Properties").getCompoundList("textures").addCompound();
         texture.setString("Value", headData);
         return nbtItem.getItem();
      }
   }
}
