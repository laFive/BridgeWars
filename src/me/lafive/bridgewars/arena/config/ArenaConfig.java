package me.lafive.bridgewars.arena.config;

import java.io.File;
import java.io.IOException;
import me.lafive.bridgewars.BridgeWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class ArenaConfig {
      private String arenaName;
      private File dataFile;
      private YamlConfiguration dataYaml;

      public ArenaConfig(String arenaName) {
            this.arenaName = arenaName;
            this.dataFile = new File(BridgeWars.getInstance().getDataFolder() + "/arenas/");
            this.dataFile.mkdirs();
            this.dataFile = new File(BridgeWars.getInstance().getDataFolder() + "/arenas/" + this.arenaName + ".yml");
            if (!this.dataFile.exists()) {
                  try {
                        this.dataFile.createNewFile();
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(this.dataFile);
                        yaml.set("Name", this.arenaName);
                        yaml.set("Max-Players", 5);
                        yaml.set("Map-Author", "LitPix");
                        yaml.set("Void-Level", 10);
                        yaml.set("Spawn-Location.x", 0);
                        yaml.set("Spawn-Location.y", 60);
                        yaml.set("Spawn-Location.z", 0);
                        yaml.set("Spawn-Location.yaw", 90.0F);
                        yaml.set("Spawn-Location.pitch", 0.0F);
                        yaml.set("Spawn-Location.world", ((World)Bukkit.getWorlds().get(0)).getName());
                        yaml.set("Spectator-Location.x", 0);
                        yaml.set("Spectator-Location.y", 60);
                        yaml.set("Spectator-Location.z", 0);
                        yaml.set("Spectator-Location.yaw", 90.0F);
                        yaml.set("Spectator-Location.pitch", 0.0F);
                        yaml.set("Spectator-Location.world", ((World)Bukkit.getWorlds().get(0)).getName());
                        yaml.set("Player-Location.Loc-1.x", 0);
                        yaml.set("Player-Location.Loc-1.y", 60);
                        yaml.set("Player-Location.Loc-1.z", 0);
                        yaml.set("Player-Location.Loc-1.yaw", 90.0F);
                        yaml.set("Player-Location.Loc-1.pitch", 0.0F);
                        yaml.set("Player-Location.Loc-1.world", ((World)Bukkit.getWorlds().get(0)).getName());
                        yaml.save(this.dataFile);
                  } catch (IOException var3) {
                        var3.printStackTrace();
                        System.out.println("[BridgeWars] An error occoured while saving an arena file! (IOException)");
                        return;
                  }
            }

            this.dataYaml = YamlConfiguration.loadConfiguration(this.dataFile);
      }

      public Location getSpawnLocation() {
            return new Location(Bukkit.getWorld(this.dataYaml.getString("Spawn-Location.world")), this.dataYaml.getDouble("Spawn-Location.x"), this.dataYaml.getDouble("Spawn-Location.y"), this.dataYaml.getDouble("Spawn-Location.z"), Double.valueOf(this.dataYaml.getDouble("Spawn-Location.yaw")).floatValue(), Double.valueOf(this.dataYaml.getDouble("Spawn-Location.pitch")).floatValue());
      }

      public int getVoidLevel() {
            return this.dataYaml.getInt("Void-Level");
      }

      public void setVoidLevel(int i) {
            this.dataYaml.set("Void-Level", i);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving an arena file! (IOException)");
            }

      }

      public Location getSpectatorLocation() {
            return new Location(Bukkit.getWorld(this.dataYaml.getString("Spectator-Location.world")), this.dataYaml.getDouble("Spectator-Location.x"), this.dataYaml.getDouble("Spectator-Location.y"), this.dataYaml.getDouble("Spectator-Location.z"), Double.valueOf(this.dataYaml.getDouble("Spectator-Location.yaw")).floatValue(), Double.valueOf(this.dataYaml.getDouble("Spectator-Location.pitch")).floatValue());
      }

      public void setSpectatorLocation(Location loc) {
            this.dataYaml.set("Spectator-Location.x", loc.getX());
            this.dataYaml.set("Spectator-Location.y", loc.getY());
            this.dataYaml.set("Spectator-Location.z", loc.getZ());
            this.dataYaml.set("Spectator-Location.yaw", loc.getYaw());
            this.dataYaml.set("Spectator-Location.pitch", loc.getPitch());
            this.dataYaml.set("Spectator-Location.world", loc.getWorld().getName());

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving an arena file! (IOException)");
            }

      }

      public void setSpawnLocation(Location loc) {
            this.dataYaml.set("Spawn-Location.x", loc.getX());
            this.dataYaml.set("Spawn-Location.y", loc.getY());
            this.dataYaml.set("Spawn-Location.z", loc.getZ());
            this.dataYaml.set("Spawn-Location.yaw", loc.getYaw());
            this.dataYaml.set("Spawn-Location.pitch", loc.getPitch());
            this.dataYaml.set("Spawn-Location.world", loc.getWorld().getName());

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving an arena file! (IOException)");
            }

      }

      public String getMapAuthor() {
            return this.dataYaml.getString("Map-Author");
      }

      public void setMapAuthor(String author) {
            this.dataYaml.set("Map-Author", author);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving an arena file! (IOException)");
            }

      }

      public int getMaxPlayers() {
            return this.dataYaml.getInt("Max-Players");
      }

      public void setMaxPlayers(int maxPlayers) {
            this.dataYaml.set("Max-Players", maxPlayers);

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving an arena file! (IOException)");
            }

      }

      public Location getPlayerLocation(int locationNumber) {
            return new Location(Bukkit.getWorld(this.dataYaml.getString("Player-Location.Loc-" + locationNumber + ".world")), this.dataYaml.getDouble("Player-Location.Loc-" + locationNumber + ".x"), this.dataYaml.getDouble("Player-Location.Loc-" + locationNumber + ".y"), this.dataYaml.getDouble("Player-Location.Loc-" + locationNumber + ".z"), Double.valueOf(this.dataYaml.getDouble("Player-Location.Loc-" + locationNumber + ".yaw")).floatValue(), Double.valueOf(this.dataYaml.getDouble("Player-Location.Loc-" + locationNumber + ".pitch")).floatValue());
      }

      public void setPlayerLocation(int locationNumber, Location loc) {
            this.dataYaml.set("Player-Location.Loc-" + locationNumber + ".x", loc.getX());
            this.dataYaml.set("Player-Location.Loc-" + locationNumber + ".y", loc.getY());
            this.dataYaml.set("Player-Location.Loc-" + locationNumber + ".z", loc.getZ());
            this.dataYaml.set("Player-Location.Loc-" + locationNumber + ".yaw", loc.getYaw());
            this.dataYaml.set("Player-Location.Loc-" + locationNumber + ".pitch", loc.getPitch());
            this.dataYaml.set("Player-Location.Loc-" + locationNumber + ".world", loc.getWorld().getName());

            try {
                  this.dataYaml.save(this.dataFile);
            } catch (IOException var4) {
                  var4.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving an arena file! (IOException)");
            }

      }
}
