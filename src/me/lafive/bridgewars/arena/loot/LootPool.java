package me.lafive.bridgewars.arena.loot;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootPool {

    private Random random;
    private List<ItemStack> lootItems;

    public LootPool() {
        random = new Random();
        lootItems = new ArrayList<ItemStack>();
    }

    public void addItem(ItemStack item) {
        lootItems.add(item);
    }

    public void addItem(ItemStack item, int weight) {
        for (int i = 0; i < weight; i++) {
            lootItems.add(item);
        }
    }

    public ItemStack getRandomItem() {
        if (lootItems.size() == 0) {
            return null;
        }
        return lootItems.get(random.nextInt(lootItems.size()));
    }

}
