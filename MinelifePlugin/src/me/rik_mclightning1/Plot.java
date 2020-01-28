package me.rik_mclightning1;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Plot {
	
	int x;
	int y;
	int z;
	int energy;
	String owner;
	ArrayList<String> residents = new ArrayList<String>();
	World world;
	Location[] locList;
	int price;
	boolean upgraded;
	int removeTime;

	public Plot(String plotWorld, String plotX, String plotY, String plotZ, String plotEnergy, String plotOwner, String plotResidents, String plotPrice, String plotUpgraded, String plotRemoveTime) {
		
		x = Integer.parseInt(plotX);
		y = Integer.parseInt(plotY);
		z = Integer.parseInt(plotZ);
		energy = Integer.parseInt(plotEnergy);
		owner = plotOwner;
		for(String resident : plotResidents.split(",")) residents.add(resident);
		world = Bukkit.getServer().getWorld(plotWorld);
		Location[] tempList = {
				
				new Location(world, x+4, y-1, z+4),
				new Location(world, x-4, y-1, z+4),
				new Location(world, x+4, y-1, z-4),
				new Location(world, x-4, y-1, z-4),
				
				new Location(world, x+4, y, z+4),
				new Location(world, x-4, y, z+4),
				new Location(world, x+4, y, z-4),
				new Location(world, x-4, y, z-4),
				
				new Location(world, x+4, y+7, z+4),
				new Location(world, x-4, y+7, z+4),
				new Location(world, x+4, y+7, z-4),
				new Location(world, x-4, y+7, z-4),
				
				new Location(world, x+4, y+6, z+4),
				new Location(world, x-4, y+6, z+4),
				new Location(world, x+4, y+6, z-4),
				new Location(world, x-4, y+6, z-4),
				
				new Location(world, x+4, y-1, z+3),
				new Location(world, x-4, y-1, z+3),
				new Location(world, x+4, y-1, z-3),
				new Location(world, x-4, y-1, z-3),
				
				new Location(world, x+3, y-1, z+4),
				new Location(world, x-3, y-1, z+4),
				new Location(world, x+3, y-1, z-4),
				new Location(world, x-3, y-1, z-4),
				
				new Location(world, x+4, y+7, z+3),
				new Location(world, x-4, y+7, z+3),
				new Location(world, x+4, y+7, z-3),
				new Location(world, x-4, y+7, z-3),
				
				new Location(world, x+3, y+7, z+4),
				new Location(world, x-3, y+7, z+4),
				new Location(world, x+3, y+7, z-4),
				new Location(world, x-3, y+7, z-4),
				
		};
		price = Integer.parseInt(plotPrice);
		locList = tempList.clone();
		upgraded = Boolean.valueOf(plotUpgraded);
		removeTime = Integer.parseInt(plotRemoveTime);
		
	}
	public boolean inPlot(int x, int y, int z, World world) {
		
		if(world == this.world) {
		
			if(x >= this.x-4 && x <= this.x+4) {
				
				if(y >= this.y-1 && y <= this.y+7) {
					
					if(z >= this.z-4 && z <= this.z+4) {
						
						return true;
						
					}
					
				}
				
			}
			
		}
		
		return false;
		
	}
	
	public String getRank(String UUID) {
		
		if(UUID.equals(this.owner)) {
			
			return "owner";
			
		}
		else {
			
			for(String resident : this.residents) {
				
				if(UUID.equals(resident)) {
					
					return "resident";
					
				}
				
			}
			
		}
		
		return "none";
		
	}
	
	@SuppressWarnings("deprecation")
	public void showBorders(Player player) {
		
		if(!(player.getWorld() == this.world)) return;
		
		for(Location loc : this.locList) {
			
			if(this.getRank(player.getUniqueId().toString()).equals("none")) player.sendBlockChange(loc, Material.WOOL, (byte) 14);
			if(this.getRank(player.getUniqueId().toString()).equals("owner")) player.sendBlockChange(loc, Material.WOOL, (byte) 5);
			if(this.getRank(player.getUniqueId().toString()).equals("resident")) player.sendBlockChange(loc, Material.WOOL, (byte) 3);
		
		}
			
	}
	
	@SuppressWarnings("deprecation")
	public void hideBorders(Player player) {
		
		for(Location loc : this.locList) {
			
			player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
		
		}
			
	}
	
}
