package me.lafive.bridgewars.util;

import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftBat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PacketUtil {

      public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int stayTicks, int fadeOut) {
            PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, new ChatComponentText(subTitle));
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, new ChatComponentText(title), fadeIn, stayTicks, fadeOut);
            PacketPlayOutTitle timePacket = new PacketPlayOutTitle(fadeIn, stayTicks, fadeOut);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(subTitlePacket);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(titlePacket);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(timePacket);
      }

      public static void sendActionBar(Player player, String actionBar) {
            ChatComponentText component = new ChatComponentText(actionBar);
            PacketPlayOutChat actionBarPacket = new PacketPlayOutChat(component, (byte) 2);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(actionBarPacket);
      }

      public static void forceFly(Player player) {
            PacketPlayOutAbilities abilitiesPacket = new PacketPlayOutAbilities();
            abilitiesPacket.a(false);
            abilitiesPacket.b(true);
            abilitiesPacket.c(true);
            abilitiesPacket.d(false);
            abilitiesPacket.a(0.05F);
            abilitiesPacket.b(0.1F);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(abilitiesPacket);
      }

      public static void vanishPlayer(Player player, Iterable<Player> targets) {

            PacketPlayOutEntityDestroy entityDestroy = new PacketPlayOutEntityDestroy(player.getEntityId());
            for (Player t : targets) {

                  // Stops funky shit happening
                  if (t == player) continue;
                  ((CraftPlayer)t).getHandle().playerConnection.sendPacket(entityDestroy);

            }

      }

      public static void sitPlayer(Player player) {
            Bat clientEntity = (Bat)player.getWorld().spawnEntity(player.getLocation().clone().subtract(0.0D, 0.5D, 0.0D), EntityType.BAT);
            ((CraftBat)clientEntity).getHandle().getDataWatcher().watch(15, (byte)1);
            clientEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 1, true, false));
            PacketPlayOutAttachEntity attachPacket = new PacketPlayOutAttachEntity(0, ((CraftPlayer)player).getHandle(), ((CraftBat)clientEntity).getHandle());
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(attachPacket);
      }

      public static void unSitPlayer(Player player) {
            PacketPlayOutAttachEntity attachPacket = new PacketPlayOutAttachEntity(0, ((CraftPlayer)player).getHandle(), (Entity)null);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(attachPacket);
      }
}
