package me.lafive.bridgewars.data;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import me.lafive.bridgewars.BridgeWars;
import org.bukkit.entity.Player;

public class DataManager {
      private HashMap playerData = new HashMap();

      public DataManager(BridgeWars plugin) {
            File playerDataDir = new File(plugin.getDataFolder() + "/playerdata/");
            playerDataDir.mkdirs();
      }

      public void createPlayerData(Player p) {
            this.playerData.put(p.getUniqueId(), new PlayerData(p.getUniqueId()));
      }

      public PlayerData getPlayerData(UUID p) {
            if (!this.playerData.containsKey(p)) {
                  this.playerData.put(p, new PlayerData(p));
            }

            return (PlayerData)this.playerData.get(p);
      }
}
