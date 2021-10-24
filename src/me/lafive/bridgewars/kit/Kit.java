package me.lafive.bridgewars.kit;

import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Kit {
      private String name;
      private int cost;
      private boolean free;
      private Material itemMaterial;
      private List description;
      private Consumer gameStartTask;

      public Kit(String name, int cost, List description) {
            this.name = name;
            this.cost = cost;
            this.description = description;
      }

      public String getName() {
            return this.name;
      }

      public void setName(String name) {
            this.name = name;
      }

      public int getCost() {
            return this.cost;
      }

      public void setCost(int cost) {
            this.cost = cost;
      }

      public boolean isFree() {
            return this.free;
      }

      public Kit setFree(boolean free) {
            this.free = free;
            return this;
      }

      public Consumer getGameStartTask() {
            return this.gameStartTask;
      }

      public Kit setGameStartTask(Consumer<Player> gameStartTask) {
            this.gameStartTask = gameStartTask;
            return this;
      }

      public List getDescription() {
            return this.description;
      }

      public void setDescription(List description) {
            this.description = description;
      }

      public Material getItemMaterial() {
            return this.itemMaterial;
      }

      public Kit setItemMaterial(Material itemMaterial) {
            this.itemMaterial = itemMaterial;
            return this;
      }
}
