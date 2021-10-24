package me.lafive.bridgewars.listener;

import me.lafive.bridgewars.BridgeWars;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ArenaListener implements Listener {
      @EventHandler
      public void handleEvent(BlockPlaceEvent e) {
            BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                  a.handleBukkitEvent(e);
            });
      }

      @EventHandler
      public void handleEvent(BlockBreakEvent e) {
            BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                  a.handleBukkitEvent(e);
            });
      }

      @EventHandler
      public void handleEvent(PlayerInteractEvent e) {
            BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                  a.handleBukkitEvent(e);
            });
      }

      @EventHandler
      public void handleEvent(PlayerDropItemEvent e) {
            BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                  a.handleBukkitEvent(e);
            });
      }

      @EventHandler
      public void handleEvent(InventoryClickEvent e) {
            BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                  a.handleBukkitEvent(e);
            });
      }

      @EventHandler
      public void handleEvent(EntityDamageEvent e) {
            BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                  a.handleBukkitEvent(e);
            });
      }

      @EventHandler
      public void handleEvent(PlayerQuitEvent e) {
            BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                  a.handleBukkitEvent(e);
            });
      }
}
