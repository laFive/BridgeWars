package me.lafive.bridgewars;

import me.lafive.bridgewars.arena.ArenaManager;
import me.lafive.bridgewars.command.ArenaCommand;
import me.lafive.bridgewars.command.LobbyCommand;
import me.lafive.bridgewars.command.SetBalanceCommand;
import me.lafive.bridgewars.command.SetspawnCommand;
import me.lafive.bridgewars.config.PluginConfig;
import me.lafive.bridgewars.cosmetic.CosmeticManager;
import me.lafive.bridgewars.data.DataManager;
import me.lafive.bridgewars.kit.KitManager;
import me.lafive.bridgewars.listener.ArenaListener;
import me.lafive.bridgewars.listener.MenuListener;
import me.lafive.bridgewars.listener.PlayerListener;
import me.lafive.bridgewars.menu.MenuManager;
import me.lafive.bridgewars.runnable.ArenaTickRunnable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BridgeWars extends JavaPlugin {
      private static BridgeWars instance;
      private PluginConfig pluginConfig;
      private DataManager dataManager;
      private MenuManager menuManager;
      private KitManager kitManager;
      private CosmeticManager cosmeticManager;
      private ArenaManager arenaManager;

      public void onEnable() {
            instance = this;
            this.pluginConfig = new PluginConfig(this);
            this.dataManager = new DataManager(this);
            this.menuManager = new MenuManager();
            this.kitManager = new KitManager();
            this.cosmeticManager = new CosmeticManager();
            this.arenaManager = new ArenaManager();
            this.registerCommands();
            this.registerEvents();
            Bukkit.getScheduler().runTaskTimer(this, new ArenaTickRunnable(), 1L, 1L);
            System.out.println("[BridgeWars] BridgeWars v" + this.getDescription().getVersion() + " has been loaded!");
      }

      public void onDisable() {
            System.out.println("[BridgeWars] BridgeWars has been disabled.");
      }

      public void registerEvents() {
            Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
            Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
            Bukkit.getPluginManager().registerEvents(new ArenaListener(), this);
      }

      public void registerCommands() {
            this.getCommand("lobby").setExecutor(new LobbyCommand());
            this.getCommand("setspawn").setExecutor(new SetspawnCommand());
            this.getCommand("setbalance").setExecutor(new SetBalanceCommand());
            this.getCommand("arena").setExecutor(new ArenaCommand());
      }

      public static BridgeWars getInstance() {
            return instance;
      }

      public ArenaManager getArenaManager() {
            return this.arenaManager;
      }

      public CosmeticManager getCosmeticManager() {
            return this.cosmeticManager;
      }

      public PluginConfig getPluginConfig() {
            return this.pluginConfig;
      }

      public KitManager getKitManager() {
            return this.kitManager;
      }

      public MenuManager getMenuManager() {
            return this.menuManager;
      }

      public DataManager getDataManager() {
            return this.dataManager;
      }
}
