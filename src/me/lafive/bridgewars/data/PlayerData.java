package me.lafive.bridgewars.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.runnable.LobbyRunnable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {
      private Player player;
      private File dataFile;
      private YamlConfiguration dataYaml;
      private LobbyRunnable lobbyRunnable;
      private boolean inGame;
      private long lastAttacked;
      private Player lastAttacker;

      public PlayerData(UUID player) {
            this.player = Bukkit.getPlayer(player);
            this.dataFile = new File(BridgeWars.getInstance().getDataFolder() + "/playerdata/" + player + ".yml");
            if (!this.dataFile.exists()) {
                  try {
                        this.dataFile.createNewFile();
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(this.dataFile);
                        yaml.set("Kills", 0);
                        yaml.set("Wins", 0);
                        yaml.set("Deaths", 0);
                        yaml.set("Balance", 0);
                        yaml.set("Games", 0);
                        yaml.set("Owned-Kits", new ArrayList());
                        yaml.set("Owned-Cosmetics", new ArrayList());
                        yaml.set("LastUsedKit", "Builder");
                        yaml.set("LastUsedKillEffect", "No");
                        yaml.set("LastUsedDeathMessages", "Default");
                        yaml.save(this.dataFile);
                  } catch (IOException var3) {
                        var3.printStackTrace();
                        if (this.player != null && this.player.isOnline()) {
                              this.player.kickPlayer(ChatColor.RED + "Failed to load your BridgeWars profile! Please contact a Staff Member");
                              return;
                        }
                  }
            }

            this.dataYaml = YamlConfiguration.loadConfiguration(this.dataFile);
      }

      public long getLastAttacked() {
            return lastAttacked;
      }

      public Player getLastAttacker() {
            return lastAttacker;
      }

      public Player getPlayer() {
            return this.player;
      }

      public int getKills() {
            return this.dataYaml.getInt("Kills");
      }

      public void setKills(int kills) {
            this.dataYaml.set("Kills", kills);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving a playerdata file! (IOException)");
            }

      }

      public int getGames() {
            return this.dataYaml.getInt("Games");
      }

      public void setGames(int games) {
            this.dataYaml.set("Games", games);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving a playerdata file! (IOException)");
            }

      }

      public int getWins() {
            return this.dataYaml.getInt("Wins");
      }

      public void setWins(int wins) {
            this.dataYaml.set("Wins", wins);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving a playerdata file! (IOException)");
            }

      }

      public int getBalance() {
            return this.dataYaml.getInt("Balance");
      }

      public void setBalance(int balance) {
            this.dataYaml.set("Balance", balance);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving a playerdata file! (IOException)");
            }

      }

      public int getDeaths() {
            return this.dataYaml.getInt("Deaths");
      }

      public void setDeaths(int deaths) {
            this.dataYaml.set("Deaths", deaths);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving a playerdata file! (IOException)");
            }

      }

      public String getLastKit() {
            return this.dataYaml.getString("LastUsedKit");
      }

      public void setLastKit(String kit) {
            this.dataYaml.set("LastUsedKit", kit);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving a playerdata file! (IOException)");
            }

      }

      public boolean isKitOwned(String kit) {
            List ownedKits = this.dataYaml.getStringList("Owned-Kits");
            return ownedKits.contains(kit);
      }

      public boolean isCosmeticOwned(String cosmetic) {
            List ownedCosmetics = this.dataYaml.getStringList("Owned-Cosmetics");
            return ownedCosmetics.contains(cosmetic);
      }

      public void addCosmetic(String cosmetic) {
            List ownedCosmetics = this.dataYaml.getStringList("Owned-Cosmetics");
            ownedCosmetics.add(cosmetic);
            this.dataYaml.set("Owned-Cosmetics", ownedCosmetics);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var4) {
                  var4.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving a playerdata file! (IOException)");
            }

      }

      public void addKit(String kit) {
            List ownedKits = this.dataYaml.getStringList("Owned-Kits");
            ownedKits.add(kit);
            this.dataYaml.set("Owned-Kits", ownedKits);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var4) {
                  var4.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving a playerdata file! (IOException)");
            }

      }

      public boolean isInGame() {
            return this.inGame;
      }

      public void setInGame(boolean inGame) {
            this.inGame = inGame;
      }

      public LobbyRunnable getLobbyRunnable() {
            return this.lobbyRunnable;
      }

      public void setLobbyRunnable(LobbyRunnable lobbyRunnable) {
            this.lobbyRunnable = lobbyRunnable;
      }
}
