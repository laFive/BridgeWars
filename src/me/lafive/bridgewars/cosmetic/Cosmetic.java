package me.lafive.bridgewars.cosmetic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Material;

public class Cosmetic {
      private String name;
      private int cost;
      private Material item;
      private CosmeticType type;
      private List description = new ArrayList();
      private Consumer whenRun;

      public Cosmetic(String name, int cost, CosmeticType type, Material item, List description) {
            this.name = name;
            this.cost = cost;
            this.item = item;
            this.description = description;
            this.type = type;
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

      public Material getItem() {
            return this.item;
      }

      public void setItem(Material item) {
            this.item = item;
      }

      public CosmeticType getType() {
            return this.type;
      }

      public void setType(CosmeticType type) {
            this.type = type;
      }

      public List getDescription() {
            return this.description;
      }

      public void setDescription(List description) {
            this.description = description;
      }

      public Consumer getWhenRun() {
            return this.whenRun;
      }

      public Cosmetic setWhenRun(Consumer whenRun) {
            this.whenRun = whenRun;
            return this;
      }
}
