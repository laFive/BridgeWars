package me.lafive.bridgewars.arena;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javafx.geometry.Side;
import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.arena.config.ArenaConfig;
import me.lafive.bridgewars.arena.loot.ChestManager;
import me.lafive.bridgewars.data.PlayerData;
import me.lafive.bridgewars.kit.Kit;
import me.lafive.bridgewars.runnable.LobbyRunnable;
import me.lafive.bridgewars.util.MenuUtil;
import me.lafive.bridgewars.util.PacketUtil;
import me.lafive.bridgewars.util.PlayerUtil;
import me.lafive.bridgewars.util.SidebarUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class Arena {
      private String name;
      private ArenaConfig config;
      private boolean started;
      private boolean won;
      private int countdown;
      private ChestManager chestManager;
      private int maxPlayers;
      private int gameTime;
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
            this.playerKits = new HashMap<UUID, Kit>();
            this.maxPlayers = this.config.getMaxPlayers();
            this.chestManager = new ChestManager(this);
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
                                          SidebarUtil.drawGameSidebar(px, this);
                                    });
                                    removeBarriers();
                                    this.playGameSound(Sound.NOTE_PLING, 2);
                                    this.sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Game Started!");
                                    this.alivePlayers.forEach((px) -> {
                                          px.setGameMode(GameMode.SURVIVAL);
                                          px.setWalkSpeed(0.2F);
                                          px.getInventory().clear();
                                          playerKits.get(px.getUniqueId()).getGameStartTask().accept(px);
                                          px.setMaxHealth(40);
                                          px.setHealth(40);
                                    });
                                    started = true;
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

                              for (Player alivePlayer : alivePlayers) {
                                    PacketUtil.sendActionBar(alivePlayer, ChatColor.GRAY + "Selected Kit: " + ChatColor.GOLD + playerKits.get(alivePlayer.getUniqueId()).getName());
                              }

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
                  return;
            }

            if (ticks % 10 == 0) {
                  for (Player p : players) {
                        p.getScoreboard().getTeam("GamePlayers").setSuffix(ChatColor.YELLOW.toString() + alivePlayers.size());
                        p.getScoreboard().getTeam("GameTime").setSuffix(ChatColor.YELLOW + SidebarUtil.parseSeconds(gameTime));
                  }
                  for (Player player : players) {
                        if (player.getLocation().getY() < config.getVoidLevel()) {
                              if (alivePlayers.contains(player)) {
                                    EntityDamageEvent e = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.VOID, 99999);
                                    Bukkit.getPluginManager().callEvent(e);
                              } else {
                                    player.teleport(config.getSpectatorLocation());
                              }
                        }
                  }
            }

            if (ticks % 20 == 0) {
                  gameTime++;
            }

            if (won) {

                  if (ticks % 20 == 0) {
                        if (ticks % 40 == 0) {
                              PlayerUtil.launchFirework(alivePlayers.get(0), Color.ORANGE);
                        } else {
                              PlayerUtil.launchFirework(alivePlayers.get(0), Color.YELLOW);
                        }
                  }

                  if (ticks > 220) {

                        won = false;
                        alivePlayers.clear();
                        playerKits.clear();
                        players.forEach(p -> handleGameLeave(p, false));
                        players.clear();
                        playerSpawn.clear();
                        gameTime = 0;
                        pasteSchematic();
                        chestManager.fillChestsAsync();
                        ticks = 0;
                        countdown = 0;
                        started = false;

                  }

            }
      }

      public void removeBarriers() {
            for (int i = 1; i <= config.getMaxPlayers(); i++) {
                  Location rawNearLocation = config.getPlayerLocation(i);
                  for (double x = -1; x <= 1; x += 0.5) {
                        for (double y = -1; y <= 2.5; y += 0.5) {
                              for (double z = -1; z <= 1; z += 0.5) {
                                    Location nearLocation = rawNearLocation.clone().add(x, y, z);
                                    if (nearLocation.getBlock().getType() != Material.BARRIER) continue;
                                    nearLocation.getBlock().setType(Material.AIR);
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

      public void handleWin(Player winner) {

            players.forEach(p -> PacketUtil.sendTitle(p, ChatColor.RED.toString() + ChatColor.BOLD + "Game End!", ChatColor.GRAY + "Winner: " + winner.getDisplayName(), 0, 120, 0));
            PacketUtil.sendTitle(winner, ChatColor.GOLD.toString() + ChatColor.BOLD + "WINNER", ChatColor.GRAY + "You win, Congratulations!", 0, 120, 0);
            sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Game Over! Congratulations to " + winner.getDisplayName() + ChatColor.GRAY + " for winning!");
            ticks = 19;
            won = true;

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
                  player.setWalkSpeed(0);
                  player.setExp(0.0F);
                  player.getInventory().clear();
                  player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
                  Iterator var4 = player.getActivePotionEffects().iterator();
                  Kit lastKit = null;
                  for (Kit k : BridgeWars.getInstance().getKitManager().getKits()) {
                        if (k.getName().equalsIgnoreCase(data.getLastKit())) {
                              lastKit = k;
                              break;
                        }
                  }
                  if (lastKit == null || (!data.isKitOwned(lastKit.getName()) && !lastKit.isFree())) {
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

      public void handleGameLeave(Player player, boolean removeFromPlayers) {
            if (this.players.contains(player)) {
                  PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
                  data.setInGame(false);
                  player.teleport(BridgeWars.getInstance().getPluginConfig().getSpawnLocation());
                  player.getInventory().clear();
                  player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
                  player.getActivePotionEffects().forEach((pe) -> {
                        player.removePotionEffect(pe.getType());
                  });
                  player.setAllowFlight(false);
                  player.setGameMode(GameMode.SURVIVAL);
                  player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0F, 0);
                  player.setFoodLevel(20);
                  player.setHealth(20.0D);
                  player.setMaxHealth(20);
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
                  if (removeFromPlayers) {
                        players.remove(player);
                  }
                  if (this.alivePlayers.contains(player)) {
                        this.alivePlayers.remove(player);
                        if (!this.started) {
                              player.setWalkSpeed(0.2F);
                              this.playerKits.remove(player.getUniqueId());
                              this.playerSpawn.remove(player);
                              this.sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " quit " + ChatColor.GREEN + "(" + this.alivePlayers.size() + "/" + this.maxPlayers + ")");
                              this.playGameSound(Sound.NOTE_BASS, 5);
                              return;
                        }
                        PlayerUtil.dropPlayerItems(player);

                        if (System.currentTimeMillis() - data.getLastAttacked() < 20000L) {
                              sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " was killed by " + data.getLastAttacker().getDisplayName() + ChatColor.GRAY + ".");
                        } else {
                              sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " took the L.");
                        }

                        playGameSound(Sound.ENDERDRAGON_HIT, 1);

                        if (alivePlayers.size() == 1) {
                              handleWin(alivePlayers.get(0));
                        }
                  }
            }
      }

      public void setKit(UUID uuid, Kit kit) {
            playerKits.put(uuid, kit);
            BridgeWars.getInstance().getDataManager().getPlayerData(uuid).setLastKit(kit.getName());
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
                        this.players.remove(player);
                        this.alivePlayers.remove(player);
                        if (!this.started) {
                              this.playerKits.remove(player.getUniqueId());
                              this.playerSpawn.remove(player);
                              this.sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " quit " + ChatColor.GREEN + "(" + this.alivePlayers.size() + "/" + this.maxPlayers + ")");
                              this.playGameSound(Sound.NOTE_BASS, 5);
                              return;
                        }
                        if (won) return;
                        PlayerUtil.dropPlayerItems(player);

                        PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
                        if (System.currentTimeMillis() - data.getLastAttacked() < 20000L) {
                              sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " was killed by " + data.getLastAttacker().getDisplayName() + ChatColor.GRAY + ".");
                        } else {
                              sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " took the L.");
                        }

                        playGameSound(Sound.ENDERDRAGON_HIT, 1);

                        if (alivePlayers.size() == 1) {
                              handleWin(alivePlayers.get(0));
                        }

                  }
            } if (event instanceof EntityDamageByEntityEvent) {
                  EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
                  if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                        Player victim = (Player) e.getEntity();
                        Player attacker = (Player) e.getDamager();
                        if (alivePlayers.contains(victim) && players.contains(attacker)) {
                              if (!alivePlayers.contains(attacker)) {
                                    e.setCancelled(true);
                              }
                        }
                  }
            } if (event instanceof EntityDamageEvent) {
                  EntityDamageEvent e = (EntityDamageEvent)event;
                  if (e.getEntity() instanceof Player) {
                        player = (Player)e.getEntity();
                        if (this.alivePlayers.contains(player)) {
                              if (!this.started) {
                                    e.setCancelled(true);
                                    return;
                              }
                              if (won) {
                                    e.setCancelled(true);
                                    return;
                              }
                              if (e.getDamage() > (player.getHealth())) {

                                    e.setCancelled(true);
                                    alivePlayers.remove(player);
                                    PlayerUtil.dropPlayerItems(player);
                                    PacketUtil.vanishPlayer(player, players);
                                    PlayerUtil.makeSpectator(player, config.getSpectatorLocation());

                                    PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
                                    if (System.currentTimeMillis() - data.getLastAttacked() < 20000L) {
                                          sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " was killed by " + data.getLastAttacker().getDisplayName() + ChatColor.GRAY + ".");
                                    } else {
                                          sendGameMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + player.getDisplayName() + ChatColor.GRAY + " took the L.");
                                    }

                                    for (Player p : players) {
                                          if (p != player) p.playSound(p.getLocation(), Sound.ENDERDRAGON_HIT, 10, 1 );
                                    }

                                    if (alivePlayers.size() == 1) {
                                          handleWin(alivePlayers.get(0));
                                    }

                              }
                        } else if (players.contains(player)) {
                              e.setCancelled(true);
                        }
                  }
            } if (event instanceof BlockPlaceEvent) {
                  BlockPlaceEvent e = (BlockPlaceEvent)event;
                  if (this.alivePlayers.contains(e.getPlayer())) {
                        if (!this.started) {
                              e.setCancelled(true);
                        }

                  } else if (players.contains(e.getPlayer())) {
                        e.setCancelled(true);
                  }
            } if (event instanceof BlockBreakEvent) {
                  BlockBreakEvent e = (BlockBreakEvent)event;
                  if (this.alivePlayers.contains(e.getPlayer())) {
                        if (!this.started) {
                              e.setCancelled(true);
                        }

                  } else if (players.contains(e.getPlayer())) {
                        e.setCancelled(true);
                  }
            } if (event instanceof PlayerInteractEvent) {
                  PlayerInteractEvent e = (PlayerInteractEvent)event;
                  if (this.players.contains(e.getPlayer())) {
                        if (e.getItem() != null && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null) {
                              String itemName = e.getItem().getItemMeta().getDisplayName();
                              if (itemName.contains("Quit Game")) {
                                    e.setCancelled(true);
                                    this.handleGameLeave(e.getPlayer(), true);
                                    return;
                              }
                              if (itemName.contains("Kit Selector")) {
                                    e.setCancelled(true);
                                    MenuUtil.openKitSelector(e.getPlayer(), playerKits.get(e.getPlayer().getUniqueId()).getName(), this);
                                    return;
                              }
                        }

                        if (this.alivePlayers.contains(e.getPlayer())) {
                              if (!this.started) {
                                    e.setCancelled(true);
                                    return;
                              }

                        } else {
                              e.setCancelled(true);
                        }
                  }
            } if (event instanceof PlayerDropItemEvent) {
                  PlayerDropItemEvent e = (PlayerDropItemEvent)event;
                  if (this.alivePlayers.contains(e.getPlayer())) {
                        if (!this.started) {
                              e.setCancelled(true);
                        }

                  }
                  if (this.players.contains(e.getPlayer()) && !this.alivePlayers.contains(e.getPlayer())) {
                        e.setCancelled(true);
                  }
            } if (event instanceof InventoryClickEvent) {
                  InventoryClickEvent e = (InventoryClickEvent)event;
                  if (this.alivePlayers.contains((Player)e.getWhoClicked())) {
                        if (!this.started) {
                              e.setCancelled(true);
                        }

                  }
                  if (this.players.contains((Player) e.getWhoClicked()) && !this.alivePlayers.contains((Player) e.getWhoClicked())) {
                        e.setCancelled(true);
                  }
            } if (event instanceof PlayerPickupItemEvent) {
                  PlayerPickupItemEvent e = (PlayerPickupItemEvent) event;
                  if (players.contains(e.getPlayer()) && !alivePlayers.contains(e.getPlayer())) {
                        e.setCancelled(true);
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

      public int getGameTime() {
            return gameTime;
      }

      public int getMaxPlayers() {
            return this.maxPlayers;
      }

      public String getMapName() {
            return this.name;
      }

      public ChestManager getChestManager() {
            return chestManager;
      }

}
