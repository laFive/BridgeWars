package me.lafive.bridgewars.listener;

import me.lafive.bridgewars.BridgeWars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener implements Listener {
      @EventHandler
      public void handleEvent(InventoryClickEvent e) {
            BridgeWars.getInstance().getMenuManager().handleInventoryClick(e);
      }

      @EventHandler
      public void handleEvent(InventoryCloseEvent e) {
            BridgeWars.getInstance().getMenuManager().removeOpenMenu(e.getPlayer().getUniqueId());
      }

      @EventHandler
      public void handleEvent(PlayerQuitEvent e) {
            BridgeWars.getInstance().getMenuManager().removeOpenMenu(e.getPlayer().getUniqueId());
      }
}
