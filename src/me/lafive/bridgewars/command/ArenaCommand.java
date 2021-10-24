package me.lafive.bridgewars.command;

import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.arena.Arena;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaCommand implements CommandExecutor {
      public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (args.length < 1) {
                  sender.sendMessage(ChatColor.RED + "Invalid Arguments!");
                  return true;
            } else if (args[0].equalsIgnoreCase("create")) {
                  if (args.length < 2) {
                        sender.sendMessage(ChatColor.RED + "Usage: /arena create <name>");
                        return true;
                  } else {
                        BridgeWars.getInstance().getArenaManager().createArena(args[1]);
                        sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Arena created! Set it up?");
                        return true;
                  }
            } else {
                  Player player;
                  Arena targetArena;
                  if (args[0].equalsIgnoreCase("setspawnlocation")) {
                        if (!(sender instanceof Player)) {
                              return true;
                        } else {
                              player = (Player)sender;
                              if (args.length < 2) {
                                    sender.sendMessage(ChatColor.RED + "Usage: /arena setspawnlocation <arena>");
                                    return true;
                              } else {
                                    targetArena = BridgeWars.getInstance().getArenaManager().getArena(args[1]);
                                    if (targetArena == null) {
                                          sender.sendMessage(ChatColor.RED + "Arena not found!");
                                          return true;
                                    } else {
                                          targetArena.getArenaConfig().setSpawnLocation(player.getLocation());
                                          sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Spawn location set!");
                                          return true;
                                    }
                              }
                        }
                  } else if (args[0].equalsIgnoreCase("setspectatorlocation")) {
                        if (!(sender instanceof Player)) {
                              return true;
                        } else {
                              player = (Player)sender;
                              if (args.length < 2) {
                                    sender.sendMessage(ChatColor.RED + "Usage: /arena setspectatorlocation <arena>");
                                    return true;
                              } else {
                                    targetArena = BridgeWars.getInstance().getArenaManager().getArena(args[1]);
                                    if (targetArena == null) {
                                          sender.sendMessage(ChatColor.RED + "Arena not found!");
                                          return true;
                                    } else {
                                          targetArena.getArenaConfig().setSpectatorLocation(player.getLocation());
                                          sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Spectator location set!");
                                          return true;
                                    }
                              }
                        }
                  } else if (args[0].equalsIgnoreCase("setmapauthor")) {
                        if (!(sender instanceof Player)) {
                              return true;
                        } else {
                              player = (Player)sender;
                              if (args.length < 3) {
                                    sender.sendMessage(ChatColor.RED + "Usage: /arena setmapauthor <arena> <author>");
                                    return true;
                              } else {
                                    targetArena = BridgeWars.getInstance().getArenaManager().getArena(args[1]);
                                    if (targetArena == null) {
                                          sender.sendMessage(ChatColor.RED + "Arena not found!");
                                          return true;
                                    } else {
                                          targetArena.getArenaConfig().setMapAuthor(args[2]);
                                          sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Arena author set!");
                                          return true;
                                    }
                              }
                        }
                  } else {
                        int locationNumber;
                        if (args[0].equalsIgnoreCase("setmaxplayers")) {
                              if (!(sender instanceof Player)) {
                                    return true;
                              } else {
                                    player = (Player)sender;
                                    if (args.length < 3) {
                                          sender.sendMessage(ChatColor.RED + "Usage: /arena setmaxplayers <arena> <max players>");
                                          return true;
                                    } else {
                                          targetArena = BridgeWars.getInstance().getArenaManager().getArena(args[1]);
                                          if (targetArena == null) {
                                                sender.sendMessage(ChatColor.RED + "Arena not found!");
                                                return true;
                                          } else {
                                                try {
                                                      locationNumber = Integer.parseInt(args[2]);
                                                } catch (NumberFormatException var9) {
                                                      sender.sendMessage(ChatColor.RED + "Invalid max players value!");
                                                      return true;
                                                }

                                                targetArena.getArenaConfig().setMaxPlayers(locationNumber);
                                                sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Max Players set!");
                                                return true;
                                          }
                                    }
                              }
                        } else if (args[0].equalsIgnoreCase("setvoidlevel")) {
                              if (!(sender instanceof Player)) {
                                    return true;
                              } else {
                                    player = (Player)sender;
                                    if (args.length < 3) {
                                          sender.sendMessage(ChatColor.RED + "Usage: /arena setvoidlevel <arena> <Y level>");
                                          return true;
                                    } else {
                                          targetArena = BridgeWars.getInstance().getArenaManager().getArena(args[1]);
                                          if (targetArena == null) {
                                                sender.sendMessage(ChatColor.RED + "Arena not found!");
                                                return true;
                                          } else {
                                                try {
                                                      locationNumber = Integer.parseInt(args[2]);
                                                } catch (NumberFormatException var10) {
                                                      sender.sendMessage(ChatColor.RED + "Invalid void level value!");
                                                      return true;
                                                }

                                                targetArena.getArenaConfig().setVoidLevel(locationNumber);
                                                sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Void level set!");
                                                return true;
                                          }
                                    }
                              }
                        } else if (args[0].equalsIgnoreCase("setplayerlocation")) {
                              if (!(sender instanceof Player)) {
                                    return true;
                              } else {
                                    player = (Player)sender;
                                    if (args.length < 3) {
                                          sender.sendMessage(ChatColor.RED + "Usage: /arena setplayerlocation <arena> <location number>");
                                          return true;
                                    } else {
                                          targetArena = BridgeWars.getInstance().getArenaManager().getArena(args[1]);
                                          if (targetArena == null) {
                                                sender.sendMessage(ChatColor.RED + "Arena not found!");
                                                return true;
                                          } else {
                                                try {
                                                      locationNumber = Integer.parseInt(args[2]);
                                                } catch (NumberFormatException var11) {
                                                      sender.sendMessage(ChatColor.RED + "Invalid location value!");
                                                      return true;
                                                }

                                                targetArena.getArenaConfig().setPlayerLocation(locationNumber, player.getLocation());
                                                sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Player Location set!");
                                                return true;
                                          }
                                    }
                              }
                        } else {
                              sender.sendMessage(ChatColor.RED + "Invalid Arguments!");
                              return true;
                        }
                  }
            }
      }
}
