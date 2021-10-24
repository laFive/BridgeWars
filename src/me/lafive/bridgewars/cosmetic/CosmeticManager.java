package me.lafive.bridgewars.cosmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CosmeticManager {
      private List cosmeticList = new ArrayList();

      public CosmeticManager() {
            this.cosmeticList.add(new Cosmetic("No Kill Effect", 0, CosmeticType.KILL_EFFECT, Material.BARRIER, Arrays.asList("Have no effect upon kill (default)")));
            this.cosmeticList.add((new Cosmetic("Flame Kill Effect", 1000, CosmeticType.KILL_EFFECT, Material.BLAZE_POWDER, Arrays.asList("Spawn flame particles and", "a fire sound effect when", "you get a kill!"))).setWhenRun(new Consumer<Player>() {
                  public void accept(Player p) {
                  }
            }));
            this.cosmeticList.add((new Cosmetic("Water Kill Effect", 1000, CosmeticType.KILL_EFFECT, Material.WATER_BUCKET, Arrays.asList("Spawn water particles and", "a splashing sound effect when", "you get a kill!"))).setWhenRun(new Consumer<Player>() {
                  public void accept(Player p) {
                  }
            }));
            this.cosmeticList.add((new Cosmetic("Explosion Kill Effect", 1500, CosmeticType.KILL_EFFECT, Material.TNT, Arrays.asList("Spawn an explosion and a", "boom sound when you", "you get a kill!"))).setWhenRun(new Consumer<Player>() {
                  public void accept(Player p) {
                  }
            }));
      }

      public List getCosmetics() {
            return this.cosmeticList;
      }
}
