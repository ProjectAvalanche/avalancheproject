package com.bitbucket.avalancheplugin.dev;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.bitbucket.avalancheplugin.Main;

public class AvalancheAPI extends Main{
	public static Main plugin;
	public AvalancheAPI(Main instance){
		plugin = instance;
	}
	public static FileConfiguration getDataFile(){
		return getData();
	}
	public static void saveDataFile(){
		plugin.saveData();
		return;
	}
	public static ConfigurationSection getCoreSection(){
		return getData().getConfigurationSection("CORE");
	}
	public static ConfigurationSection getChatSection(){
		return getData().getConfigurationSection("CHAT");
	}
	public static ConfigurationSection getPermisSection(){
		return getData().getConfigurationSection("PERMS");
	}
	public static ConfigurationSection getChunkClaimSection(){
		return getData().getConfigurationSection("CHUNK_CLAIM");
	}
	public static ConfigurationSection getServerGuardSection(){
		return getData().getConfigurationSection("SERVERGUARD");
	}
	public static File getAvalancheFolder(){
		return plugin.getDataFolder();
	}
}
