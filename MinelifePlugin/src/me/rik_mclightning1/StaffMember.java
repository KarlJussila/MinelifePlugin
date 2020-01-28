package me.rik_mclightning1;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class StaffMember {

	Location lastSurvivalLoc;
	Player player;
	boolean monitoring;
	PlayerInventory inv;
	
	public StaffMember(Player normalPlayer) {
		
		player = normalPlayer;
		lastSurvivalLoc = player.getLocation();
		monitoring = false;
		inv = null;
		
	}
	
}