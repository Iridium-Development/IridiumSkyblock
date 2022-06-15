package com.iridium.iridiumskyblock.support.material.implementations;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.iridium.iridiumskyblock.support.material.IridiumMaterial;

import lombok.Getter;

public class SpawnerMaterial extends IridiumMaterial {
    @Getter
    EntityType entity;

    public SpawnerMaterial(EntityType entity,String key,String extra) {
        super(key, extra);
        this.entity = entity;
    }

    @Override
    public Material parseMaterial() {
        return Material.SPAWNER;
    }

    @Override
    public ItemStack parseItem() {
       ItemStack itemStack = new ItemStack(Material.SPAWNER);
       ItemMeta itemMeta = itemStack.getItemMeta();
       if (itemMeta == null)
           return itemStack;

       BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
       CreatureSpawner creatureSpawner = (CreatureSpawner) blockStateMeta.getBlockState();
       creatureSpawner.setSpawnedType(entity);
       blockStateMeta.setBlockState(creatureSpawner);

       itemStack.setItemMeta(itemMeta);

       applyExtra(itemStack);
       return itemStack;
    }
}
