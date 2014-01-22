package com.bitbucket.avalancheplugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bitbucket.avalancheplugin.commands.Commands;

public class Main extends JavaPlugin implements Listener{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Main main;
	public static String n = System.getProperty("line.separator");
	public File folder = getDataFolder();
	static FileConfiguration config = null;
	public File file = null;
	public static String colorize(String str){
		return str.replaceAll("(?i)&([a-f0-9k-or])", "\u00a7$1");
		}
	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new Features(), this);
		Bukkit.getPluginManager().registerEvents(new Commands(this), this);
		//File file1 = new File(getDataFolder() + File.separator + "config.yml");
		//if(!file1.exists()){
			//getLogger().info("Generating config file");
			//getConfig().options().copyDefaults(true);
			//saveConfig();
			//getLogger().info("Done!");
		//}
		file = new File(getDataFolder() + File.separator + "data.yml");
		config = YamlConfiguration.loadConfiguration(file);
	    if(!file.exists()){
	        try{
	            file.createNewFile();
	            getData().options().header("DATA FILE FOR AVALANCHE" + n + n + "This is the data file used by all Avalanche plugins. Please do not mess with data, all changes that you might need" + n + "to make can be done with in-game commands." + n);
	            getData().createSection("CORE");
	            getData().createSection("CHAT");
	            getData().createSection("PERMS");
	            getData().createSection("CHUNK_CLAIM");
	            getData().createSection("SERVERGUARD");
	        }catch(IOException x){
	            getLogger().severe("Could not create data file!");
	        }
	    }
	    getData().options().copyDefaults(true);
	    saveData();
	    getLogger().info("=-=-=-=-=-=-=-=");
	    getLogger().info("Avalanche Core");
	    getLogger().info("By: Freelix2000 & Paldiu");
	    getLogger().info("=-=-=-=-=-=-=-=");
	    if(avalChatEnabled()){
	    	getLogger().info("Found AvalancheChat");
	    }else{
	    	getLogger().info("Could not find AvalancheChat");
	    }
	    if(avalPermsEnabled()){
	    	getLogger().info("Found AvalanchePermissions");
	    }else{
	    	getLogger().info("Could not find AvalanchePermissions");
	    }
	    if(avalChunkClaimEnabled()){
	    	getLogger().info("Found AvalancheChunkClaim");
	    }else{
	    	getLogger().info("Could not find AvalancheChunkClaim");
	    }
	    if(avalAntiHackEnabled()){
	    	getLogger().info("Found AvalancheAntiHack");
	    }else{
	    	getLogger().info("Could not find AvalancheAntiHack");
	    }
	}
	@Override
	public void onDisable(){
		for(Entity e : Commands.piggies){
			e.remove();
		}
		return;
	}
	public static FileConfiguration getData(){
		return config;
	}
	public void saveData(){
		try{
			getData().save(getDataFolder() + File.separator + "data.yml");
		}catch (IOException e){
			getLogger().severe("Could not save data!");
		}
		return;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		Commands.command(cmd, Bukkit.getPlayer(sender.getName()), args);
		return true;
	}
	public static boolean avalChatEnabled(){
		if(Bukkit.getPluginManager().getPlugin("AvalancheChat") == null){
			return false;
		}
		return true;
	}
	public static boolean avalPermsEnabled(){
		if(Bukkit.getPluginManager().getPlugin("AvalanchePermissions") == null){
			return false;
		}
		return true;
	}
	public static boolean avalAntiHackEnabled(){
		if(Bukkit.getPluginManager().getPlugin("AvalancheAntiHack") == null){
			return false;
		}
		return true;
	}
	public static boolean avalChunkClaimEnabled(){
		if(Bukkit.getPluginManager().getPlugin("AvalancheChunkClaim") == null){
			return false;
		}
		return true;
	}
}
