package com.bitbucket.avalancheplugin.commands;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.bitbucket.avalancheplugin.Main;

public class Commands extends Main implements Listener{
	public static ArrayList<Entity> piggies = new ArrayList<Entity>();
	public static Main plugin;
	public Commands(Main instance){
		plugin = instance;
	}
	public static HashMap<String, Double> superkill = new HashMap<String, Double>();
	public static void command(Command cmd, Player p, String[] args){
		if(cmd.getName().equalsIgnoreCase("purge")){
			if(!p.hasPermission("avalanche.command.purge")){
				p.sendMessage(ChatColor.RED + "You do not have permission to use that command.");
				return;
			}
			if(args.length < 1){
				p.sendMessage(ChatColor.RED + "Too few arguments.");
				p.sendMessage(ChatColor.RED + "/purge <entityType>");
				return;
			}
			EntityType entity = null;
			try{
				entity = EntityType.valueOf(args[0].toUpperCase());
			}catch(IllegalArgumentException iae){ 
				p.sendMessage(ChatColor.RED + "Strange entity type \"" + args[0] + "\". Please try different variations of that name.");
				return;
			}
			if(!p.hasPermission("avalanche.command.purge." + args[0].toLowerCase())){
				p.sendMessage(ChatColor.RED + "You do not have permission to purge that entity.");
				return;
			}
			Integer amount = 0;
			for(Entity e : p.getWorld().getEntities()){
				if(e.getType().equals(entity) && !e.equals(p)){
					e.getWorld().strikeLightning(e.getLocation());
					amount = amount + 1;
					if(e instanceof LivingEntity){
						((LivingEntity) e).setHealth(0);
					}else{
						e.remove();
					}
				}
			}
			p.sendMessage(ChatColor.GRAY + "Purged " + ChatColor.WHITE + amount + ChatColor.GRAY + " entities.");
			return;
		}
		if(cmd.getName().equalsIgnoreCase("globalmsg")){
			if(!p.hasPermission("avalanche.commands.globalmsg")){
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
				return;
			}
			if(args.length < 1){
				p.sendMessage(ChatColor.RED + "Too few arguments.");
				p.sendMessage(ChatColor.RED + "/globalmsg <message>");
				p.sendMessage("");
				p.sendMessage(ChatColor.RED + "Broadcasts raw message to entire server. Feel free to use color codes, and use %name% to insert the viewer's name. This name will appear different for each player.");
				return;
			}
			StringBuilder builder = new StringBuilder();
			for(String s : args){
				builder.append(" " + s);
			}
			String msg = builder.toString().replaceFirst(" ", "");
			for(Player pl : Bukkit.getOnlinePlayers()){
				pl.sendMessage(colorize(msg.replaceAll("%name%", pl.getName())));
			}
			return;
		}
		if(cmd.getName().equalsIgnoreCase("piggyback")){
			if(!p.hasPermission("avalanche.commands.piggyback")){
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
				return;
			}
			if(args.length < 1){
				p.sendMessage(ChatColor.RED + "Too few arguments.");
				p.sendMessage(ChatColor.RED + "/piggyback <entityType>");
				return;
			}
			EntityType entity = null;
			try{
				entity = EntityType.valueOf(args[0].toUpperCase());
			}catch(IllegalArgumentException iae){
				p.sendMessage(ChatColor.RED + "Strange entity \"" + args[0] + "\". Please try different variations of that name.");
				return;
			}
			if(!entity.isAlive()){
				p.sendMessage(ChatColor.RED + "Entity must be a living entity.");
				return;
			}
			if(!p.hasPermission("avalanche.commands.piggyback." + args[0].toLowerCase())){
				p.sendMessage(ChatColor.RED + "You do not have permission to piggy back that entity.");
				return;
			}
			final LivingEntity e = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), entity);
			e.setPassenger(p);
			piggies.add(e);
			new BukkitRunnable(){
				public void run(){
						if(piggies.contains(e)){
							e.remove();
						}
					}
			}.runTaskLater(plugin, 20*60);
			return;
		}
		if(cmd.getName().equalsIgnoreCase("superkill")){
			if(!p.hasPermission("avalanche.commands.superkill")){
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
				return;
			}
			if(args.length < 1){
				p.sendMessage(ChatColor.RED + "Too few arguments.");
				p.sendMessage(ChatColor.RED + "/superkill <player>");
				return;
			}
			if(Bukkit.getPlayer(args[0]) == null){
				p.sendMessage(ChatColor.RED + "\"" + args[0] + "\" is not online.");
				return;
			}
			Player v = Bukkit.getPlayer(args[0]);
			if(v.hasPermission("avalanche.commands.superkill.exempt")){
				p.sendMessage(ChatColor.RED + "You cannot superkill that player.");
					return;
			}
			v.teleport(v.getWorld().getHighestBlockAt(v.getLocation()).getLocation().add(0, 1, 0));
			Double height = v.getLocation().getY() + 25;
			superkill.put(v.getName(), height);
			Location loc = v.getLocation();
			loc.setPitch(-90);
			v.setVelocity(loc.getDirection().multiply(2));
			return;
		}
	}
	@EventHandler
	public void piggyDamage(EntityDamageEvent e){
		if(piggies.contains(e.getEntity())){
			e.setCancelled(true);
			e.getEntity().remove();
			piggies.remove(e);
		}
		return;
	}
	@EventHandler
	public void playerMove(PlayerMoveEvent e){
		if(superkill.containsKey(e.getPlayer().getName())){
			Player p = e.getPlayer();
			if(!(p.getLocation().getY() + 15 < superkill.get(p.getName()))){
				p.getWorld().strikeLightningEffect(p.getLocation());
				p.getWorld().strikeLightningEffect(p.getLocation());
				p.getWorld().strikeLightningEffect(p.getLocation());
				p.getWorld().createExplosion(p.getLocation(), 15);
				if(!p.isDead()){
					p.setHealth(0);
				}
				superkill.remove(p.getName());
			}
		}
		return;
	}
}
