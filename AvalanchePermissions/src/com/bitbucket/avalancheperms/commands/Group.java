package com.bitbucket.avalancheperms.commands;

import java.util.List;

import com.bitbucket.avalancheplugin.dev.AvalancheAPI;

public class Group{
	public static String name = null;
	public static String world = null;
	public Group(String groupName, String worldName){
		name = groupName;
		world = worldName;
	}
	public static List<String> getPermissions(){
		return AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").getStringList(name + "|" + world + ".permissions");
	}
	public static List<String> getInheritedGroups(){
		return AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").getStringList(name + "|" + world + ".inheritance");
	}
	public static boolean isDefault(){
		if(AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").getString(name + "|" + world + ".default").equalsIgnoreCase("true")){
			return true;
		}
		return false;
	}
}
