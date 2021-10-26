package me.lafive.bridgewars.command;

import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.data.PlayerData;
import me.lafive.bridgewars.runnable.LobbyRunnable;
import me.lafive.bridgewars.util.SidebarUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyCommand implements CommandExecutor {
      public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)) {
                  return true;
            } else {
                  Player player = (Player)sender;
                  PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
                  if (!data.isInGame()) {
                        player.teleport(BridgeWars.getInstance().getPluginConfig().getSpawnLocation());
                        player.getInventory().clear();
                        player.getInventory().setArmorContents(new ItemStack[0]);
                        player.getActivePotionEffects().forEach((pe) -> {
                              player.removePotionEffect(pe.getType());
                        });
                        player.setAllowFlight(false);
                        player.setGameMode(GameMode.SURVIVAL);
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 10.0F, 0);
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
                        return true;
                  } else {
                        BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                              a.handleGameLeave(player, true);
                        });
                        return true;
                  }
            }
      }
}
