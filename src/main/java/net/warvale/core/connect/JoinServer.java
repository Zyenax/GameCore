package net.warvale.core.connect;

import net.warvale.core.game.Game;
import net.warvale.core.game.State;
import net.warvale.core.game.scoreboards.LobbyScoreboard;
import net.warvale.core.message.MessageManager;
import net.warvale.core.message.PrefixType;
import net.warvale.core.spec.Preferences;
import net.warvale.staffcore.bossbar.BarManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.md_5.bungee.api.ChatColor;
import net.warvale.core.Main;
import org.bukkit.scheduler.BukkitRunnable;


public class JoinServer implements Listener {


    public JoinServer(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
    	event.setJoinMessage("");
        String playerName = event.getPlayer().getName();
        event.getPlayer().getInventory().clear();
        event.getPlayer().sendMessage(ChatColor.GRAY + "Welcome back to " + ChatColor.DARK_RED + "Warvale!");
        Main.getTeams().getSpectatorTeam().addEntry(event.getPlayer().getName());
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 1));
        for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()){
			if(!Preferences.noJoinMessages.contains(onlinePlayer.getName()) && onlinePlayer.getName() != event.getPlayer().getName()){
				onlinePlayer.sendMessage(ChatColor.GRAY + playerName + ChatColor.GRAY + " joined.");
			}
		}
        

        if (Main.getTeams().getSpectatorTeam().getEntries().contains(event.getPlayer().getName())) {
            event.getPlayer().setAllowFlight(true);
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        }

        LobbyScoreboard.getInstance().addScoreboard(event.getPlayer());
        LobbyScoreboard.getInstance().newScoreboard(event.getPlayer());

        BarManager.broadcast(BarColor.GREEN, ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "[+] " + ChatColor.RESET + playerName);
        BarManager.broadcastSound(Sound.BLOCK_NOTE_BASS);

        int minPlayers = Game.getInstance().getMinPlayers() - Bukkit.getOnlinePlayers().size();

        MessageManager.broadcast(PrefixType.MAIN, ChatColor.RED +
                String.valueOf(minPlayers) + ChatColor.DARK_GREEN +
                " more players needed to start the game!");

        if (Game.getInstance().isState(State.LOBBY)) {
            event.getPlayer().teleport(new Location(Bukkit.getWorld("lobby"),
                    0, 50, 0));
        }

    }
}
