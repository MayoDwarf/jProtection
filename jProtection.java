package jProtection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class jProtection extends JavaPlugin implements Listener {
	private FileConfiguration config;
	ArrayList<String> pvp = new ArrayList<String>();
	
	ArrayList<String> One = new ArrayList<String>();

	ArrayList<String> Once = new ArrayList<String>();
	
	List<String> tpEvt = new ArrayList<String>();
	
	List<String> msg = new ArrayList<String>();
	
	
public void onEnable() {
	this.getServer().getPluginManager().registerEvents(this, this);
	   config = this.getConfig();
	    config.options().copyDefaults(true);
	    if(config.getBoolean("ResetConfig", true)) {
	    config.set("ResetConfig", false);
	    this.saveConfig();
	    } else if(config.getBoolean("ResetConfig", false)) {  
	    	this.saveConfig();
	    	
	    }
	    config.set("ResetConfig", false);
	    config.set("Test", true);
	    config.set("Donate", "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=Axeexification%40gmail%2ecom&lc=US&item_name=Donate&no_note=0&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHostedGuest");
	    saveConfig(); 
	    }
@EventHandler
public void onPlayerJoin(PlayerJoinEvent evt) {
	final Player player = evt.getPlayer();
	if(!Once.contains(player.getName())) {
		
	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		public void run() {
			double X = config.getInt("X") + .50;
			double Z = config.getInt("Z") + .50;
	double Y = config.getInt("Y");
	
	String world = config.getString("World");
	Location spawn = new Location(Bukkit.getWorld(world), X, Y, Z);
	player.teleport(spawn); 
	Once.add(player.getName());
		} }, 1L);
} } 

@EventHandler
public void onPlayerMove(PlayerMoveEvent evt) {
	Player player = evt.getPlayer();
	if(tpEvt.contains(player.getName())) {
		if(evt.getFrom().getBlockX() != evt.getTo().getBlockX() || evt.getFrom().getBlockZ() != evt.getTo().getBlockZ()) {
		tpEvt.remove(player.getName());
		msg.remove(player.getName());
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("WarpCancelMSG"))); 
		}
			}
	Location loc = player.getLocation();
	double z = loc.getBlockZ();
	double x = loc.getBlockX();
	double Border = config.getInt("Border");
	double cX = config.getInt("X");
	double cZ = config.getInt("Z");
	double X = cX + Border;
	double Z = cZ + Border;
			double Xneg = cX - Border;
			double Zneg = cZ - Border;
			double WholeBorder = config.getInt("WholeWorldBorder");
			if(x >= WholeBorder || z >= WholeBorder) {
				if(!player.hasPermission("jProtect.BordBy")) {
				evt.setCancelled(true);
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("WorldBorderMSG"))); 
			} }

	if (x >= X || z >= Z || x <= Xneg || z <= Zneg) {
		if(!pvp.contains(player.getName())) {
		pvp.add(player.getName()); }
		if(!One.contains(player.getName())) { 
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("LostMSG"))); 
		One.add(player.getName());
		}
	}

}
@EventHandler
public void onPlayerRespawn(PlayerDeathEvent evt) {
	Player player = (Player) evt.getEntity();
	if(player instanceof Player) {

} }
@EventHandler
public void onPlayerRespawn(PlayerRespawnEvent evt) {
	final Player p = evt.getPlayer();
	if(pvp.contains(p.getName())) {
		pvp.remove(p.getName());
		One.remove(p.getName());
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("RegainMSG"))); 
	}
	this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
		public void run() {
			double X = config.getInt("X") + .50;
			double Y = config.getInt("Y");
			double Z = config.getInt("Z") + .50;
			float yaw = config.getInt("yaw");
			float pitch = config.getInt("pitch");
	String world = config.getString("World");
	Location spawn = new Location(Bukkit.getWorld(world), X, Y, Z, yaw, pitch);
	p.teleport(spawn); } }, 1L);
}
@EventHandler
public void onEntityDamage(EntityDamageByEntityEvent evt) {
	Entity e = evt.getEntity();
	Entity damager = evt.getDamager();
	if(e instanceof Player) {
		Player player = (Player) e;
		Player hurter = (Player) damager;
		if(pvp.contains(player.getName())) { //If they have this then they can be pvped. If not they can not.
			if(!pvp.contains(hurter.getName())) {
				if(!(player.getHealth() >= 20)) {
				pvp.add(hurter.getName());
				One.add(hurter.getName());
				hurter.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("LostMSG")));  }
			}
		} else {
			evt.setCancelled(true);
		if(damager instanceof Player) {
			hurter.sendMessage(ChatColor.RED + "This player has spawn protection!");
		}
		}
		
	} else {
		Player player = (Player) e;
		if(!(pvp.contains(player.getName()))) {
			evt.setCancelled(true);
		}
	}
}
@EventHandler
public void DamageEvent(EntityDamageEvent evt) {
	Entity e = evt.getEntity();
	if(e instanceof Player) {
		Player p = (Player) e;
		if(!(pvp.contains(p.getName()))) {
			evt.setCancelled(true);
			}
		}
	}


@EventHandler
public void onBlockBreak(BlockBreakEvent evt) {
	Player player = evt.getPlayer();
	Location loc = player.getLocation();
	double z = loc.getBlockZ();
	double x = loc.getBlockX();
	double Border = config.getInt("Border");
	double cX = config.getInt("X") + .50;
	double cZ = config.getInt("Z") + .50;
	double X = cX + Border;
	double Z = cZ + Border;
			double Xneg = cX - Border;
			double Zneg = cZ - Border;
	if (x >= X || z >= Z || x <= Xneg || z <= Zneg){ } else {
		if(player.hasPermission("jProtect.canBreak")) { } else {
		evt.setCancelled(true);
		player.sendMessage(ChatColor.RED + "You do not have permission to break here."); }
	}
}
@EventHandler
public void onBlockPlace(BlockPlaceEvent evt) {
	Player player = evt.getPlayer();
	Location loc = player.getLocation();
	double z = loc.getBlockZ();
	double x = loc.getBlockX();
	double Border = config.getInt("Border");
	double cX = config.getInt("X") + .50;
	double cZ = config.getInt("Z") + .50;
	double X = cX + Border;
	double Z = cZ + Border;
			double Xneg = cX - Border;
			double Zneg = cZ - Border;
	if(x >= X || z >= Z || x <= Xneg || z <= Zneg) { } else {
		if(player.hasPermission("jProtect.canBuild")) { } else {
		evt.setCancelled(true);
		player.sendMessage(ChatColor.RED + "You do not have permission to place here."); }
	}
}
public boolean onCommand(final CommandSender sender, Command cmd, String commandLabel, String[] args) {
	if(cmd.getName().equalsIgnoreCase("spawn")) {
		
		final Player player = (Player) sender;
		if(player.hasPermission("jProtect.spawn")) {
			double X = config.getInt("X") + .50;
			double Y = config.getInt("Y");
			double Z = config.getInt("Z") + .50;
			float yaw = config.getInt("yaw");
			float pitch = config.getInt("pitch");
				String world = config.getString("World");
				for(Player players : Bukkit.getOnlinePlayers()) {
				
					
				  
				    
		if(Bukkit.getOnlinePlayers().length == 1) {
	Location spawn = new Location(Bukkit.getWorld(world), X, Y, Z, yaw, pitch);
	player.teleport(spawn);
	if((pvp.contains(player.getName()) && One.contains(player.getName()))) {
	pvp.remove(player.getName()); 
	
	 
	One.remove(player.getName());
	
	player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("RegainMSG"))); }
		}
		
		
		
		else if(Bukkit.getOnlinePlayers().length > 1){
			List<Entity> near = player.getNearbyEntities(10, 10, 10);
			for(Entity e : near) {
				if(e instanceof Player) {
					Location location = e.getLocation();
					if(e.getLocation().distance(location) >= 5) {
				    	if(!(players.getName().equals(player.getName()))) {
				    		if(!(msg.contains(player.getName()))) {
				    		tpEvt.add(player.getName());
				    		
				    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NearbyMSG")));
				    	msg.add(player.getName());
				    	
				    		}
						this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							public void run() {
							if(tpEvt.contains(player.getName())) {
								double X = config.getInt("X") + .50;
								double Y = config.getInt("Y");
								double Z = config.getInt("Z") + .50;
								float yaw = config.getInt("yaw");
								float pitch = config.getInt("pitch");
						String world = config.getString("World");
						Location spawn = new Location(Bukkit.getWorld(world), X, Y, Z, yaw, pitch);
						player.teleport(spawn);
						tpEvt.remove(player.getName());
						msg.remove(player.getName());
						if((pvp.contains(player.getName()) && One.contains(player.getName()))) {
							pvp.remove(player.getName()); 
							
								 
							One.remove(player.getName());
							
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("RegainMSG")));
						}
							
									}  
							
							
							} }, 200L); 
				    	}
			} } }
		
				    } else {
				    	
				    if(!(msg.contains(player.getName()))) {
				    	Location spawn = new Location(Bukkit.getWorld(world), X, Y, Z, yaw, pitch);
						player.teleport(spawn);
						msg.add(player.getName());
						this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
							public void run() {
							tpEvt.remove(player.getName());
							msg.remove(player.getName());
									}  
							
							
							}, 20L); 
						if((pvp.contains(player.getName()) && One.contains(player.getName()))) {
							pvp.remove(player.getName()); 
							
								 
							One.remove(player.getName());
							
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("RegainMSG"))); }
									
				    }
				}
				
				
			  
				 } 
		
		}
	}
	
	if(cmd.getName().equalsIgnoreCase("setspawn")) {
		Player p = (Player) sender;
		if(p.hasPermission("jProtect.setspawn")) {
		Location loc = p.getLocation();
		int X = loc.getBlockX();
		int Z = loc.getBlockZ();
		int Y = loc.getBlockY();
		config.set("X", X);
		config.set("Y", Y);
		config.set("Z", Z);
		config.set("yaw", loc.getYaw());
		config.set("pitch", loc.getPitch());
		this.saveConfig();
		p.sendMessage(ChatColor.GRAY + "Set the spawn for all players!"); }
	}
	if(cmd.getName().equalsIgnoreCase("jProtect")) {
		
		
		
		
	}
	
	
	
	
	
	return true; }



public void onDisable() {
	config.set("Test", false);
}
}
