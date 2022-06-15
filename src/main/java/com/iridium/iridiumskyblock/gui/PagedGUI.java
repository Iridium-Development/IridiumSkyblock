package com.iridium.iridiumskyblock.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.iridium.iridiumskyblock.support.material.Background;
import com.iridium.iridiumskyblock.support.material.Item;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;

public abstract class PagedGUI<T> implements com.iridium.iridiumcore.gui.GUI {
   private int page;
   private final int size;
   private final Background background;
   private final Item previousPage;
   private final Item nextPage;
   private final Inventory previousInventory;
   private final Item backButton;
   private final Map<Integer, T> items = new HashMap<>();

   public PagedGUI(int page, int size, Background background, Item previousPage, Item nextPage) {
      this.page = page;
      this.size = size;
      this.background = background;
      this.previousPage = previousPage;
      this.nextPage = nextPage;
      this.previousInventory = null;
      this.backButton = null;
   }

   public PagedGUI(int page, int size, Background background, Item previousPage, Item nextPage, Inventory previousInventory, Item backButton) {
      this.page = page;
      this.size = size;
      this.background = background;
      this.previousPage = previousPage;
      this.nextPage = nextPage;
      if (previousInventory == null) {
         this.previousInventory = null;
      } else {
         this.previousInventory = previousInventory.getType() == InventoryType.CHEST ? previousInventory : null;
      }

      this.backButton = backButton;
   }

   public void addContent(Inventory inventory) {
      this.items.clear();
      InventoryUtils.fillInventory(inventory, this.background);
      if (this.isPaged()) {
         inventory.setItem(inventory.getSize() - 3, ItemStackUtils.makeItem(this.nextPage));
         inventory.setItem(inventory.getSize() - 7, ItemStackUtils.makeItem(this.previousPage));
      }

      int elementsPerPage = inventory.getSize() - (!this.isPaged() && this.previousInventory == null ? 0 : 9);
      List<T> objects = (List)this.getPageObjects().stream().skip((long)(this.page - 1) * (long)elementsPerPage).limit((long)elementsPerPage).collect(Collectors.toList());
      AtomicInteger slot = new AtomicInteger(0);
      Iterator var5 = objects.iterator();

      while(var5.hasNext()) {
         T t = (T)var5.next();
         int currentSlot = slot.getAndIncrement();
         this.items.put(currentSlot, t);
         inventory.setItem(currentSlot, this.getItemStack(t));
      }

      if (this.previousInventory != null && this.backButton != null) {
         inventory.setItem(inventory.getSize() + this.backButton.slot, ItemStackUtils.makeItem(this.backButton));
      }

   }

   public abstract Collection<T> getPageObjects();

   public abstract ItemStack getItemStack(T var1);

   public T getItem(int slot) {
      return this.items.get(slot);
   }

   public Optional<Integer> getSlot(T t) {
      return this.items.keySet().stream().filter((slot) -> {
         return this.getItem(slot).equals(t);
      }).findFirst();
   }

   public int getSize() {
      int newSize = this.size;
      if (this.size <= 0) {
         newSize = (int)(Math.ceil((double)this.getPageObjects().size() / 9.0D) * 9.0D);
      }

      return Math.max(Math.min(newSize + 9, 54), 9);
   }

   public boolean isPaged() {
      return this.getPageObjects().size() > this.getSize();
   }

   public void onInventoryClick(InventoryClickEvent event) {
      if (this.isPaged()) {
         if (event.getSlot() == this.getInventory().getSize() - 7) {
            if (this.page > 1) {
               --this.page;
               event.getWhoClicked().openInventory(this.getInventory());
            }
         } else if (event.getSlot() == this.getInventory().getSize() - 3 && (event.getInventory().getSize() - 9) * this.page < this.getPageObjects().size()) {
            ++this.page;
            event.getWhoClicked().openInventory(this.getInventory());
         }
      } else if (this.previousInventory != null && this.backButton != null && event.getSlot() == event.getInventory().getSize() + this.backButton.slot) {
         event.getWhoClicked().openInventory(this.previousInventory);
      }

   }
}
