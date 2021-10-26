package me.lafive.bridgewars.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import me.lafive.bridgewars.BridgeWars;

public class ArenaManager {
      private Arena lastArena;
      private Random random = new Random();
      private List<Arena> arenas = new ArrayList();

      public ArenaManager() {
            File arenaDir = new File(BridgeWars.getInstance().getDataFolder() + "/arenas/");
            arenaDir.mkdirs();
            String[] var5;
            int var4 = (var5 = arenaDir.list()).length;

            for(int var3 = 0; var3 < var4; ++var3) {
                  String arenaName = var5[var3];
                  if (arenaName.endsWith(".yml")) {
                        String an = arenaName.replace(".yml", "");
                        Arena a = new Arena(an);
                        a.pasteSchematic();
                        a.getChestManager().fillChestsAsync();
                        this.arenas.add(a);
                  }
            }

      }

      public List<Arena> getArenas() {
            return this.arenas;
      }

      public Arena getArena(String name) {
            Iterator var3 = this.arenas.iterator();

            while(var3.hasNext()) {
                  Arena arena = (Arena)var3.next();
                  if (arena.getMapName().equalsIgnoreCase(name)) {
                        return arena;
                  }
            }

            return null;
      }

      public void createArena(String name) {
            this.arenas.add(new Arena(name));
      }

      public Arena getArena() {
            ArrayList availibleArenas;
            Arena a;
            Iterator var3;
            if (this.lastArena == null) {
                  availibleArenas = new ArrayList();
                  var3 = this.arenas.iterator();

                  while(var3.hasNext()) {
                        a = (Arena)var3.next();
                        if (!a.isStarted() && a.hasPlayerLocations() && a.getAlivePlayerCount() < a.getMaxPlayers()) {
                              availibleArenas.add(a);
                        }
                  }

                  if (availibleArenas.size() == 0) {
                        return null;
                  }

                  this.lastArena = (Arena)availibleArenas.get(this.random.nextInt(availibleArenas.size()));
            }

            if (!this.lastArena.isStarted() && this.lastArena.getAlivePlayerCount() < this.lastArena.getMaxPlayers()) {
                  return this.lastArena;
            } else {
                  availibleArenas = new ArrayList();
                  var3 = this.arenas.iterator();

                  while(var3.hasNext()) {
                        a = (Arena)var3.next();
                        if (!a.isStarted() && a.hasPlayerLocations() && a.getAlivePlayerCount() < a.getMaxPlayers()) {
                              availibleArenas.add(a);
                        }
                  }

                  if (availibleArenas.size() == 0) {
                        return null;
                  } else {
                        this.lastArena = (Arena)availibleArenas.get(this.random.nextInt(availibleArenas.size()));
                        return this.lastArena;
                  }
            }
      }
}
