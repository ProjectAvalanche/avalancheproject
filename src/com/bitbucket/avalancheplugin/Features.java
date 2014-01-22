package com.bitbucket.avalancheplugin;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Features extends Main implements Listener{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void entityClick(PlayerInteractEntityEvent e){
		Player p = e.getPlayer();
		if(e.getRightClicked() instanceof LivingEntity){
			if(p.hasPermission("avalanche.features.ride." + e.getRightClicked().getType().getName())){
				e.getRightClicked().setPassenger(p);
			}
		}
		return;
	}
}
