package me.lafive.bridgewars.listener;

import java.util.Iterator;
import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.data.PlayerData;
import me.lafive.bridgewars.runnable.LobbyRunnable;
import me.lafive.bridgewars.util.MenuUtil;
import me.lafive.bridgewars.util.SidebarUtil;
import me.lafive.core.Core;
import me.lafive.core.profile.PlayerProfile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {
      private BridgeWars plugin;

      public PlayerListener(BridgeWars plugin) {
            this.plugin = plugin;
      }

      @EventHandler(
            priority = EventPriority.NORMAL
      )
      public void handleEvent(PlayerJoinEvent e) {
            e.setJoinMessage("");
            this.plugin.getDataManager().createPlayerData(e.getPlayer());
            PlayerProfile profile = Core.INSTANCE.getMongoManager().getProfileManager().getProfile(e.getPlayer().getUniqueId());
            e.getPlayer().teleport(this.plugin.getPluginConfig().getSpawnLocation());
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setArmorContents(new ItemStack[0]);
            e.getPlayer().getActivePotionEffects().forEach((pe) -> {
                  e.getPlayer().removePotionEffect(pe.getType());
            });
            e.getPlayer().setAllowFlight(false);
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);
            e.getPlayer().setFoodLevel(20);
            e.getPlayer().setHealth(20.0D);
            e.getPlayer().setExp(0.0F);
            e.getPlayer().setFireTicks(0);
            e.getPlayer().setLevel(0);
            e.getPlayer().getInventory().setHeldItemSlot(1);
            ItemStack joinItem = new ItemStack(Material.BLAZE_ROD);
            ItemMeta jMeta = joinItem.getItemMeta();
            jMeta.setDisplayName(ChatColor.GOLD + "Play " + ChatColor.GRAY + "(Right Click)");
            joinItem.setItemMeta(jMeta);
            ItemStack shopItem = new ItemStack(Material.NETHER_STAR);
            ItemMeta sMeta = shopItem.getItemMeta();
            sMeta.setDisplayName(ChatColor.GOLD + "Shop " + ChatColor.GRAY + "(Right Click)");
            shopItem.setItemMeta(sMeta);
            ItemStack hubItem = new ItemStack(Material.BED);
            ItemMeta hMeta = hubItem.getItemMeta();
            hMeta.setDisplayName(ChatColor.RED + "Return to hub " + ChatColor.GRAY + "(Right Click)");
            hubItem.setItemMeta(hMeta);
            e.getPlayer().getInventory().setItem(0, joinItem);
            e.getPlayer().getInventory().setItem(4, shopItem);
            e.getPlayer().getInventory().setItem(8, hubItem);
            e.getPlayer().sendMessage(" ");
            e.getPlayer().sendMessage(ChatColor.GRAY + "Welcome to " + ChatColor.GOLD.toString() + ChatColor.BOLD + "BridgeWars!");
            e.getPlayer().sendMessage(ChatColor.GRAY + "In BridgeWars, your objective is to be the last player standing!");
            e.getPlayer().sendMessage(ChatColor.GRAY + "Fight on a bridge structure with custom kits and abilities");
            e.getPlayer().sendMessage(ChatColor.GRAY + "Who will survive?");
            e.getPlayer().sendMessage(" ");
            if (e.getPlayer().hasPermission("bridgewars.joinmsg")) {
                  Iterator var10 = Bukkit.getOnlinePlayers().iterator();

                  while(var10.hasNext()) {
                        Player player = (Player)var10.next();
                        if (player.getWorld().equals(this.plugin.getPluginConfig().getSpawnLocation().getWorld())) {
                              player.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.translateAlternateColorCodes('&', profile.getRank().getPrefix()) + e.getPlayer().getName() + ChatColor.GRAY + " joined the Lobby!");
                        }
                  }
            }

            if (BridgeWars.getInstance().getDataManager().getPlayerData(e.getPlayer().getUniqueId()).getLobbyRunnable() != null) {
                  BridgeWars.getInstance().getDataManager().getPlayerData(e.getPlayer().getUniqueId()).getLobbyRunnable().cancel();
            }

            SidebarUtil.drawLobbySidebar(e.getPlayer());
            LobbyRunnable lr = new LobbyRunnable(e.getPlayer());
            lr.runTaskTimer(BridgeWars.getInstance(), 1L, 10L);
            BridgeWars.getInstance().getDataManager().getPlayerData(e.getPlayer().getUniqueId()).setLobbyRunnable(lr);
      }

      @EventHandler
      public void handleEvent(PlayerQuitEvent e) {
            e.setQuitMessage("");
      }

      @EventHandler
      public void handleEvent(BlockBreakEvent e) {
            PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(e.getPlayer().getUniqueId());
            if (!data.isInGame() && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                  e.setCancelled(true);
            }

      }

      @EventHandler
      public void handleEvent(BlockPlaceEvent e) {
            PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(e.getPlayer().getUniqueId());
            if (!data.isInGame() && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                  e.setCancelled(true);
            }

      }

      @EventHandler
      public void handleEvent(InventoryClickEvent e) {
            PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(e.getWhoClicked().getUniqueId());
            if (!data.isInGame() && e.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
                  e.setCancelled(true);
            }

      }

      @EventHandler
      public void handleEvent(PlayerDropItemEvent e) {
            PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(e.getPlayer().getUniqueId());
            if (!data.isInGame() && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                  e.setCancelled(true);
            }

      }

      @EventHandler
      public void handleEvent(PlayerInteractEvent e) {
            PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(e.getPlayer().getUniqueId());
            if (!data.isInGame() && e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                  e.setCancelled(true);
                  if (e.getItem() == null || e.getItem().getItemMeta() == null || e.getItem().getItemMeta().getDisplayName() == null) {
                        return;
                  }

                  String usedItem = e.getItem().getItemMeta().getDisplayName();
                  if (usedItem.contains("Return to hub")) {
                        Bukkit.dispatchCommand(e.getPlayer(), "connect hub");
                        return;
                  }

                  if (usedItem.contains("Play")) {
                        MenuUtil.openPlayInventory(e.getPlayer());
                        return;
                  }

                  if (usedItem.contains("Shop")) {
                        MenuUtil.openShopMenu(e.getPlayer());
                        return;
                  }
            }

      }

      @EventHandler
      public void handleEvent(FoodLevelChangeEvent e) {
            e.setCancelled(true);
      }

      @EventHandler
      public void handleEvent(EntityDamageEvent e) {
            if (e.getEntity() instanceof Bat) {
                  e.setCancelled(true);
            } else if (e.getEntity() instanceof Player) {
                  Player p = (Player)e.getEntity();
                  PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(p.getUniqueId());
                  if (!data.isInGame()) {
                        e.setCancelled(true);
                  }

            }
      }
}
