package com.iridium.iridiumskyblock.support.material;

 import java.util.List;
 import java.util.UUID;
 
 public class Item {
    public IridiumMaterial material;
    public int amount;
    public String displayName;
    public String headData;
    public String headOwner;
    public UUID headOwnerUUID;
    public List<String> lore;
    public Integer slot;
 
    public Item(IridiumMaterial material, int amount, String displayName, List<String> lore) {
       this.material = material;
       this.amount = amount;
       this.lore = lore;
       this.displayName = displayName;
    }
 
    public Item(IridiumMaterial material, int slot, int amount, String displayName, List<String> lore) {
       this.material = material;
       this.amount = amount;
       this.lore = lore;
       this.displayName = displayName;
       this.slot = slot;
    }
 
    public Item(IridiumMaterial material, int slot, String headData, int amount, String displayName, List<String> lore) {
       this.material = material;
       this.amount = amount;
       this.lore = lore;
       this.displayName = displayName;
       this.slot = slot;
       this.headData = headData;
    }
 
    public Item(IridiumMaterial material, int slot, int amount, String displayName, String headOwner, List<String> lore) {
       this.material = material;
       this.amount = amount;
       this.lore = lore;
       this.displayName = displayName;
       this.headOwner = headOwner;
       this.slot = slot;
    }
 
    public Item(IridiumMaterial material, int amount, String displayName, String headOwner, List<String> lore) {
       this.material = material;
       this.amount = amount;
       this.lore = lore;
       this.displayName = displayName;
       this.headOwner = headOwner;
    }
 
    public Item(IridiumMaterial material, int amount, String displayName, String headOwner, UUID ownerUUID, List<String> lore) {
       this.material = material;
       this.amount = amount;
       this.lore = lore;
       this.displayName = displayName;
       this.headOwnerUUID = ownerUUID;
       this.headOwner = headOwner;
    }
 
    public Item() {
    }
 }
 