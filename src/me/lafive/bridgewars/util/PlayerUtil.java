package me.lafive.bridgewars.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerUtil {

    public static void launchFirework(Player target, Color color) {

        Firework fw = target.getLocation().getWorld().spawn(target.getLocation(), Firework.class);
        FireworkMeta fwMeta = fw.getFireworkMeta();
        fwMeta.setPower(0);
        fwMeta.addEffects(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(color).build());
        fw.setFireworkMeta(fwMeta);

    }

    public static void makeSpectator(Player player, Location spectatorLocation) {

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[] {});
        player.teleport(spectatorLocation);
        player.playSound(player.getLocation(), Sound.WITHER_HURT, 10, 1);
        player.setHealth(20);
        player.setMaxHealth(20);
        player.getActivePotionEffects().forEach(pe -> player.removePotionEffect(pe.getType()));
        PacketUtil.forceFly(player);
        PacketUtil.sendTitle(player, ChatColor.RED.toString() + ChatColor.BOLD + "You Died!", ChatColor.GRAY + "Better luck next time!", 0, 60, 0);

        ItemStack leaveItem = new ItemStack(Material.BED);
        ItemMeta leaveMeta = leaveItem.getItemMeta();
        leaveMeta.setDisplayName(ChatColor.RED + "Quit Game " + ChatColor.GRAY + "(Right Click)");
        leaveItem.setItemMeta(leaveMeta);
        player.getInventory().setItem(8, leaveItem);

    }

    public static void dropPlayerItems(Player player) {

        for (ItemStack is : player.getInventory().getContents()) {
            if (is != null && is.getType() != Material.AIR) {
                player.getWorld().dropItem(player.getLocation(), is);
            }
        }
        for (ItemStack is : player.getInventory().getArmorContents()) {
            if (is != null && is.getType() != Material.AIR) {
                player.getWorld().dropItem(player.getLocation(), is);
            }
        }

    }

}
