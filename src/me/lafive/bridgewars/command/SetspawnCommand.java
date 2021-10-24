package me.lafive.bridgewars.command;

import me.lafive.bridgewars.BridgeWars;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetspawnCommand implements CommandExecutor {
      public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!sender.hasPermission("bridgewars.setspawn")) {
                  sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                  return true;
            } else if (!(sender instanceof Player)) {
                  return true;
            } else {
                  Player player = (Player)sender;
                  BridgeWars.getInstance().getPluginConfig().setSpawnLocation(player.getLocation());
                  sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GOLD.toString() + ChatColor.BOLD + "BridgeWars" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + "Spawn location set!");
                  return true;
            }
      }
}
