package me.lafive.bridgewars.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuManager {
      private Map openMenus = new HashMap();

      public void setOpenMenu(UUID player, Menu menu) {
            this.openMenus.put(player, menu);
      }

      public Menu getOpenMenu(UUID player) {
            return (Menu)this.openMenus.get(player);
      }

      public void removeOpenMenu(UUID player) {
            this.openMenus.remove(player);
      }

      public void handleInventoryClick(InventoryClickEvent e) {
            if (e.getCurrentItem() != null) {
                  Player player = (Player)e.getWhoClicked();
                  if (this.openMenus.containsKey(player.getUniqueId())) {
                        e.setCancelled(true);
                        Menu openMenu = (Menu)this.openMenus.get(player.getUniqueId());
                        MenuButton clickedButton = openMenu.getMenuButton(e.getRawSlot());
                        if (clickedButton != null && clickedButton.getWhenClicked() != null) {
                              clickedButton.getWhenClicked().accept(player);
                        }
                  }

            }
      }
}
