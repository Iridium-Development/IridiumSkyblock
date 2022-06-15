package com.iridium.iridiumskyblock.support.material.implementations;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.iridium.iridiumcore.dependencies.xseries.XMaterial;
import com.iridium.iridiumskyblock.support.material.IridiumMaterial;

import lombok.Getter;

public class XMaterialImpl extends IridiumMaterial {

    @Getter
    private XMaterial mat;
    public XMaterialImpl(XMaterial x,String key,String extra) {
        super(key, extra);
        this.mat = x;
    }

    @Override
    public Material parseMaterial() {
        return mat.parseMaterial();
    }

    @Override
    public ItemStack parseItem() {
        ItemStack is = mat.parseItem();
        applyExtra(is);
        return is;
    }
    
}
