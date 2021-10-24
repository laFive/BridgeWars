package me.lafive.bridgewars.menu;

import java.util.List;
import java.util.function.Consumer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuButton {
      private ItemStack item;
      private Consumer whenClicked;

      public MenuButton(ItemStack item) {
            this.item = item;
      }

      public MenuButton(ItemStack item, String displayName) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            item.setItemMeta(meta);
            this.item = item;
      }

      public MenuButton(ItemStack item, String displayName, List lore) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            item.setItemMeta(meta);
            this.item = item;
      }

      public String getName() {
            return this.item.getItemMeta().getDisplayName();
      }

      public ItemStack getItem() {
            return this.item;
      }

      public Consumer getWhenClicked() {
            return this.whenClicked;
      }

      public void setWhenClicked(Consumer whenClicked) {
            this.whenClicked = whenClicked;
      }
}
