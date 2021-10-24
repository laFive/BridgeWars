package me.lafive.bridgewars.runnable;

import me.lafive.bridgewars.BridgeWars;

public class ArenaTickRunnable implements Runnable {
      public void run() {
            BridgeWars.getInstance().getArenaManager().getArenas().forEach((a) -> {
                  a.tickArena();
            });
      }
}
