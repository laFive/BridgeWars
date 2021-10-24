package me.lafive.bridgewars.arena;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import java.io.File;
import java.io.IOException;
import java.util.*;

import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.arena.config.ArenaConfig;
import me.lafive.bridgewars.data.PlayerData;
import me.lafive.bridgewars.kit.Kit;
import me.lafive.bridgewars.runnable.LobbyRunnable;
import me.lafive.bridgewars.util.PacketUtil;
import me.lafive.bridgewars.util.SidebarUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import sun.corba.Bridge;

public class Arena {
      private String name;
      private ArenaConfig config;
      private boolean started;
      private int countdown;
      private int maxPlayers;
      private int ticks;
      private List<Player> players;
      private List<Player> alivePlayers;
      private Map<UUID, Kit> playerKits;
      private Map playerSpawn;

      public Arena(String name) {
            this.name = name;
            this.config = new ArenaConfig(this.name);
            this.players = new ArrayList();
            this.alivePlayers = new ArrayList();
            this.playerSpawn = new HashMap();
            this.maxPlayers = this.config.getMaxPlayers();
      }

      public void tickArena() {
            ++this.ticks;
            if (!this.started) {
                  Iterator var2 = this.players.iterator();

                  Player p;
                  while(var2.hasNext()) {
                        p = (Player)var2.next();
                        if (p.getLocation().getY() < (double)this.config.getVoidLevel()) {
                              p.teleport(this.config.getSpawnLocation());
                        }
                  }

                  if (this.alivePlayers.size() >= 2 && this.countdown == 0) {
                        this.countdown = 30;
                  } else if (this.countdown > 0 && this.alivePlayers.size() < 2) {
                        this.countdown = 0;
                  } else {
                        if (this.ticks % 20 == 0 && this.alivePlayers.size() >= 2) {
                              if (this.countdown == 1) {
                                    this.started = true;
                                    this.players.forEach((px) -> {
                                          PacketUtil.sendTitle(px, ChatColor.GOLD.toString() + ChatColor.BOLD + "Go!", ChatColor.GRAY + "Game Started!", 0, 40, 0);
                                    });
                                    this.playGameSound(Sound.NOTE_PLING, 2);
                                    this.sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Game Started!");
                                    this.alivePlayers.forEach((px) -> {
                                          px.setGameMode(GameMode.SURVIVAL);
                                          PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(px.getUniqueId());

                                    });
                                    for (int i = 0; i < config.getMaxPlayers(); i++) {
                                          for (int x = -1; x <= 1; x += 0.5) {
                                                for (int y = -1; y <= 2.5; y += 0.5) {
                                                      for (int z = -1; z <= 1; z += 0.5) {
                                                            Location nearLocation = config.getPlayerLocation(i).clone().add(x, y, z);
                                                            if (nearLocation.getBlock().getType() != Material.BARRIER) continue;
                                                            nearLocation.getBlock().setType(Material.AIR);
                                                      }
                                                }
                                          }
                                    }
                                    return;
                              }

                              --this.countdown;
                              if (this.countdown > 0 && this.countdown <= 5) {
                                    this.players.forEach((px) -> {
                                          PacketUtil.sendTitle(px, ChatColor.GOLD.toString() + ChatColor.BOLD + "Starting in " + this.countdown + "s!", ChatColor.GRAY + "Get ready to fight!", 0, 25, 0);
                                    });
                                    this.sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Game starts in " + this.countdown + " seconds!");
                                    this.playGameSound(Sound.NOTE_PLING, 1);
                              }
                        }

                        if (this.ticks % 10 == 0) {
                              var2 = this.players.iterator();

                              while(var2.hasNext()) {
                                    p = (Player)var2.next();
                                    p.getScoreboard().getTeam("PreGamePlayers").setSuffix(ChatColor.YELLOW.toString() + this.alivePlayers.size());
                                    p.getScoreboard().getTeam("StartingIn").setSuffix(ChatColor.YELLOW + this.getCountdown());
                                    if (this.alivePlayers.contains(p)) {
                                          p.setGameMode(GameMode.ADVENTURE);
                                    }
                              }
                        }

                  }
            }
      }

      public void pasteSchematic() {
            File dir = new File(BridgeWars.getInstance().getDataFolder() + "/schematics/");
            dir.mkdirs();
            File schem = new File(BridgeWars.getInstance().getDataFolder() + "/schematics/" + this.name + ".schematic");

            try {
                  EditSession editsession = ClipboardFormats.findByFile(schem).load(schem).paste(FaweAPI.getWorld(this.config.getPlayerLocation(1).getWorld().getName()), new Vector(0, 60, 0));
                  editsession.commit();
            } catch (IOException var4) {
                  var4.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while pasting an arena schematic! (IOException)");
            }

      }

      public void spreadPlayers() {
            int playerCount = 0;
            Iterator var3 = this.alivePlayers.iterator();

            while(var3.hasNext()) {
                  Player p = (Player)var3.next();
                  ++playerCount;
                  p.teleport(this.config.getPlayerLocation(playerCount));
            }

      }

      public void sendPlayer(Player player) {
            if (!this.started && this.alivePlayers.size() < this.maxPlayers) {
                  PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
                  this.players.add(player);
                  this.alivePlayers.add(player);
                  player.setAllowFlight(false);
                  player.setFlying(false);
                  player.setFireTicks(0);
                  player.setLevel(0);
                  player.setExp(0.0F);
                  player.getInventory().clear();
                  player.getInventory().setArmorContents(new ItemStack[0]);
                  Iterator var4 = player.getActivePotionEffects().iterator();
                  Kit lastKit = null;
                  for (Kit k : BridgeWars.getInstance().getKitManager().getKits()) {
                        if (k.getName().equalsIgnoreCase(data.getLastKit())) {
                              lastKit = k;
                              break;
                        }
                  }
                  if (lastKit == null || !data.isKitOwned(lastKit.getName())) {
                        for (Kit k : BridgeWars.getInstance().getKitManager().getKits()) {
                              if (k.isFree()) {
                                    lastKit = k;
                                    break;
                              }
                        }
                  }
                  playerKits.put(player.getUniqueId(), lastKit);

                  while(var4.hasNext()) {
                        PotionEffect pe = (PotionEffect)var4.next();
                        player.removePotionEffect(pe.getType());
                  }

                  for(int i = 1; i <= this.maxPlayers; ++i) {
                        if (!this.playerSpawn.containsValue(i)) {
                              this.playerSpawn.put(player, i);
                              player.teleport(this.config.getPlayerLocation(i));
                              break;
                        }
                  }

                  this.sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " joined " + ChatColor.GREEN + "(" + this.alivePlayers.size() + "/" + this.maxPlayers + ")");
                  this.playGameSound(Sound.NOTE_STICKS, 1);
                  ItemStack kitItem = new ItemStack(Material.STICK);
                  ItemMeta kitMeta = kitItem.getItemMeta();
                  kitMeta.setDisplayName(ChatColor.GOLD + "Kit Selector " + ChatColor.GRAY + "(Right Click)");
                  kitItem.setItemMeta(kitMeta);
                  ItemStack cosmeticItem = new ItemStack(Material.INK_SACK, 1, (short)15);
                  ItemMeta cosmeticMeta = cosmeticItem.getItemMeta();
                  cosmeticMeta.setDisplayName(ChatColor.GOLD + "Cosmetic Menu " + ChatColor.GRAY + "(Right Click)");
                  cosmeticItem.setItemMeta(cosmeticMeta);
                  ItemStack quitItem = new ItemStack(Material.BED);
                  ItemMeta quitMeta = quitItem.getItemMeta();
                  quitMeta.setDisplayName(ChatColor.RED + "Quit Game " + ChatColor.GRAY + "(Right Click)");
                  quitItem.setItemMeta(quitMeta);
                  player.getInventory().setItem(0, kitItem);
                  player.getInventory().setItem(7, cosmeticItem);
                  player.getInventory().setItem(8, quitItem);
                  SidebarUtil.drawPreGameSidebar(player, this);
                  player.setGameMode(GameMode.ADVENTURE);
                  data.setInGame(true);
            } else {
                  player.sendMessage(ChatColor.RED + "Failed to send you to a game! If this happens again please contact a staff member. (1)");
            }
      }

      public void handleGameLeave(Player player) {
            if (this.players.contains(player)) {
                  PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
                  data.setInGame(false);
                  player.teleport(BridgeWars.getInstance().getPluginConfig().getSpawnLocation());
                  player.getInventory().clear();
                  player.getInventory().setArmorContents(new ItemStack[0]);
                  player.getActivePotionEffects().forEach((pe) -> {
                        player.removePotionEffect(pe.getType());
                  });
                  player.setAllowFlight(false);
                  player.setGameMode(GameMode.SURVIVAL);
                  player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0F, 10.0F);
                  player.setFoodLevel(20);
                  player.setHealth(20.0D);
                  player.setFireTicks(0);
                  player.setExp(0.0F);
                  player.setLevel(0);
                  player.getInventory().setHeldItemSlot(1);
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
                  player.getInventory().setItem(0, joinItem);
                  player.getInventory().setItem(4, shopItem);
                  player.getInventory().setItem(8, hubItem);
                  if (BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId()).getLobbyRunnable() != null) {
                        BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId()).getLobbyRunnable().cancel();
                  }

                  SidebarUtil.drawLobbySidebar(player);
                  LobbyRunnable lr = new LobbyRunnable(player);
                  lr.runTaskTimer(BridgeWars.getInstance(), 1L, 10L);
                  BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId()).setLobbyRunnable(lr);
                  this.players.remove(player);
                  if (!this.started && this.alivePlayers.contains(player)) {
                        this.alivePlayers.remove(player);
                        this.playerSpawn.remove(player);
                        this.sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " quit " + ChatColor.GREEN + "(" + this.alivePlayers.size() + "/" + this.maxPlayers + ")");
                        this.playGameSound(Sound.NOTE_BASS, 5);
                  }
            }
      }

      public boolean isStarted() {
            return this.started;
      }

      public ArenaConfig getArenaConfig() {
            return this.config;
      }

      public boolean hasPlayerLocations() {
            for(int i = 1; i <= this.maxPlayers; ++i) {
                  try {
                        this.config.getPlayerLocation(i);
                  } catch (NullPointerException var3) {
                        return false;
                  }
            }

            return true;
      }

      public void handleBukkitEvent(Event event) {
            Player player;
            if (event instanceof PlayerQuitEvent) {
                  PlayerQuitEvent e = (PlayerQuitEvent)event;
                  player = e.getPlayer();
                  if (this.alivePlayers.contains(player)) {
                        if (!this.started) {
                              this.alivePlayers.remove(player);
                              this.players.remove(player);
                              this.playerSpawn.remove(player);
                              this.sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " quit " + ChatColor.GREEN + "(" + this.alivePlayers.size() + "/" + this.maxPlayers + ")");
                              this.playGameSound(Sound.NOTE_BASS, 5);
                        }
                  }
            } else if (event instanceof EntityDamageEvent) {
                  EntityDamageEvent e = (EntityDamageEvent)event;
                  if (e.getEntity() instanceof Player) {
                        player = (Player)e.getEntity();
                        if (this.alivePlayers.contains(player)) {
                              if (!this.started || this.countdown > 0) {
                                    e.setCancelled(true);
                              }

                        }
                  }
            } else if (event instanceof BlockPlaceEvent) {
                  BlockPlaceEvent e = (BlockPlaceEvent)event;
                  if (this.alivePlayers.contains(e.getPlayer())) {
                        if (!this.started || this.countdown > 0) {
                              e.setCancelled(true);
                        }

                  }
            } else if (event instanceof BlockBreakEvent) {
                  BlockBreakEvent e = (BlockBreakEvent)event;
                  if (this.alivePlayers.contains(e.getPlayer())) {
                        if (!this.started || this.countdown > 0) {
                              e.setCancelled(true);
                        }

                  }
            } else if (event instanceof PlayerInteractEvent) {
                  PlayerInteractEvent e = (PlayerInteractEvent)event;
                  if (this.players.contains(e.getPlayer())) {
                        if (e.getItem() != null && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null) {
                              String itemName = e.getItem().getItemMeta().getDisplayName();
                              if (itemName.contains("Quit Game")) {
                                    e.setCancelled(true);
                                    this.handleGameLeave(e.getPlayer());
                                    return;
                              }
                        }

                        if (this.alivePlayers.contains(e.getPlayer())) {
                              if (!this.started || this.countdown > 0) {
                                    e.setCancelled(true);
                              }

                        }
                  }
            } else if (event instanceof PlayerDropItemEvent) {
                  PlayerDropItemEvent e = (PlayerDropItemEvent)event;
                  if (this.alivePlayers.contains(e.getPlayer())) {
                        if (!this.started || this.countdown > 0) {
                              e.setCancelled(true);
                        }

                  }
            } else if (event instanceof InventoryClickEvent) {
                  InventoryClickEvent e = (InventoryClickEvent)event;
                  if (this.alivePlayers.contains((Player)e.getWhoClicked())) {
                        if (!this.started || this.countdown > 0) {
                              e.setCancelled(true);
                        }

                  }
            }
      }

      public void playGameSound(Sound sound, int pitch) {
            this.players.forEach((p) -> {
                  p.playSound(p.getLocation(), sound, 10.0F, (float)pitch);
            });
      }

      public void sendGameMessage(String message) {
            this.players.forEach((p) -> {
                  p.sendMessage(message);
            });
      }

      public String getCountdown() {
            return this.alivePlayers.size() < 2 ? "Waiting" : this.countdown + "s";
      }

      public int getAlivePlayerCount() {
            return this.alivePlayers.size();
      }

      public int getMaxPlayers() {
            return this.maxPlayers;
      }

      public String getMapName() {
            return this.name;
      }
}
