package warvale.core.plugin.spec;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.potion.PotionEffect;

import net.md_5.bungee.api.ChatColor;
import warvale.core.plugin.Main;

public class Preferences implements Listener {
	
    public Preferences(Main plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private static Inventory inv;
    
    @EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
	    if (Main.getSpectatorTeam().getEntries().contains(event.getPlayer().getName())) {
	        ItemStack preferences = new ItemStack(Material.REDSTONE_COMPARATOR, 1); {
	        ItemMeta spawnmeta = preferences.getItemMeta();
	        spawnmeta.setDisplayName(ChatColor.RESET + "" + ChatColor.AQUA + "Menu & Preferences");
	        preferences.setItemMeta(spawnmeta);
	        
	        event.getPlayer().getInventory().setItem(6, preferences);
	        }
	    }
	}
	
	private void tsGUI(Player player ) {
		inv = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY + "Preferences Menu:");
		
		ItemStack forumslink = new ItemStack(Material.NAME_TAG);
		ItemMeta forumslinkmeta = forumslink.getItemMeta();
		forumslinkmeta.setDisplayName(ChatColor.AQUA + "Forums Link");
		forumslink.setItemMeta(forumslinkmeta);
		
		ItemStack discordlink = new ItemStack(Material.ANVIL);
		ItemMeta discordlinkmeta = discordlink.getItemMeta();
		discordlinkmeta.setDisplayName(ChatColor.AQUA + "Discord Link");
		discordlink.setItemMeta(discordlinkmeta);
		
		ItemStack twitterlink = new ItemStack(Material.DIAMOND);
		ItemMeta twitterlinkmeta = twitterlink.getItemMeta();
		twitterlinkmeta.setDisplayName(ChatColor.AQUA + "Twitter Link");
		twitterlink.setItemMeta(twitterlinkmeta);
		
		ItemStack storelink = new ItemStack(Material.NETHER_STAR);
		ItemMeta storelinkmeta = storelink.getItemMeta();
		storelinkmeta.setDisplayName(ChatColor.AQUA + "Store Link");
		storelink.setItemMeta(storelinkmeta);
		
		ItemStack closemenu = new ItemStack(Material.BARRIER);
		ItemMeta closemenumeta = closemenu.getItemMeta();
		closemenumeta.setDisplayName(ChatColor.AQUA + "Close selector");
		closemenu.setItemMeta(closemenumeta);
		
		inv.setItem(49, forumslink);
		inv.setItem(50, discordlink);
		inv.setItem(51, twitterlink);
		inv.setItem(52, storelink);
		inv.setItem(53, closemenu);
		
		player.openInventory(inv);
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action a = event.getAction();
		ItemStack is = event.getItem();
			
		if(a == Action.PHYSICAL || is == null || is.getType()==Material.AIR)
				return;
			
		if(is.getType()==Material.REDSTONE_COMPARATOR)
				tsGUI(event.getPlayer());
		
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST) 
	public void onMove(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();

	    if (!event.getInventory().equals(inv)) {
	    	return;
		}
	    
	    switch (event.getSlot()) {
	    case 49: 
	    	System.out.println("worked.");
	    	event.setCancelled(true);
	    	event.getWhoClicked().sendMessage(ChatColor.GRAY + "Warvale Forums: " + ChatColor.AQUA + "www.warvale.com/forums");
			player.closeInventory();
	    	break;
	    case 50: 
	    	event.setCancelled(true);
	    	event.getWhoClicked().sendMessage(ChatColor.GRAY + "Warvale Discord: " + ChatColor.AQUA + "discord.gg/addlater");
	    	player.closeInventory();
	    	break;
	    case 51: 
	    	event.setCancelled(true);
	    	event.getWhoClicked().sendMessage(ChatColor.GRAY + "Warvale Twitter: " + ChatColor.AQUA + "www.twitter.com/Pixelificz");
	    	player.closeInventory();
	    	break;
	    case 52: 
	    	event.setCancelled(true);
	    	event.getWhoClicked().sendMessage(ChatColor.GRAY + "Warvale Store: " + ChatColor.AQUA + "www.warvale.com/store");
	    	player.closeInventory();
	    	break;
	    case 53: 
	    	event.setCancelled(true);
	    	player.closeInventory();
	    	break;
	    default:
	    	if (Main.getSpectatorTeam().getEntries().contains(event.getWhoClicked().getName())) {
	    		event.setCancelled(true);
	        	break;
	        }
	    }
	    }
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (Main.getSpectatorTeam().getEntries().contains(event.getPlayer().getName())) {
			if (event.getItemDrop().getItemStack().getType() == Material.REDSTONE_COMPARATOR ||event.getItemDrop().getItemStack().getType() == Material.NETHER_STAR || event.getItemDrop().getItemStack().getType() == Material.BLAZE_POWDER ) {
				event.setCancelled(true);
	}
	}
	}
	@EventHandler
	public void onPlayerOffHand(PlayerSwapHandItemsEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlaceAttempt(BlockPlaceEvent event) {
		if (Main.getSpectatorTeam().getEntries().contains(event.getPlayer().getName())) {
            event.setCancelled(true);
        }else {
            event.setCancelled(false);
        }

    }
	
	}
