package me.rik_mclightning1;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class DungeonClaim {

	Location loc;
	int xW; //width in x direction
	int zW; //width in z direction
	int h;  //height
	ArrayList<Material> placeList;
	ArrayList<Material> breakList;
	
	Location[] locList;
	
	public DungeonClaim(Location location, int xWidth, int zWidth, int height, String placeBlocks, String breakBlocks) {
		
		placeList = new ArrayList<Material>();
		breakList = new ArrayList<Material>();
		
		loc = location;
		xW = xWidth;
		zW = zWidth;
		h = height;
		World world = loc.getWorld();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		
		if(!(placeBlocks.equalsIgnoreCase(""))) {
			
			String[] placeBlocksList = placeBlocks.split(",");
			for(String item : placeBlocksList) {
				
				placeList.add(Material.getMaterial(item));
				
			}
		
		}
		
		if(!(breakBlocks.equalsIgnoreCase(""))) {
			
			String[] breakBlocksList = breakBlocks.split(",");
			for(String item : breakBlocksList) {
				
				breakList.add(Material.getMaterial(item));
				
			}
		
		}
		
		Location[] tempList = {
				
				new Location(world, x, y, z),
				new Location(world, x, y+1, z),
				new Location(world, x+1, y, z),
				new Location(world, x, y, z+1),
				
				new Location(world, x+xW, y, z),
				new Location(world, x+xW, y+1, z),
				new Location(world, x+xW-1, y, z),
				new Location(world, x+xW, y, z+1),
				
				new Location(world, x, y, z+zW),
				new Location(world, x, y+1, z+zW),
				new Location(world, x+1, y, z+zW),
				new Location(world, x, y, z+zW-1),
				
				new Location(world, x+xW, y, z+zW),
				new Location(world, x+xW, y+1, z+zW),
				new Location(world, x+xW-1, y, z+zW),
				new Location(world, x+xW, y, z+zW-1),
				
				new Location(world, x, y+h, z),
				new Location(world, x, y+h-1, z),
				new Location(world, x+1, y+h, z),
				new Location(world, x, y+h, z+1),
				
				new Location(world, x+xW, y+h, z),
				new Location(world, x+xW, y+h-1, z),
				new Location(world, x+xW-1, y+h, z),
				new Location(world, x+xW, y+h, z+1),
				
				new Location(world, x, y+h, z+zW),
				new Location(world, x, y+h-1, z+zW),
				new Location(world, x+1, y+h, z+zW),
				new Location(world, x, y+h, z+zW-1),
				
				new Location(world, x+xW, y+h, z+zW),
				new Location(world, x+xW, y+h-1, z+zW),
				new Location(world, x+xW-1, y+h, z+zW),
				new Location(world, x+xW, y+h, z+zW-1),
				
		};
		
		locList = tempList.clone();
		
	}
	
	public boolean inClaim(Location loc) {
		
		if(loc.getX() >= this.loc.getX() && loc.getX() <= this.loc.getX() + this.xW) {
			
			if(loc.getZ() >= this.loc.getZ() && loc.getZ() <= this.loc.getZ() + this.zW) {
				
				if(loc.getY() >= this.loc.getY() && loc.getY() <= this.loc.getY() + this.h) {
					
					return true;
					
				}
				
			}
			
		}
		return false;
		
	}
	
	@SuppressWarnings("deprecation")
	public void showBorders(Player player) {
		
		if(!(player.getWorld() == this.loc.getWorld())) return;
		
		for(Location loc : this.locList) {
			
			player.sendBlockChange(loc, Material.EMERALD_BLOCK, (byte) 0);
		
		}
			
	}
	
	@SuppressWarnings("deprecation")
	public void hideBorders(Player player) {
		
		for(Location loc : this.locList) {
			
			player.sendBlockChange(loc, loc.getBlock().getType(), loc.getBlock().getData());
		
		}
			
	}
	
}
