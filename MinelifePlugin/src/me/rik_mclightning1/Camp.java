package me.rik_mclightning1;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Camp {
	
	String name;
	String owner;
	ArrayList<String> members = new ArrayList<String>();
	World world;
	int x;
	int y;
	int z;
	int energy;
	int radius;
	Location location;

	public Camp(String campOwner, String campMembers, String campLocation, String campEnergy, String campRadius) {
		
		owner = campOwner;
		for(String manager : campMembers.split(",")) members.add(manager);
		world = Bukkit.getServer().getWorld(campLocation.split(",")[0]);
		x = Integer.parseInt(campLocation.split(",")[1]);
		y = Integer.parseInt(campLocation.split(",")[2]);
		z = Integer.parseInt(campLocation.split(",")[3]);
		energy = Integer.parseInt(campEnergy);
		radius = Integer.parseInt(campRadius);
		location = new Location(world,x,y,z);
		
	}
	
	public boolean inCamp(Location loc) {
		
		if(loc.getWorld() == this.world) {
		
			Location newLoc = new Location(loc.getWorld(),loc.getX(),this.y,loc.getZ());
			if(newLoc.distance(this.location) <= this.radius) {
			
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public String getRank(String UUID) {
		
		if(UUID.equals(this.owner)) {
			
			return("owner");
			
		}
		else {
			
			for(String manager : this.members) {
				
				if(UUID.equals(manager)) {
					
					return("member");
					
				}
				
			}
			
		}
		
		return("none");
		
	}
		
}
