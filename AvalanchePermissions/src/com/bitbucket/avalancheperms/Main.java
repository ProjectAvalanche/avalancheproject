package com.bitbucket.avalancheperms;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bitbucket.avalancheperms.pmanagement.AssignPerms;
import com.bitbucket.avalancheplugin.dev.AvalancheAPI;

public class Main extends JavaPlugin implements Listener{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Main plugin;
	public static String n = System.getProperty("line.separator");
	public String colorize(String str){
		return str.replaceAll("(?i)&([a-f0-9k-or])", "\u00a7$1");
		}
	@Override
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(new AssignPerms(), this);
		if(AvalancheAPI.getPermsFile().options().header().equals("AVALANCHE PERMISSIONS CONFIG" + n + n + "Options and settings for Avalanche Permissions. This file is not necessary if the server is not running Avalanche Permissions." + n)){
			AvalancheAPI.getPermsFile().options().header("CONFIGURATION FOR AVALANCHE PERMISSIONS" + n + n + "WARNING: Configuring permissions improperly can result in errors. It is HIGHLY recommended that you set up permissions" + n + "in-game with the Avalanche perms commands. It is much easier and less risky. =)" + n);
			AvalancheAPI.getPermsFile().createSection("GROUPS");
			AvalancheAPI.savePermsFile();
		}
	}
}
