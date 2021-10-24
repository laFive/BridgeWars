package me.lafive.bridgewars.runnable;

import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.data.PlayerData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyRunnable extends BukkitRunnable {
      private Player player;
      private PlayerData data;

      public LobbyRunnable(Player player) {
            this.player = player;
            this.data = BridgeWars.getInstance().getDataManager().getPlayerData(player.getUniqueId());
      }

      public void run() {
            if (this.player.isOnline() && !this.data.isInGame() && this.player.getScoreboard().getTeam("BalanceTeam") != null) {
                  this.player.getScoreboard().getTeam("BalanceTeam").setPrefix(ChatColor.GRAY.toString() + "Balance: ");
                  this.player.getScoreboard().getTeam("BalanceTeam").setSuffix(ChatColor.YELLOW.toString() + this.data.getBalance());
                  if (this.player.getWorld().equals(BridgeWars.getInstance().getPluginConfig().getSpawnLocation().getWorld())) {
                        if (this.player.getLocation().getY() < 92.0D && this.player.getGameMode() != GameMode.CREATIVE) {
                              this.player.teleport(BridgeWars.getInstance().getPluginConfig().getSpawnLocation());
                        }

                        this.player.setFireTicks(0);
                  }

            } else {
                  this.cancel();
            }
      }
}
