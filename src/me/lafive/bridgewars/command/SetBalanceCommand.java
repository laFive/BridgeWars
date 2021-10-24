package me.lafive.bridgewars.command;

import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.data.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetBalanceCommand implements CommandExecutor {
      public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!sender.hasPermission("bridgewars.admin")) {
                  sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                  return true;
            } else if (args.length < 2) {
                  sender.sendMessage(ChatColor.RED + "Usage: /setbalance <player> <balance>");
                  return true;
            } else {
                  OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                  if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Player not found!");
                        return true;
                  } else {
                        int newBalance;
                        try {
                              newBalance = Integer.parseInt(args[1]);
                        } catch (NumberFormatException var8) {
                              sender.sendMessage(ChatColor.RED + "Invalid balance specified! Balances must be whole numbers");
                              return true;
                        }

                        PlayerData targetData = BridgeWars.getInstance().getDataManager().getPlayerData(target.getUniqueId());
                        targetData.setBalance(newBalance);
                        sender.sendMessage(ChatColor.GOLD + "[!] " + ChatColor.GRAY + "Balance Set!");
                        return true;
                  }
            }
      }
}
