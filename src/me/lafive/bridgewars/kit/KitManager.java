package me.lafive.bridgewars.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitManager {
      private List<Kit> kits = new ArrayList();

      public KitManager() {
            this.kits.add((new Kit("Healer", 0, Arrays.asList("Get 3 golden apples at the start", "of the game and regeneration I", "throughout the game!"))).setItemMaterial(Material.GOLDEN_APPLE).setFree(true).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 999999, 0));
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 3)});
                  }
            }));
            this.kits.add((new Kit("Builder", 0, Arrays.asList("Get a stack of sandstone and", "a diamond pickaxe at the start", "of the game!"))).setItemMaterial(Material.BRICK).setFree(true).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_PICKAXE)});
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.SANDSTONE, 64)});
                  }
            }));
            this.kits.add((new Kit("Predator", 2000, Arrays.asList("Get 3 ender pearls at the", "start of the game!"))).setItemMaterial(Material.ENDER_PEARL).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.ENDER_PEARL, 3)});
                  }
            }));
            this.kits.add((new Kit("Leaper", 2000, Arrays.asList("Have the ability to double jump", "during games by double-tapping", "the space bar!"))).setItemMaterial(Material.SLIME_BLOCK).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.sendMessage(ChatColor.GOLD + "[KIT] " + ChatColor.GRAY + "Double-Tap the space bar to leap! (Leaper Kit)");
                  }
            }));
            this.kits.add((new Kit("Anti-Projectile", 1500, Arrays.asList("Take no knockback from eggs, snowballs", "or arrows and start the game with", "8 eggs!"))).setItemMaterial(Material.EGG).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.sendMessage(ChatColor.GOLD + "[KIT] " + ChatColor.GRAY + "You take no knockback from projectiles! (Anti-Projectile Kit)");
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.EGG, 8)});
                  }
            }));
            this.kits.add((new Kit("Wither", 1500, Arrays.asList("Start the game with 5 snowballs", "that give victims the Wither IV", "effect for 5 seconds!"))).setItemMaterial(Material.SOUL_SAND).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        ItemStack witherSnowballs = new ItemStack(Material.SNOW_BALL, 5);
                        ItemMeta wMeta = witherSnowballs.getItemMeta();
                        wMeta.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "Wither Snowball");
                        wMeta.setLore(Arrays.asList(ChatColor.GRAY + "Wither Snowballs give victims the", ChatColor.GRAY + "Wither IV effect for 5 seconds!"));
                        witherSnowballs.setItemMeta(wMeta);
                        p.getInventory().addItem(new ItemStack[]{witherSnowballs});
                  }
            }));
            this.kits.add((new Kit("Pot", 1500, Arrays.asList("Start the game with 3 health potions", "and 1 speed II potion"))).setItemMaterial(Material.POTION).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        ItemStack healthPotion = new ItemStack(Material.POTION, 3, (short)69);
                        ItemStack speedPotion = new ItemStack(Material.POTION, 1, (short)2);
                        p.getInventory().addItem(new ItemStack[]{healthPotion});
                        p.getInventory().addItem(new ItemStack[]{speedPotion});
                  }
            }));
            this.kits.add((new Kit("Magma", 1500, Arrays.asList("Start the game with 1 lava bucket", "and take no fire/lava damage!"))).setItemMaterial(Material.LAVA_BUCKET).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.LAVA_BUCKET)});
                  }
            }));
            this.kits.add((new Kit("Clutch", 1000, Arrays.asList("Start the game with 8 cobwebs and", "1 water bucket"))).setItemMaterial(Material.WEB).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.WEB, 8)});
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.WATER_BUCKET)});
                  }
            }));
            this.kits.add((new Kit("Griefer", 1000, Arrays.asList("Start the game with 8 TNT and", "a flint and steel"))).setItemMaterial(Material.TNT).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.TNT, 8)});
                        p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.FLINT_AND_STEEL)});
                  }
            }));
            this.kits.add((new Kit("AntiFall", 1500, Arrays.asList("Take no fall damage while", "sneaking during the game!"))).setItemMaterial(Material.FEATHER).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.sendMessage(ChatColor.GOLD + "[KIT] " + ChatColor.GRAY + "You take no fall damage while sneaking! (AntiFall Kit)");
                  }
            }));
            this.kits.add((new Kit("Pirate", 2000, Arrays.asList("Start the game with Invisibility", "for 15 seconds"))).setItemMaterial(Material.STICK).setGameStartTask(new Consumer<Player>() {
                  public void accept(Player p) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 15, 0, true));
                        p.sendMessage(ChatColor.GOLD + "[KIT] " + ChatColor.GRAY + "You have the Invisibility effect for 15 seconds! (Pirate Kit)");
                  }
            }));
      }

      public List<Kit> getKits() {
            return this.kits;
      }
}
