package me.lafive.bridgewars.config;

import java.io.File;
import java.io.IOException;
import me.lafive.bridgewars.BridgeWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginConfig {
      private File file;
      private YamlConfiguration yaml;

      public PluginConfig(BridgeWars plugin) {
            plugin.getDataFolder().mkdirs();
            this.file = new File(plugin.getDataFolder() + "/config.yml");
            if (!this.file.exists()) {
                  try {
                        this.file.createNewFile();
                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(this.file);
                        yaml.set("Spawn-Location.x", 0);
                        yaml.set("Spawn-Location.y", 60);
                        yaml.set("Spawn-Location.z", 0);
                        yaml.set("Spawn-Location.yaw", 90.0F);
                        yaml.set("Spawn-Location.pitch", 0.0F);
                        yaml.set("Spawn-Location.world", ((World)Bukkit.getWorlds().get(0)).getName());
                        yaml.save(this.file);
                  } catch (IOException var3) {
                        System.out.println("[BridgeWars] A critical error occoured while loading the config! Plugin shutting down... (IOException)");
                        Bukkit.getPluginManager().disablePlugin(plugin);
                        return;
                  }
            }

            this.yaml = YamlConfiguration.loadConfiguration(this.file);
      }

      public void setSpawnLocation(Location loc) {
            this.yaml.set("Spawn-Location.world", loc.getWorld().getName());
            this.yaml.set("Spawn-Location.x", loc.getX());
            this.yaml.set("Spawn-Location.y", loc.getY());
            this.yaml.set("Spawn-Location.z", loc.getZ());
            this.yaml.set("Spawn-Location.yaw", loc.getYaw());
            this.yaml.set("Spawn-Location.pitch", loc.getPitch());

            try {
                  this.yaml.save(this.file);
            } catch (IOException var3) {
                  var3.printStackTrace();
                  System.out.println("[BridgeWars] An error occoured while saving the config file! (IOException)");
            }

      }

      public Location getSpawnLocation() {
            return new Location(Bukkit.getWorld(this.yaml.getString("Spawn-Location.world")), this.yaml.getDouble("Spawn-Location.x"), this.yaml.getDouble("Spawn-Location.y"), this.yaml.getDouble("Spawn-Location.z"), Double.valueOf(this.yaml.getDouble("Spawn-Location.yaw")).floatValue(), Double.valueOf(this.yaml.getDouble("Spawn-Location.pitch")).floatValue());
      }
}
