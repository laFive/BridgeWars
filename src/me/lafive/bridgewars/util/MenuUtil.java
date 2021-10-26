package me.lafive.bridgewars.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.arena.Arena;
import me.lafive.bridgewars.cosmetic.Cosmetic;
import me.lafive.bridgewars.data.PlayerData;
import me.lafive.bridgewars.kit.Kit;
import me.lafive.bridgewars.menu.Menu;
import me.lafive.bridgewars.menu.MenuButton;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MenuUtil {
      public static void openPlayInventory(Player player) {
            Menu playMenu = new Menu(ChatColor.GOLD.toString() + ChatColor.BOLD + "Play BridgeWars", 3);

            for(int i = 0; i < 27; ++i) {
                  playMenu.setMenuButton(i, new MenuButton(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8), ChatColor.GREEN.toString()));
            }

            playMenu.setMenuButton(13, new MenuButton(new ItemStack(Material.BLAZE_POWDER), ChatColor.GOLD.toString() + ChatColor.BOLD + "Play BridgeWars " + ChatColor.GRAY + "(Click)", Arrays.asList("", ChatColor.GRAY + "Click to play BridgeWars!", ""))).setWhenClicked(new Consumer<Player>() {
                  public void accept(Player p) {
                        Arena a = BridgeWars.getInstance().getArenaManager().getArena();
                        if (a == null) {
                              p.sendMessage(ChatColor.RED + "No availible games found!");
                              p.closeInventory();
                        } else {
                              a.sendPlayer(p);
                        }
                  }
            });
            playMenu.openMenu(player);
            player.playSound(player.getLocation(), Sound.NOTE_STICKS, 10.0F, 10.0F);
      }

      public static void openShopMenu(final Player player) {
            Menu shopMenu = new Menu(ChatColor.GOLD.toString() + ChatColor.BOLD + "BridgeWars Shop", 3);

            for(int i = 0; i < 27; ++i) {
                  shopMenu.setMenuButton(i, new MenuButton(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8), ChatColor.GREEN.toString()));
            }

            shopMenu.setMenuButton(11, new MenuButton(new ItemStack(Material.CHEST), ChatColor.GOLD.toString() + ChatColor.BOLD + "Kits " + ChatColor.GRAY + "(Click)")).setWhenClicked(new Consumer<Player>() {
                  public void accept(Player p) {
                        MenuUtil.openKitShop(p);
                  }
            });
            shopMenu.setMenuButton(15, new MenuButton(new ItemStack(Material.FIREWORK), ChatColor.GOLD.toString() + ChatColor.BOLD + "Cosmetics " + ChatColor.GRAY + "(Click)")).setWhenClicked(new Consumer<Player>() {
                  public void accept(Player p) {
                        MenuUtil.openCosmeticShop(player);
                  }
            });
            shopMenu.openMenu(player);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F, 5.0F);
      }

      public static void openKitSelector(Player player, String selectedKit, Arena arena) {
            final PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
            Menu kitShopMenu = new Menu(ChatColor.GOLD.toString() + ChatColor.BOLD + "Kit Selector", 4);

            int startKitSlot;
            for(startKitSlot = 0; startKitSlot < 36; ++startKitSlot) {
                  kitShopMenu.setMenuButton(startKitSlot, new MenuButton(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8), ChatColor.GREEN.toString()));
            }

            startKitSlot = 9;
            Iterator var5 = BridgeWars.getInstance().getKitManager().getKits().iterator();

            while(var5.hasNext()) {
                  final Kit kit = (Kit)var5.next();
                  ++startKitSlot;
                  if ((startKitSlot + 1) % 9 == 0) {
                        startKitSlot += 2;
                  }

                  ItemStack kitItem = new ItemStack(kit.getItemMaterial());
                  List kitLore = new ArrayList();
                  kitLore.add(ChatColor.DARK_GRAY.toString() + "--------------------");
                  Iterator var9 = kit.getDescription().iterator();

                  while(var9.hasNext()) {
                        String s = (String)var9.next();
                        kitLore.add(ChatColor.GRAY + s);
                  }

                  kitLore.add(ChatColor.DARK_GRAY.toString() + "--------------------");
                  if (kit.getName().equalsIgnoreCase(selectedKit)) {
                        kitLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "You selected this kit!");
                  } else if (!data.isKitOwned(kit.getName()) && !kit.isFree()) {
                        kitLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "You do not own this kit!");
                  } else {
                        kitLore.add(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Click to Select!");
                  }

                  MenuButton kitButton = new MenuButton(kitItem, ChatColor.GOLD.toString() + ChatColor.BOLD + kit.getName() + " Kit", kitLore);
                  kitButton.setWhenClicked(new Consumer<Player>() {
                        public void accept(Player p) {
                              if ((data.isKitOwned(kit.getName()) || kit.isFree()) && !kit.getName().equalsIgnoreCase(selectedKit)) {
                                    arena.setKit(p.getUniqueId(), kit);
                                    p.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "You selected the " + kit.getName() + " kit!");
                                    p.playSound(p.getLocation(), Sound.NOTE_STICKS, 10, 1);
                                    p.closeInventory();
                              }
                        }
                  });
                  kitShopMenu.setMenuButton(startKitSlot, kitButton);
            }

            kitShopMenu.openMenu(player);
      }

      public static void openKitShop(Player player) {
            final PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
            Menu kitShopMenu = new Menu(ChatColor.GOLD.toString() + ChatColor.BOLD + "Kit Shop", 4);

            int startKitSlot;
            for(startKitSlot = 0; startKitSlot < 36; ++startKitSlot) {
                  kitShopMenu.setMenuButton(startKitSlot, new MenuButton(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8), ChatColor.GREEN.toString()));
            }

            startKitSlot = 9;
            Iterator var5 = BridgeWars.getInstance().getKitManager().getKits().iterator();

            while(var5.hasNext()) {
                  final Kit kit = (Kit)var5.next();
                  ++startKitSlot;
                  if ((startKitSlot + 1) % 9 == 0) {
                        startKitSlot += 2;
                  }

                  ItemStack kitItem = new ItemStack(kit.getItemMaterial());
                  List kitLore = new ArrayList();
                  kitLore.add(ChatColor.DARK_GRAY.toString() + "--------------------");
                  Iterator var9 = kit.getDescription().iterator();

                  while(var9.hasNext()) {
                        String s = (String)var9.next();
                        kitLore.add(ChatColor.GRAY + s);
                  }

                  kitLore.add(ChatColor.DARK_GRAY.toString() + "--------------------");
                  if (!data.isKitOwned(kit.getName()) && !kit.isFree()) {
                        kitLore.add(ChatColor.GRAY + "This kit costs $" + kit.getCost());
                        kitLore.add(ChatColor.GOLD + "Click to purchase!");
                  } else {
                        kitLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "You own this kit!");
                  }

                  MenuButton kitButton = new MenuButton(kitItem, ChatColor.GOLD.toString() + ChatColor.BOLD + kit.getName() + " Kit", kitLore);
                  kitButton.setWhenClicked(new Consumer<Player>() {
                        public void accept(Player p) {
                              if (!data.isKitOwned(kit.getName())) {
                                    MenuUtil.openConfirmMenu(p, kit);
                              }
                        }
                  });
                  kitShopMenu.setMenuButton(startKitSlot, kitButton);
            }

            kitShopMenu.openMenu(player);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F, 10.0F);
      }

      public static void openCosmeticShop(Player player) {
            final PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
            Menu cosmeticShopMenu = new Menu(ChatColor.GOLD.toString() + ChatColor.BOLD + "Cosmetic Shop", 4);

            int startCosmeticSlot;
            for(startCosmeticSlot = 0; startCosmeticSlot < 36; ++startCosmeticSlot) {
                  cosmeticShopMenu.setMenuButton(startCosmeticSlot, new MenuButton(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8), ChatColor.GREEN.toString()));
            }

            startCosmeticSlot = 9;
            Iterator var5 = BridgeWars.getInstance().getCosmeticManager().getCosmetics().iterator();

            while(var5.hasNext()) {
                  final Cosmetic cosmetic = (Cosmetic)var5.next();
                  ++startCosmeticSlot;
                  if ((startCosmeticSlot + 1) % 9 == 0) {
                        startCosmeticSlot += 2;
                  }

                  ItemStack cosmeticItem = new ItemStack(cosmetic.getItem());
                  List cosmeticLore = new ArrayList();
                  cosmeticLore.add(ChatColor.DARK_GRAY.toString() + "--------------------");
                  Iterator var9 = cosmetic.getDescription().iterator();

                  while(var9.hasNext()) {
                        String s = (String)var9.next();
                        cosmeticLore.add(ChatColor.GRAY + s);
                  }

                  cosmeticLore.add(ChatColor.DARK_GRAY.toString() + "--------------------");
                  if (!data.isCosmeticOwned(cosmetic.getName()) && cosmetic.getCost() != 0) {
                        cosmeticLore.add(ChatColor.GRAY + "This cosmetic costs $" + cosmetic.getCost());
                        cosmeticLore.add(ChatColor.GOLD + "Click to purchase!");
                  } else {
                        cosmeticLore.add(ChatColor.GREEN.toString() + ChatColor.BOLD + "You own this cosmetic!");
                  }

                  MenuButton kitButton = new MenuButton(cosmeticItem, ChatColor.GOLD.toString() + ChatColor.BOLD + cosmetic.getName(), cosmeticLore);
                  kitButton.setWhenClicked(new Consumer<Player>() {
                        public void accept(Player p) {
                              if (!data.isCosmeticOwned(cosmetic.getName()) && cosmetic.getCost() != 0) {
                                    MenuUtil.openConfirmMenu(p, cosmetic);
                              }
                        }
                  });
                  cosmeticShopMenu.setMenuButton(startCosmeticSlot, kitButton);
            }

            cosmeticShopMenu.openMenu(player);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F, 10.0F);
      }

      public static void openConfirmMenu(final Player player, final Kit kit) {
            Menu confirmMenu = new Menu(ChatColor.GOLD.toString() + ChatColor.BOLD + "Confirm Purchase (" + kit.getName() + " Kit)", 3);

            for(int i = 0; i < 27; ++i) {
                  confirmMenu.setMenuButton(i, new MenuButton(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8), ChatColor.GREEN.toString()));
            }

            MenuButton acceptButton = new MenuButton(new ItemStack(Material.EMERALD_BLOCK), ChatColor.GREEN.toString() + ChatColor.BOLD + "Confirm Purchase " + ChatColor.GRAY + "(Click)");
            acceptButton.setWhenClicked(new Consumer<Player>() {
                  public void accept(Player p) {
                        PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
                        if (data.getBalance() < kit.getCost()) {
                              p.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "You do not have enough money to buy this kit! Obtain money in games by getting wins and kills");
                              p.playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 10.0F, 1.0F);
                              p.closeInventory();
                        } else {
                              data.setBalance(data.getBalance() - kit.getCost());
                              data.addKit(kit.getName());
                              p.closeInventory();
                              p.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "You purchased the " + kit.getName() + " kit!");
                              p.playSound(p.getLocation(), Sound.NOTE_PLING, 10.0F, 10.0F);
                              p.playSound(p.getLocation(), Sound.LEVEL_UP, 10.0F, 3.0F);
                        }
                  }
            });
            MenuButton cancelButton = new MenuButton(new ItemStack(Material.REDSTONE_BLOCK), ChatColor.RED.toString() + ChatColor.BOLD + "Cancel Purchase " + ChatColor.GRAY + "(Click)");
            cancelButton.setWhenClicked(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 10.0F, 10.0F);
                        p.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Purchase canceled!");
                  }
            });
            confirmMenu.setMenuButton(11, acceptButton);
            confirmMenu.setMenuButton(15, cancelButton);
            confirmMenu.openMenu(player);
      }

      public static void openConfirmMenu(final Player player, final Cosmetic cosmetic) {
            Menu confirmMenu = new Menu(ChatColor.GOLD.toString() + ChatColor.BOLD + "Confirm Purchase", 3);

            for(int i = 0; i < 27; ++i) {
                  confirmMenu.setMenuButton(i, new MenuButton(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8), ChatColor.GREEN.toString()));
            }

            MenuButton acceptButton = new MenuButton(new ItemStack(Material.EMERALD_BLOCK), ChatColor.GREEN.toString() + ChatColor.BOLD + "Confirm Purchase " + ChatColor.GRAY + "(Click)");
            acceptButton.setWhenClicked(new Consumer<Player>() {
                  public void accept(Player p) {
                        PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
                        if (data.getBalance() < cosmetic.getCost()) {
                              p.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "You do not have enough money to buy this kit! Obtain money in games by getting wins and kills");
                              p.playSound(player.getLocation(), Sound.ENDERMAN_SCREAM, 10.0F, 1.0F);
                              p.closeInventory();
                        } else {
                              data.setBalance(data.getBalance() - cosmetic.getCost());
                              data.addCosmetic(cosmetic.getName());
                              p.closeInventory();
                              p.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "You purchased the " + cosmetic.getName() + " cosmetic!");
                              p.playSound(p.getLocation(), Sound.NOTE_PLING, 10.0F, 10.0F);
                              p.playSound(p.getLocation(), Sound.LEVEL_UP, 10.0F, 3.0F);
                        }
                  }
            });
            MenuButton cancelButton = new MenuButton(new ItemStack(Material.REDSTONE_BLOCK), ChatColor.RED.toString() + ChatColor.BOLD + "Cancel Purchase " + ChatColor.GRAY + "(Click)");
            cancelButton.setWhenClicked(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.closeInventory();
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 10.0F, 10.0F);
                        p.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Purchase canceled!");
                  }
            });
            confirmMenu.setMenuButton(11, acceptButton);
            confirmMenu.setMenuButton(15, cancelButton);
            confirmMenu.openMenu(player);
      }
}
