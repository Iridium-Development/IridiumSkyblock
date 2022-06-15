package com.iridium.iridiumskyblock.support.material.implementations;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.iridium.iridiumskyblock.support.material.IridiumMaterial;

import lombok.Getter;

public class BukkitMaterial extends IridiumMaterial {
    @Getter
    Material mat;

    public BukkitMaterial(Material mat,String key, String extra) {
        super(key, extra);
        this.mat = mat;
    }

    @Override
    public Material parseMaterial() {
        return mat;
    }

    @Override
    public ItemStack parseItem() {
        ItemStack is = new ItemStack(mat);
        applyExtra(is);
        return is;
    }

}
