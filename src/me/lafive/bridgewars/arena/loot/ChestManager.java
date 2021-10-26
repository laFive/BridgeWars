package me.lafive.bridgewars.arena.loot;

import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestManager {

    private Arena arena;
    private Random random;
    private LootPool lootPool1;
    private LootPool lootPool2;
    private LootPool lootPool3;

    public ChestManager(Arena arena) {

        this.arena = arena;
        this.random = new Random();
        lootPool1 = new LootPool();
        lootPool2 = new LootPool();
        lootPool3 = new LootPool();

        lootPool1.addItem(new ItemStack(Material.WOOD_SWORD), 3);
        lootPool1.addItem(new ItemStack(Material.STONE_SWORD), 3);
        lootPool1.addItem(new ItemStack(Material.GOLD_SWORD), 3);
        lootPool1.addItem(new ItemStack(Material.IRON_SWORD), 2);
        lootPool1.addItem(new ItemStack(Material.ENDER_PEARL));
        lootPool1.addItem(new ItemStack(Material.DIAMOND_AXE));
        lootPool1.addItem(new ItemStack(Material.GOLDEN_APPLE, 3));

        lootPool2.addItem(new ItemStack(Material.GOLD_LEGGINGS), 4);
        lootPool2.addItem(new ItemStack(Material.GOLD_BOOTS), 4);
        lootPool2.addItem(new ItemStack(Material.GOLD_HELMET), 3);
        lootPool2.addItem(new ItemStack(Material.IRON_LEGGINGS), 2);
        lootPool2.addItem(new ItemStack(Material.LEATHER_BOOTS), 4);
        lootPool2.addItem(new ItemStack(Material.LEATHER_CHESTPLATE), 3);
        lootPool2.addItem(new ItemStack(Material.DIAMOND_BOOTS), 1);
        lootPool2.addItem(new ItemStack(Material.DIAMOND_HELMET), 1);
        lootPool2.addItem(new ItemStack(Material.GOLD_CHESTPLATE), 6);
        lootPool2.addItem(new ItemStack(Material.IRON_CHESTPLATE), 3);
        lootPool2.addItem(new ItemStack(Material.GOLD_LEGGINGS), 4);
        lootPool2.addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
        lootPool2.addItem(new ItemStack(Material.GOLDEN_APPLE));
        lootPool2.addItem(new ItemStack(Material.ENDER_PEARL));
        lootPool2.addItem(new ItemStack(Material.BOW, 3));
        lootPool2.addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
        lootPool2.addItem(new ItemStack(Material.LEATHER_LEGGINGS), 4);
        lootPool2.addItem(new ItemStack(Material.IRON_BOOTS), 2);
        lootPool2.addItem(new ItemStack(Material.LEATHER_HELMET), 4);
        lootPool2.addItem(new ItemStack(Material.IRON_HELMET), 1);

        lootPool3.addItem(new ItemStack(Material.SANDSTONE, 8), 20);
        lootPool3.addItem(new ItemStack(Material.WOOD, 8), 20);
        lootPool3.addItem(new ItemStack(Material.EGG, 8), 13);
        lootPool3.addItem(new ItemStack(Material.FLINT_AND_STEEL), 4);
        lootPool3.addItem(new ItemStack(Material.ARROW, 4), 6);
        lootPool3.addItem(new ItemStack(Material.GOLDEN_APPLE), 3);
        lootPool3.addItem(new ItemStack(Material.BOW), 2);
        lootPool3.addItem(new ItemStack(Material.WATER_BUCKET), 6);
        lootPool3.addItem(new ItemStack(Material.DIAMOND_PICKAXE), 5);
        lootPool3.addItem(new ItemStack(Material.MUSHROOM_SOUP), 6);

    }

    public void fillChestsAsync() {

        Bukkit.getScheduler().runTaskAsynchronously(BridgeWars.getInstance(), new Runnable() {
            @Override
            public void run() {

                Location baseLocation = arena.getArenaConfig().getSpectatorLocation();
                baseLocation.setY(arena.getArenaConfig().getVoidLevel());

                for (double x = -150; x <= 150; x++) {
                    for (double y = 0; y <= 50; y++) {
                        for (double z = -150; z <= 150; z++) {

                            Location targetLocation = baseLocation.clone().add(x, y, z);

                                Bukkit.getScheduler().runTask(BridgeWars.getInstance(), new Runnable() {
                                    @Override
                                    public void run() {

                                        if (targetLocation.getBlock().getType() == Material.CHEST) {

                                            Chest chest = (Chest) targetLocation.getBlock().getState();
                                            chest.getBlockInventory().clear();
                                            List<Integer> chestSlots = new ArrayList<Integer>();

                                            for (int i = 0; i < 26; i++) {
                                                chestSlots.add(i);
                                            }

                                            Integer slot1 = chestSlots.get(random.nextInt(chestSlots.size()));
                                            chestSlots.remove(slot1);
                                            chest.getBlockInventory().setItem(slot1, lootPool1.getRandomItem());

                                            for (int i = 0; i < 3; i++) {
                                                Integer slot2 = chestSlots.get(random.nextInt(chestSlots.size()));
                                                chestSlots.remove(slot2);
                                                chest.getBlockInventory().setItem(slot2, lootPool2.getRandomItem());
                                            }

                                            for (int i = 0; i < 2; i++) {
                                                Integer slot3 = chestSlots.get(random.nextInt(chestSlots.size()));
                                                chestSlots.remove(slot3);
                                                chest.getBlockInventory().setItem(slot3, lootPool3.getRandomItem());
                                            }

                                        }

                                    }
                                });


                        }
                    }
                }

            }
        });

    }


}
