package me.lafive.bridgewars.util;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
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
