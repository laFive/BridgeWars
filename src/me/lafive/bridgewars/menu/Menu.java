package me.lafive.bridgewars.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import me.lafive.bridgewars.BridgeWars;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Menu {
      private String title;
      private int rows;
      private Map menuButtons;

      public Menu(String title, int rows) {
            this.rows = rows;
            this.title = title;
            this.menuButtons = new HashMap();
      }

      public String getTitle() {
            return this.title;
      }

      public MenuButton setMenuButton(int slot, MenuButton button) {
            this.menuButtons.put(slot, button);
            return (MenuButton)this.menuButtons.get(slot);
      }

      public void removeMenuButton(int slot) {
            this.menuButtons.remove(slot);
      }

      public MenuButton getMenuButton(int slot) {
            return (MenuButton)this.menuButtons.get(slot);
      }

      public int getRows() {
            return this.rows;
      }

      public void openMenu(Player player) {
            Inventory inv = Bukkit.createInventory((InventoryHolder)null, this.rows * 9, this.title);
            Iterator var4 = this.menuButtons.entrySet().iterator();

            while(var4.hasNext()) {
                  Entry entry = (Entry)var4.next();
                  inv.setItem((Integer)entry.getKey(), ((MenuButton)entry.getValue()).getItem());
            }

            player.openInventory(inv);
            BridgeWars.getInstance().getMenuManager().setOpenMenu(player.getUniqueId(), this);
      }
}
