package me.rik_mclightning1;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Town {
	
	String name;
	String owner;
	ArrayList<String> citizens = new ArrayList<String>();
	ArrayList<String> builders = new ArrayList<String>();
	ArrayList<String> managers = new ArrayList<String>();
	World world;
	int x;
	int y;
	int z;
	int stockpileEnergy;
	int energy;
	int radius;
	Location location;
	boolean growthFrozen;
//	ArrayList<Location> borders = new ArrayList<Location>();

	public Town(String townName, String townOwner, String townBuilders, String townManagers, String townCitizens, String townLocation, String townStockpileEnergy, String townEnergy, String townRadius, String townGrowthFrozen) {
		
		name = townName;
		owner = townOwner;			
		for(String citizen : townCitizens.split(",")) citizens.add(citizen);
		for(String builder : townBuilders.split(",")) builders.add(builder);
		for(String manager : townManagers.split(",")) managers.add(manager);
		world = Bukkit.getServer().getWorld(townLocation.split(",")[0]);
		x = Integer.parseInt(townLocation.split(",")[1]);
		y = Integer.parseInt(townLocation.split(",")[2]);
		z = Integer.parseInt(townLocation.split(",")[3]);
		stockpileEnergy = Integer.parseInt(townStockpileEnergy);
		energy = Integer.parseInt(townEnergy);
		radius = Integer.parseInt(townRadius);
		location = new Location(world,x,y,z);
		growthFrozen = Boolean.valueOf(townGrowthFrozen);
		
	}
	
	public boolean inTown(Location loc) {
		
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
			
			for(String manager : this.managers) {
				
				if(UUID.equals(manager)) {
					
					return("manager");
					
				}
				
			}
			for(String builder : this.builders) {
				
				if(UUID.equals(builder)) {
					
					return("builder");
					
				}
				
			}
			for(String citizen : this.citizens) {
				
				if(UUID.equals(citizen)) {
					
					return("citizen");
					
				}
				
			}
			
		}
		
		return("none");
		
	}
	
//	@SuppressWarnings("deprecation")
//	public void showBorders(Player player) {
//		
//		String playerRank = this.getRank(player.getName());
//		
//		for(Location loc : this.borders) {
//			
//			if(playerRank.equals("none")) {
//				
//				player.sendBlockChange(loc, Material.WOOL, (byte) 14);
//				
//			}
//			else if(playerRank.equals("owner")) {
//				
//				player.sendBlockChange(loc, Material.WOOL, (byte) 5);
//				
//			}
//			else {
//				
//				player.sendBlockChange(loc, Material.WOOL, (byte) 3);
//				
//			}
//			
//		}
//		
//	}
//	
//	@SuppressWarnings("deprecation")
//	public void hideBorders(Player player) {
//		
//		for(Location loc : this.borders) {
//			
//			player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
//		
//		}
//			
//	}
//	
}
