package com.bitbucket.avalancheperms.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bitbucket.avalancheperms.Main;
import com.bitbucket.avalancheplugin.dev.AvalancheAPI;

public class Commands extends Main{
	public static void permsCommand(CommandSender p, Command cmd, String[] args){
		if(cmd.getName().equalsIgnoreCase("aperms")){
			if(!p.hasPermission("avalanche.permissions.manage")){
				p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
				return;
			}
			if(args.length < 1){
				p.sendMessage(ChatColor.WHITE + "Avalanche Permissions");
				p.sendMessage(ChatColor.WHITE + "By: " + ChatColor.GRAY + "Freelix2000 & Paldiu");
				p.sendMessage(ChatColor.WHITE + "Version: " + ChatColor.GRAY + "1.0");
				p.sendMessage(ChatColor.WHITE + "Type " + ChatColor.GRAY + "/aperms help" + ChatColor.GRAY + " for commands.");
				return;
			}
			if(args[0].equalsIgnoreCase("help")){
				p.sendMessage(ChatColor.WHITE + "**********" + ChatColor.GRAY + "Avalanche Permissions Help" + ChatColor.WHITE + "**********");
				p.sendMessage(ChatColor.WHITE + "/aperms help " + ChatColor.GRAY + "Take a guess, genius.");
				p.sendMessage(ChatColor.WHITE + "/aperms creategroup <groupName> [world] " + ChatColor.GRAY + "Create a permissions group");
				p.sendMessage(ChatColor.WHITE + "/aperms delgroup <groupName> " + ChatColor.GRAY + "Delete a group.");
				p.sendMessage(ChatColor.WHITE + "/aperms setperm <group> <permission> " + ChatColor.GRAY + "Set group permission");
				p.sendMessage(ChatColor.WHITE + "/aperms removeperm <group> <permission> " + ChatColor.GRAY + "Remove permission from group.");
				p.sendMessage(ChatColor.WHITE + "/aperms inherit <inheritorGroup> <group> " + ChatColor.GRAY + "Add group to another group's inheritance");
				p.sendMessage(ChatColor.WHITE + "/aperms uninherit <inheritorGroup> <group> " + ChatColor.GRAY + "Remove group inheritance");
				p.sendMessage(ChatColor.WHITE + "/aperms setplayerperm <player> <permission> " + ChatColor.GRAY + "Set a player permission");
				p.sendMessage(ChatColor.WHITE + "/aperms delplayerperm <player> <permission> " + ChatColor.GRAY + "Remove a plaeyr permission");
				p.sendMessage(ChatColor.WHITE + "/aperms setgroup <player> <group> " + ChatColor.GRAY + "Set a player's group");
				p.sendMessage(ChatColor.WHITE + "/aperms setdefaultgroup <group> " + ChatColor.GRAY + "Set the default group");
				p.sendMessage(ChatColor.WHITE + "/aperms show <p/g> <name> " + ChatColor.GRAY + "Shows group or player info");
				return;
			}
			if(args[0].equalsIgnoreCase("creategroup")){
				if(!p.hasPermission("avalanche.permissions.creategroup")){
					p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return;
				}
				if(args.length < 2){
					p.sendMessage(ChatColor.RED + "Too few arguments.");
					p.sendMessage(ChatColor.RED + "/aperms creategroup <groupName> [world]");
					return;
				}
				String world = "global";
				if(!(args.length < 3)){
					world = args[2];
				}
				if(args[1].contains(":")){
					p.sendMessage(ChatColor.RED + "Illegal character \":\"");
					return;
				}
				if(AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").contains(args[1] + "|" + world)){
					p.sendMessage(ChatColor.RED + "That group already exists!");
					return;
				}
				AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").set(args[1] + "|" + world + ".default", "false");
				AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").set(args[1] + "|" + world + ".permissions", new ArrayList<String>());
				AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").set(args[1] + "|" + world + ".inheritance", new ArrayList<String>());
				AvalancheAPI.savePermsFile();
				p.sendMessage(ChatColor.WHITE + "Group " + ChatColor.GRAY + args[1] + ChatColor.WHITE + " created!");
				return;
			}
			if(args[0].equalsIgnoreCase("delgroup")){
				if(!p.hasPermission("avalanche.permissions.delgroup")){
					p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return;
				}
				if(args.length < 2){
					p.sendMessage(ChatColor.RED + "Too few arguments.");
					p.sendMessage(ChatColor.RED + "/aperms delgroup <group> [world]");
					return;
				}
				String world = null;
				if(args.length < 3){
					if(p instanceof Player){
						world = ((Player) p).getWorld().getName();
					}else{
						p.sendMessage("Console senders must specify a world. Use \"global\" for a global group.");
						return;
					}
				}
				boolean containsGroup = false;
				if(world.equalsIgnoreCase("global")){
					world = "global";
				}
				for(String g : AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").getKeys(false)){
					if(g.split("|")[0].equalsIgnoreCase(args[1] + "|" + world)){
						containsGroup = true;
						return;
					}
				}
				if(!containsGroup){
					p.sendMessage(ChatColor.RED + "Could not find group \"" + args[1] + "\" in the specified world/your world. If it is a global group, then please specify the world as \"global\"");
					return;
				}
				AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").set(args[1] + "|" + world + ".default", null);
				AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").set(args[1] + "|" + world + ".permissions", null);
				AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").set(args[1] + "|" + world + ".inheritance", null);
				AvalancheAPI.getPermsFile().getConfigurationSection("GROUPS").set(args[1] + "|" + world, null);
				AvalancheAPI.savePermsFile();
				p.sendMessage(ChatColor.WHITE + "Group " + ChatColor.GRAY + args[1] + ChatColor.WHITE + " removed in world " + ChatColor.GRAY + world + ChatColor.WHITE + ".");
				return;
			}
		}
	}
}
