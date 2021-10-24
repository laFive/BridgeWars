package me.lafive.bridgewars.util;

import me.lafive.bridgewars.BridgeWars;
import me.lafive.bridgewars.arena.Arena;
import me.lafive.bridgewars.data.PlayerData;
import me.lafive.core.Core;
import me.lafive.core.profile.PlayerProfile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SidebarUtil {
      public static void drawLobbySidebar(Player p) {
            PlayerData data = BridgeWars.getInstance().getDataManager().getPlayerData(p.getUniqueId());
            PlayerProfile profile = Core.INSTANCE.getMongoManager().getProfileManager().getProfile(p.getUniqueId());
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = sb.registerNewObjective("LobbySidebar", "N/A");
            obj.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "BridgeWars");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.getScore(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------").setScore(10);
            Team balanceTeam = sb.registerNewTeam("BalanceTeam");
            balanceTeam.addEntry(ChatColor.BLUE.toString() + ChatColor.GRAY.toString());
            balanceTeam.setPrefix(ChatColor.GRAY.toString() + "Balance: ");
            balanceTeam.setSuffix(ChatColor.YELLOW.toString() + data.getBalance());
            obj.getScore(ChatColor.BLUE.toString() + ChatColor.GRAY.toString()).setScore(9);
            obj.getScore(ChatColor.BLUE.toString() + ChatColor.WHITE).setScore(8);
            obj.getScore(ChatColor.GRAY + "Rank: " + ChatColor.YELLOW + profile.getRank().getName()).setScore(7);
            obj.getScore(ChatColor.YELLOW.toString() + ChatColor.GREEN).setScore(6);
            obj.getScore(ChatColor.GRAY + "Kills: " + ChatColor.YELLOW + data.getKills()).setScore(5);
            obj.getScore(ChatColor.WHITE.toString() + ChatColor.DARK_PURPLE).setScore(4);
            obj.getScore(ChatColor.GRAY + "Wins: " + ChatColor.YELLOW + data.getWins()).setScore(3);
            obj.getScore(ChatColor.BLUE.toString()).setScore(2);
            obj.getScore(ChatColor.GOLD + "play.litpix.org").setScore(1);
            obj.getScore(ChatColor.BLACK + ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------").setScore(0);
            p.setScoreboard(sb);
      }

      public static void drawPreGameSidebar(Player p, Arena a) {
            Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = sb.registerNewObjective("PregameSidebar", "N/A");
            obj.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "BridgeWars");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.getScore(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------").setScore(8);
            Team startingIn = sb.registerNewTeam("StartingIn");
            startingIn.addEntry(ChatColor.GOLD.toString() + ChatColor.GREEN.toString());
            startingIn.setPrefix(ChatColor.GRAY + "Starting In: ");
            startingIn.setSuffix(ChatColor.YELLOW + a.getCountdown());
            obj.getScore(ChatColor.GOLD.toString() + ChatColor.GREEN.toString()).setScore(7);
            obj.getScore(ChatColor.GREEN.toString()).setScore(6);
            Team players = sb.registerNewTeam("PreGamePlayers");
            players.addEntry(ChatColor.RED.toString() + ChatColor.BLACK.toString());
            players.setPrefix(ChatColor.GRAY + "Players: ");
            players.setSuffix(ChatColor.YELLOW.toString() + a.getAlivePlayerCount());
            obj.getScore(ChatColor.RED.toString() + ChatColor.BLACK.toString()).setScore(5);
            obj.getScore(ChatColor.DARK_PURPLE.toString()).setScore(4);
            obj.getScore(ChatColor.GRAY + "Map: " + ChatColor.YELLOW + a.getMapName()).setScore(3);
            obj.getScore(ChatColor.WHITE.toString()).setScore(2);
            obj.getScore(ChatColor.GOLD + "play.litpix.org").setScore(1);
            obj.getScore(ChatColor.GREEN + ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------").setScore(0);
            p.setScoreboard(sb);
      }
}
