package me.rik_mclightning1;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

public class Minelife extends JavaPlugin {

	ArrayList<Town> townList = new ArrayList<Town>();
	ArrayList<Plot> plotList = new ArrayList<Plot>();
	ArrayList<AdminTown> adminTownList = new ArrayList<AdminTown>();
	ArrayList<Plot> campList = new ArrayList<Plot>();
	ArrayList<Player> raidCooldownList = new ArrayList<Player>();
	ArrayList<Town> townRaidCooldownList = new ArrayList<Town>();
	ArrayList<Plot> plotRaidCooldownList = new ArrayList<Plot>();
	ArrayList<Player> murdererList = new ArrayList<Player>();
	ArrayList<StaffMember> staffList = new ArrayList<StaffMember>();
	ArrayList<String> muteList = new ArrayList<String>();
	ArrayList<DungeonClaim> dungeonList = new ArrayList<DungeonClaim>();
	int resets;

    private static final Logger log = Logger.getLogger("Minecraft");
    public Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {

		//World_Of_Minelife,-430,68,-174
		if (!setupEconomy()) {
            log.severe(String.format("[%s] - No Vault dependency found!", getDescription().getName()));
        }
        setupPermissions();
		new EventListener(this);
		
		
		CommandListener cmdListener = new CommandListener(this);
		this.getCommand("test").setExecutor(cmdListener);
		this.getCommand("town").setExecutor(cmdListener);
		this.getCommand("claim").setExecutor(cmdListener);
		this.getCommand("plot").setExecutor(cmdListener);
		this.getCommand("admintown").setExecutor(cmdListener);
		this.getCommand("minelife").setExecutor(cmdListener);
		this.getCommand("t").setExecutor(cmdListener);
		this.getCommand("dungeon").setExecutor(cmdListener);
		this.getCommand("setradius").setExecutor(cmdListener);
		this.getCommand("mute").setExecutor(cmdListener);
		this.getCommand("unmute").setExecutor(cmdListener);
		this.getCommand("bal").setExecutor(cmdListener);

		this.getConfig().addDefault("towns", "");
		this.getConfig().addDefault("plots", "");
		this.getConfig().addDefault("adminTowns", "");
		this.getConfig().addDefault("resets", 1);
		this.getConfig().addDefault("dungeons", "");

		this.getConfig().options().copyDefaults(true);
		
		saveConfig();

		resets = getConfig().getInt("resets");
	
		String AdminTowns = removeLastChar(getConfig().getString("adminTowns"));
		if(!AdminTowns.equals("")) {
			
			String[] adminTowns = AdminTowns.split("!");
			
			for(String town : adminTowns) {
	
				String[] adminTownInfo = town.split(";");
				adminTownList.add(new AdminTown(adminTownInfo[0],adminTownInfo[1],adminTownInfo[2],adminTownInfo[3],adminTownInfo[4],adminTownInfo[5],adminTownInfo[6]));
	
			}
			
		}
		
		//Getting towns from config	
		String Towns = removeLastChar(getConfig().getString("towns"));
		
		if(!Towns.equals("")) {
		
			String[] towns = Towns.split("!");
			for(String town : towns) {
	
				String[] townInfo = town.split(";");
				townList.add(new Town(townInfo[0],townInfo[1],townInfo[2],townInfo[3],townInfo[4],townInfo[5],townInfo[6],townInfo[7],townInfo[8],townInfo[9]));
	
			}
			
		}
		
		//Getting plots from config
		String Plots = removeLastChar(getConfig().getString("plots"));
		
		if(!Plots.equals("")) {
		
			String[] plots = Plots.split("!");
			for(String plot : plots) {
	
				String[] plotInfo = plot.split(";");
				plotList.add(new Plot(plotInfo[0],plotInfo[1],plotInfo[2],plotInfo[3],plotInfo[4],plotInfo[5],plotInfo[6],plotInfo[7],plotInfo[8],plotInfo[9]));
	
			}
			
		}
		
		String Dungeons = removeLastChar(getConfig().getString("dungeons"));
		
		if(!Dungeons.equals("")) {
			
			String[] dungeons = Dungeons.split("!");
			for(String dungeon : dungeons) {
				
				String[] dungeonInfo = dungeon.split(";");
				dungeonList.add(new DungeonClaim(new Location(Bukkit.getServer().getWorld(dungeonInfo[3]),(double)Integer.parseInt(dungeonInfo[0]),(double)Integer.parseInt(dungeonInfo[1]),(double)Integer.parseInt(dungeonInfo[2])), Integer.parseInt(dungeonInfo[4]),Integer.parseInt(dungeonInfo[5]), Integer.parseInt(dungeonInfo[6]), dungeonInfo[7], dungeonInfo[8]));
				//dungeons += ("" + (int)dungeon.loc.getX() + ";" + (int)dungeon.loc.getY() + ";" + (int)dungeon.loc.getZ() + ";" + dungeon.loc.getWorld().getName() + ";" + dungeon.xW + ";" + dungeon.zW + ";" + dungeon.h + ";" + placeBlocks + ";" + breakBlocks + "!");
			}
			
		}

		if((resets-1) % 4 == 0) {

			ArrayList<Plot> plotRemoveList = new ArrayList<Plot>();

			for(Plot plot : plotList) {

				for(Town town : townList) {
					
					String playerRank = town.getRank(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName());

					if(town.inTown(new Location(plot.world, (double)plot.x, (double)plot.y, (double)plot.z)) && playerRank != "manager" && playerRank != "owner") {

						EconomyResponse r = econ.withdrawPlayer(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName(), 12);
						if(!r.transactionSuccess()) {

							plot.owner = town.owner;
							econ.depositPlayer(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName(), 12);

						}

					}

				}
				
				for(AdminTown town : adminTownList) {

					String playerRank = town.getRank(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName());
					
					if(town.inTown(new Location(plot.world, (double)plot.x, (double)plot.y, (double)plot.z)) && playerRank != "manager" && playerRank != "owner") {

						EconomyResponse r = econ.withdrawPlayer(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName(), 18);
						if(!r.transactionSuccess()) {

							plot.owner = town.owner;

						}
						else {
							
							econ.depositPlayer(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName(), 18);
							
						}

					}

				}
				
				if(plot.upgraded) {

					EconomyResponse r = econ.withdrawPlayer(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName(), 12);
					if(!r.transactionSuccess()) {

						if(plot.removeTime == resets) {
							
							plotRemoveList.add(plot);
							
						}
						else if(plot.removeTime < resets) {
							
							plot.removeTime = resets + 12;
							
						}

					}
					else {
						
						plot.removeTime = 0;
						
					}

				}

			}
			
			for(Town town : townList) {

				if(town.stockpileEnergy > 0) {

					town.stockpileEnergy -= 500;

				}
				else {

					town.energy -= 500;
					town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));

				}
				if(town.energy > 100000) {
					
					if(town.stockpileEnergy > 0) {
	
						town.stockpileEnergy -= 300;
	
					}
					else {
	
						town.energy -= 300;
						town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));
	
					}
					
				}
				if(town.energy > 200000) {
					
					if(town.stockpileEnergy > 0) {
						
						town.stockpileEnergy -= 800;
						
					}
					else {
						
						town.energy -= 800;
						town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));
						
					}
					
				}
				if(town.energy > 300000) {
					
					if(town.stockpileEnergy > 0) {
						
						town.stockpileEnergy -= 1600;
						
					}
					else {
						
						town.energy -= 1600;
						town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));
						
					}
					
				}
				if(town.energy > 500) {
					
					if(town.stockpileEnergy > 0) {
						
						town.stockpileEnergy -= 3050;
						
					}
					else {
						
						town.energy -= 3050;
						town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));
						
					}
					
				}
				if(town.energy == 1000000) {
					
					if(town.stockpileEnergy > 0) {
						
						town.stockpileEnergy -= 6250;
						
					}
					else {
						
						town.energy -= 6250;
						town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));
						
					}
					
				}
				
			}

		}

	}

	@Override
	public void onDisable() {

		String towns = "";
		for(Town town : townList) {

			String citizens = "";
			for(String citizen : town.citizens) {

				citizens += citizen+",";

			}
			String builders = "";
			for(String builder : town.builders) {

				builders += builder+",";

			}
			String managers = "";
			for(String manager : town.managers) {

				managers += manager+",";

			}
			towns += (town.name+";"+town.owner+";"+builders+";"+managers+";"+citizens+";"+town.location.getWorld().getName()+","+(int)town.location.getX()+","+(int)town.location.getY()+","+(int)town.location.getZ()+";"+town.stockpileEnergy+";"+town.energy+";"+town.radius+";"+town.growthFrozen+"!");

		}
		this.getConfig().set("towns",towns);

		//Plot(String plotWorld, String plotX, String plotY, String plotZ, String plotEnergy, String plotOwner, String plotResidents)
		String plots = "";
		for(Plot plot : plotList) {

			String residents = "";
			for(String resident : plot.residents){

				residents += resident + ",";

			}

			plots += plot.world.getName()+";"+plot.x+";"+plot.y+";"+plot.z+";"+plot.energy+";"+plot.owner+";"+residents+";"+plot.price+";"+plot.upgraded+";"+plot.removeTime+"!";

		}
		this.getConfig().set("plots", plots);

		String adminTowns = "";
		for(AdminTown adminTown : adminTownList) {

			String citizens = "";
			for(String citizen : adminTown.citizens) {

				citizens += citizen+",";

			}
			String builders = "";
			for(String builder : adminTown.builders) {

				builders += builder+",";

			}
			String managers = "";
			for(String manager : adminTown.managers) {

				managers += manager+",";

			}
			adminTowns += (adminTown.name+";"+adminTown.owner+";"+builders+";"+managers+";"+citizens+";"+adminTown.location.getWorld().getName()+","+(int)adminTown.location.getX()+","+(int)adminTown.location.getY()+","+(int)adminTown.location.getZ()+";"+adminTown.radius+"!");

		}
		this.getConfig().set("adminTowns",adminTowns);
		
		String dungeons = "";
		for(DungeonClaim dungeon : dungeonList) {
			
			String placeBlocks = "BEDROCK,";
			String breakBlocks = "BEDROCK,";
			
			for(Material item : dungeon.placeList) {
				
				placeBlocks += (item.toString() + ",");
				
			}
			for(Material item : dungeon.breakList) {
				
				breakBlocks += (item.toString() + ",");
				
			}
			
			dungeons += ("" + (int)dungeon.loc.getX() + ";" + (int)dungeon.loc.getY() + ";" + (int)dungeon.loc.getZ() + ";" + dungeon.loc.getWorld().getName() + ";" + dungeon.xW + ";" + dungeon.zW + ";" + dungeon.h + ";" + placeBlocks + ";" + breakBlocks + "!");
			
		}
		this.getConfig().set("dungeons", dungeons);
		
		this.getConfig().set("resets", this.resets + 1);
		saveConfig();

	}

/*	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("town") && sender instanceof Player) {

			Player player = (Player) sender;
			int length = args.length;

			if(length > 1) {

				if(args[0].equalsIgnoreCase("rename")) {

					for(Town town : this.townList) {

						if(town.inTown(player.getLocation())) {

							if(args[1].equals("NNTC")) {
								
								town.name = " ";
								return true;
								
							}
							if(length >= 2) {
								
								if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equals(sender.getName())) {
	
									player.sendMessage("Your town has been renamed from "+town.name+" to "+args[1]);
									town.name = args[1];
									return true;
	
								}
								
							}

						}

					}
					for(AdminTown town : this.adminTownList) {

						if(town.inTown(player.getLocation())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equals(sender.getName())) {

								player.sendMessage("Your town has been renamed from "+town.name+" to "+args[1]);
								town.name = args[1];
								return true;

							}

						}

					}

				}
				else if(args[0].equalsIgnoreCase("transfer")){

					for(Town town : this.townList) {

						if(town.inTown(player.getLocation())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equals(sender.getName())) {

								player.sendMessage("Your town has been transfered to "+args[1]);
								town.owner = Bukkit.getServer().getPlayer(args[1]).getUniqueId().toString();
								return true;

							}

						}

					}
					for(AdminTown town : this.adminTownList) {

						if(town.inTown(player.getLocation())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equals(sender.getName())) {

								player.sendMessage("Your town has been transfered to "+args[1]);
								town.owner = Bukkit.getServer().getPlayer(args[1]).getUniqueId().toString();
								return true;

							}

						}

					}

				}
				else if(args[0].equalsIgnoreCase("cap")) {

					for(Town town : this.townList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equals(sender.getName())) {

								if(args[1].equalsIgnoreCase("confirm")) {

									town.growthFrozen = true;
									player.sendMessage(ChatColor.AQUA + "Town growth frozen!");
									return true;

								}
								else {
									player.sendMessage("Are you sure? This cannot be reversed. Type \"/town cap confirm\" to confirm this.");
									return true;
								}

							}

						}

					}

				}
				else if(args[0].equalsIgnoreCase("add")) {

					for(Town town : this.townList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(town.getRank(((Player) sender).getUniqueId().toString()).equals("owner") || town.getRank(((Player) sender).getUniqueId().toString()).equals("manager")) {

								if(args.length > 2) {

									if(args[2].equalsIgnoreCase("citizen")) {

										town.citizens.add(args[1]);
										sender.sendMessage(ChatColor.GREEN + "Citizen added!");

									}
									else if(args[2].equalsIgnoreCase("builder")) {

										town.builders.add(args[1]);
										sender.sendMessage(ChatColor.GREEN + "Builder added!");
										
									}
									else if(args[2].equalsIgnoreCase("manager")) {

										town.managers.add(args[1]);
										sender.sendMessage(ChatColor.GREEN + "Manager added!");
										
									}

									return true;

								}

							}

						}

					}
					for(AdminTown town : this.adminTownList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equals(sender.getName())) {

								if(args.length > 2) {

									if(args[2].equalsIgnoreCase("citizen")) {

										town.citizens.add(args[1]);
										sender.sendMessage(ChatColor.GREEN + "Citizen added!");

									}
									else if(args[2].equalsIgnoreCase("builder")) {

										town.builders.add(args[1]);
										sender.sendMessage(ChatColor.GREEN + "Builder added!");

									}
									else if(args[2].equalsIgnoreCase("manager")) {

										town.managers.add(args[1]);
										sender.sendMessage(ChatColor.GREEN + "Manager added!");

									}

									return true;

								}

							}

						}

					}

				}
				else if(args[0].equalsIgnoreCase("remove")) {

					for(Town town : this.townList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(town.getRank(((Player) sender).getUniqueId().toString()).equals("owner") || town.getRank(((Player) sender).getUniqueId().toString()).equals("manager")) {

								if(args.length > 1 && !args[1].equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

									town.citizens.remove(args[1]);
									town.builders.remove(args[1]);
									town.managers.remove(args[1]);
									
									sender.sendMessage(ChatColor.RED + "Player removed!");
									
									if(town.citizens.size() == 0) {

										town.citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

									}
									if(town.builders.size() == 0) {

										town.builders.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

									}
									if(town.managers.size() == 0) {

										town.managers.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

									}

									return true;

								}

							}
							else {

								for(String manager : town.managers) {

									if(sender.getName().equals(manager)) {

										if(args.length > 1) {

											town.citizens.remove(args[1]);
											town.builders.remove(args[1]);
											town.managers.remove(args[1]);
											
											sender.sendMessage(ChatColor.RED + "Player removed!");
											
											if(town.citizens.size() == 0) {

												town.citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

											}
											if(town.builders.size() == 0) {

												town.builders.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

											}
											if(town.managers.size() == 0) {

												town.managers.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

											}

										}

									}

								}

							}

						}

					}
					for(AdminTown town : this.adminTownList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equalsIgnoreCase(sender.getName())) {

								if(args.length > 1 && !args[1].equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

									town.citizens.remove(args[1]);
									town.builders.remove(args[1]);
									town.managers.remove(args[1]);
									if(town.citizens.size() == 0) {

										town.citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

									}
									if(town.builders.size() == 0) {

										town.builders.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

									}
									if(town.managers.size() == 0) {

										town.managers.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

									}

									return true;

								}

							}
							else {

								for(String manager : town.managers) {

									if(sender.getName().equals(manager)) {

										if(args.length > 1) {

											town.citizens.remove(args[1]);
											town.builders.remove(args[1]);
											town.managers.remove(args[1]);
											if(town.citizens.size() == 0) {

												town.citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

											}
											if(town.builders.size() == 0) {

												town.builders.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

											}
											if(town.managers.size() == 0) {

												town.managers.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

											}

										}

									}

								}

							}

						}

					}

				}
				else if(args[0].equalsIgnoreCase("leave")) {
					
					for(Town town : this.townList) {

						if(town.inTown(((Player) sender).getLocation()) && !sender.getName().equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

							town.citizens.remove(args[1]);
							town.builders.remove(args[1]);
							town.managers.remove(args[1]);
							
							sender.sendMessage(ChatColor.RED + "You have left this town.");
							
							if(town.citizens.size() == 0) {

								town.citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}
							if(town.builders.size() == 0) {

								town.builders.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}
							if(town.managers.size() == 0) {

								town.managers.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}

						}

					}
					for(AdminTown town : this.adminTownList) {

						if(town.inTown(((Player) sender).getLocation()) && !sender.getName().equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

							town.citizens.remove(args[1]);
							town.builders.remove(args[1]);
							town.managers.remove(args[1]);
							
							sender.sendMessage(ChatColor.RED + "You have left this town.");
							
							if(town.citizens.size() == 0) {

								town.citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}
							if(town.builders.size() == 0) {

								town.builders.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}
							if(town.managers.size() == 0) {

								town.managers.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}

						}

					}
					
					return true;

				}
				if(args[0].equalsIgnoreCase("help")) {
					
					sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Town Commands:\n" + ChatColor.RESET + ChatColor.GOLD + 
							"/town info:" + ChatColor.YELLOW + " Lists the info for the town you're currently standing in\n" + ChatColor.GOLD +
							"/town transfer [name]:" + ChatColor.YELLOW + " Transfers the town you're standing in to the specified player\n" + ChatColor.GOLD +
							"/town add [player] [citizen;builder;manager]:" + ChatColor.YELLOW + " Sets the specified player to the specified rank in the town you're standing in\n" + ChatColor.GOLD +
							"/town remove [player]:" + ChatColor.YELLOW + " Removes the specified player from all ranks in the town\n" + ChatColor.GOLD +
							"/town leave:" + ChatColor.YELLOW + " Removes you from all ranks in the town you're standing in\n" + ChatColor.GOLD +
							"/town cap:" + ChatColor.YELLOW + " Halts town growth for the town you're standing in\n" + ChatColor.GOLD +
							"/town rename [name]:" + ChatColor.YELLOW + " Renames the town you're currently standing in");
					return true;
					
				}
				if(args[0].equalsIgnoreCase("info")) {
					
					for(Town town : this.townList) {
					
						if(town.inTown(((Player) sender).getLocation())) {
							
							ArrayList<String> citizens = new ArrayList<String>();
							ArrayList<String> builders = new ArrayList<String>();
							ArrayList<String> managers = new ArrayList<String>();
							
							for(String ID : town.citizens) citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(ID)).getName());
							for(String ID : town.builders) builders.add(Bukkit.getServer().getPlayer(UUID.fromString(ID)).getName());
							for(String ID : town.managers) managers.add(Bukkit.getServer().getPlayer(UUID.fromString(ID)).getName());
							
							sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Town Info\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName() + "\nEnergy: [" + town.energy + "/1000000]" + "\nEnergy: [" + town.stockpileEnergy + "/75000]" + "\nRadius: " + town.radius + "\nCitizens: " + ChatColor.WHITE + citizens + ChatColor.GOLD + "\nBuilders: " + ChatColor.WHITE + builders + ChatColor.GOLD + "\nManagers: " + ChatColor.WHITE + managers);
							return true;
							
						}
						
					}
					
				}

			}

		}
		
		if(cmd.getName().equalsIgnoreCase("town")) {
			
			if(args.length > 0) {
				
				if(args[0].equalsIgnoreCase("leave")) {
					
					for(Town town : this.townList) {

						if(town.inTown(((Player) sender).getLocation()) && !sender.getName().equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

							town.citizens.remove(args[1]);
							town.builders.remove(args[1]);
							town.managers.remove(args[1]);
							
							sender.sendMessage(ChatColor.RED + "You have left this town.");
							
							if(town.citizens.size() == 0) {

								town.citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}
							if(town.builders.size() == 0) {

								town.builders.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}
							if(town.managers.size() == 0) {

								town.managers.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}

						}

					}
					for(AdminTown town : this.adminTownList) {

						if(town.inTown(((Player) sender).getLocation()) && !sender.getName().equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

							town.citizens.remove(args[1]);
							town.builders.remove(args[1]);
							town.managers.remove(args[1]);
							
							sender.sendMessage(ChatColor.RED + "You have left this town.");
							
							if(town.citizens.size() == 0) {

								town.citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}
							if(town.builders.size() == 0) {

								town.builders.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}
							if(town.managers.size() == 0) {

								town.managers.add(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName());

							}

						}

					}
					
					return true;

				}
				if(args[0].equalsIgnoreCase("help")) {
					
					sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Town Commands:\n" + ChatColor.RESET + ChatColor.GOLD + 
							"/town info:" + ChatColor.YELLOW + " Lists the info for the town you're currently standing in\n" + ChatColor.GOLD +
							"/town transfer [name]:" + ChatColor.YELLOW + " Transfers the town you're standing in to the specified player\n" + ChatColor.GOLD +
							"/town add [player] [citizen;builder;manager]:" + ChatColor.YELLOW + " Sets the specified player to the specified rank in the town you're standing in\n" + ChatColor.GOLD +
							"/town remove [player]:" + ChatColor.YELLOW + " Removes the specified player from all ranks in the town\n" + ChatColor.GOLD +
							"/town leave:" + ChatColor.YELLOW + " Removes you from all ranks in the town you're standing in\n" + ChatColor.GOLD +
							"/town cap:" + ChatColor.YELLOW + " Halts town growth for the town you're standing in\n" + ChatColor.GOLD +
							"/town rename [name]:" + ChatColor.YELLOW + " Renames the town you're currently standing in");
					return true;
					
				}
				if(args[0].equalsIgnoreCase("info")) {
					
					for(Town town : this.townList) {
					
						if(town.inTown(((Player) sender).getLocation())) {
							
							ArrayList<String> citizens = new ArrayList<String>();
							ArrayList<String> builders = new ArrayList<String>();
							ArrayList<String> managers = new ArrayList<String>();
							
							for(String ID : town.citizens) citizens.add(Bukkit.getServer().getPlayer(UUID.fromString(ID)).getName());
							for(String ID : town.builders) builders.add(Bukkit.getServer().getPlayer(UUID.fromString(ID)).getName());
							for(String ID : town.managers) managers.add(Bukkit.getServer().getPlayer(UUID.fromString(ID)).getName());
							
							sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Town Info\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName() + "\nEnergy: [" + town.energy + "/1000000]" + "\nEnergy: [" + town.stockpileEnergy + "/75000]" + "\nRadius: " + town.radius + "\nCitizens: " + ChatColor.WHITE + citizens + ChatColor.GOLD + "\nBuilders: " + ChatColor.WHITE + builders + ChatColor.GOLD + "\nManagers: " + ChatColor.WHITE + managers);
							return true;

							
						}
						
					}
					
				}
				
			}
			
		}

		if(cmd.getName().equalsIgnoreCase("plot") || cmd.getName().equalsIgnoreCase("claim") && sender instanceof Player) {

			if(args[0].equalsIgnoreCase("remove")) {

				Location loc = ((Player) sender).getLocation();

				Plot plotToRemove = null;
				for(Plot plot : this.plotList) {

					if(plot.inPlot((int) loc.getX(),(int) loc.getY(),(int) loc.getZ(),loc.getWorld())) {

						if(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName().equals(sender.getName())) {

							
							plotToRemove = plot;
							int logs = (plotToRemove.energy-500)/200;
							while(logs > 0) {
								
								if(logs >= 64) {
									
									plotToRemove.world.dropItemNaturally(new Location(plotToRemove.world,plotToRemove.x,plotToRemove.y,plotToRemove.z), new ItemStack(Material.LOG, 64));
									logs -= 64;
									
								}
								else {
									
									plotToRemove.world.dropItemNaturally(new Location(plotToRemove.world,plotToRemove.x,plotToRemove.y,plotToRemove.z), new ItemStack(Material.LOG, logs));
									logs -= logs;
									
								}
								
							}	
							((Player) sender).sendMessage(ChatColor.RED + "Plot Removed!");
							break;

						}
						else {

							for(Town town : this.townList) {

								if(town.getRank(((Player) sender).getName()).equals("owner") && town.inTown(new Location(plot.world, plot.x, plot.y, plot.z))) {

									plotToRemove = plot;

								}

							}

						}

					}

				}

				if(!(plotToRemove == null)) {

					this.plotList.remove(plotToRemove);
					plotToRemove.hideBorders((Player) sender);
					ItemStack is = new ItemStack(Material.GOLD_NUGGET,9);
					PlayerInventory inv = ((Player) sender).getInventory();
					inv.addItem(is);
					return true;

				}

			}

			if(args[0].equalsIgnoreCase("share")) {

				Location loc = ((Player) sender).getLocation();

				if(args.length > 2) {

					for(Plot plot : this.plotList) {

						if(plot.inPlot((int) loc.getX(),(int) loc.getY(),(int) loc.getZ(),loc.getWorld())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName().equals(sender.getName())) {

								if(args[1].equalsIgnoreCase("add")) {

									plot.residents.add(args[2]);
									((Player) sender).sendMessage(args[2] + " was added to the plot.");
									return true;

								}
								else if(args[1].equalsIgnoreCase("remove")) {

									plot.residents.remove(args[2]);
									((Player) sender).sendMessage(args[2] + " was removed from the plot.");
									return true;

								}

							}

						}

					}

				}

			}

			else if(args[0].equalsIgnoreCase("sell")) {

				Player player = (Player) sender;
				for(Plot plot : this.plotList) {

					if(plot.inPlot((int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ(), player.getLocation().getWorld())) {

						if(plot.getRank(player.getName()).equals("owner")) {

							plot.price = Integer.parseInt(args[1]);
							return true;

						}

					}

				}

			}
			else if(args[0].equalsIgnoreCase("buy")) {

				Player player = (Player) sender;

				for(Plot plot : this.plotList) {

					if(plot.inPlot((int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ(), player.getLocation().getWorld()) && plot.price != 0) {

						EconomyResponse r = econ.withdrawPlayer(player.getName(), plot.price);
						if(r.transactionSuccess()) {

							econ.depositPlayer(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName(),plot.price);
							ArrayList<String> residentsList = new ArrayList<String>();
							residentsList.add(player.getName());
							plot.owner = player.getUniqueId().toString();
							plot.residents = residentsList;
							plot.price = 0;
							player.sendMessage(ChatColor.GREEN+"You have purchased this plot!");
							return true;
							
						}

					}

				}

			}
			else if(args[0].equalsIgnoreCase("upgrade")) {

				for(Plot plot : this.plotList) {

					if(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName().equals(sender.getName())) {

						if(plot.inPlot(((Player)sender).getLocation().getBlockX(), ((Player)sender).getLocation().getBlockY(), ((Player)sender).getLocation().getBlockZ(), ((Player)sender).getLocation().getWorld())) {
							
							EconomyResponse r = econ.withdrawPlayer(sender.getName(), 1500);
							if(r.transactionSuccess()) {
	
								plot.upgraded = true;
								((Player)sender).sendMessage(ChatColor.GREEN+"You have upgraded this plot!");
	
							}
							else {
								
								((Player) sender).sendMessage(ChatColor.RED + "You do not have enough money for that!");
								
							}
							return true;
							
						}

					}

				}

			}
			else if(args[0].equalsIgnoreCase("info")) {
				
				for(Plot plot : this.plotList) {
					
					if(plot.inPlot((int)((Player) sender).getLocation().getX(), (int)((Player) sender).getLocation().getY(), (int)((Player) sender).getLocation().getZ(), ((Player) sender).getLocation().getWorld())) {
						
						String upgraded = "No";
						if(plot.upgraded) upgraded = "Yes";
						
						ArrayList<String> residents = new ArrayList<String>();
						for(String ID : plot.residents) residents.add(Bukkit.getServer().getPlayer(UUID.fromString(ID)).getName());
						
						sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Claim Info\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName() + "\nEnergy: [" + plot.energy + "/50000]" + ChatColor.GREEN + "\nPvE Upgraded: " + upgraded + ChatColor.GOLD + "\nResidents: " + ChatColor.WHITE + residents);
						return true;
						
					}
					
				}
				
			}
			else if(args[0].equalsIgnoreCase("list")) {
				
				for(Plot plot : this.plotList) {
					
					if(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName().equals(sender.getName())) {
						
						String upgraded = "No";
						if(plot.upgraded) upgraded = "Yes";
						sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "(" + plot.x + "," + plot.y + "," + plot.z + ")\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName() + "\nEnergy: [" + plot.energy + "/50000]" + ChatColor.GREEN + "\nPvE Upgraded: " + upgraded + ChatColor.GOLD + "\nResidents: " + ChatColor.WHITE + plot.residents);
						
					}
					
					return true;
					
				}
				
			}
			else if(args[0].equalsIgnoreCase("help")) {
				
				sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Claim Commands:\n" + ChatColor.RESET + ChatColor.GOLD +
						"/claim [on;off;toggle]:" + ChatColor.YELLOW + "Toggles claiming mode\n" + ChatColor.GOLD +
						"/claim info:" + ChatColor.YELLOW + " Displays information about the claim you're currently standing in\n" + ChatColor.GOLD +
						"/claim list:" + ChatColor.YELLOW + " Lists all of the claims you own\n" + ChatColor.GOLD +
						"/claim share [add;remove] [player]:" + ChatColor.YELLOW + " Adds or removes specified player from the claim you're standing in\n" + ChatColor.GOLD +
						"/claim upgrade:" + ChatColor.YELLOW + " Upgrades your claim to protect mobs inside at the cost of $1500 and a regular tax\n" + ChatColor.GOLD +
						"/claim sell [price]:" + ChatColor.YELLOW + " Puts your claim up for sale for the specified price; Doing \"/claim sell 0\" will make it no longer purchaseable\n" + ChatColor.GOLD +
						"/claim buy:" + ChatColor.YELLOW + " Buys the claim you're currently standing in for its listed price if it's for sale\n" + ChatColor.GOLD +
						"/claim remove:" + ChatColor.YELLOW + " Removes the claim you're currently standing in, giving you back nine gold nuggets");	
				return true;
				
			}

		}

		if(cmd.getName().equalsIgnoreCase("test") && sender instanceof Player) {

			Player player = (Player) sender;
			player.sendMessage("The plugin is working");
			return true;

		}

		if(cmd.getName().equalsIgnoreCase("claim") && sender instanceof Player) {

			Player player = (Player) sender;
			int length = args.length;
			FileConfiguration config = this.getConfig();
			if(!config.contains(player.getName())) {

				config.set(player.getName(), false);

			}

			if(length >= 1) {

				if(args[0].equalsIgnoreCase("on")) {

					config.set(player.getName(), true);
					player.sendMessage("Claiming mode on");
					for(Plot plot : this.plotList) {

						plot.showBorders((Player) sender);

					}
					return true;

				}
				else if(args[0].equalsIgnoreCase("off")) {

					config.set(player.getName(), false);
					player.sendMessage("Claiming mode off");
					for(Plot plot : this.plotList) {

						plot.hideBorders((Player) sender);

					}
					for(DungeonClaim dungeon : this.dungeonList) {
						
						dungeon.hideBorders((Player) sender);
						
					}
					//					for(Town town : this.townList) {
					//						
					//						town.hideBorders((Player) sender);
					//						
					//					}
					return true;

				}
				else if(args[0].equalsIgnoreCase("toggle")) {

					config.set(player.getName(), !config.getBoolean(player.getName()));
					if(config.getBoolean(player.getName())) {

						player.sendMessage("Claiming mode on");
						for(Plot plot : this.plotList) {

							plot.showBorders((Player) sender);

						}
						//						for(Town town : this.townList) {
						//							
						//							town.showBorders((Player) sender);
						//							
						//						}

					}
					else {

						player.sendMessage("Claiming mode off");
						for(Plot plot : this.plotList) {

							plot.hideBorders((Player) sender);

						}
						//						for(Town town : this.townList) {
						//							
						//							town.hideBorders((Player) sender);
						//							
						//						}

					}
					return true;

				}

			}

		}
		
		if(cmd.getName().equalsIgnoreCase("admintown") && sender instanceof Player) {

			if(sender.isOp()) {

				Town townToRemove = null;
				for(Town town : this.townList) {

					if(town.inTown(((Player) sender).getLocation())) {

						townToRemove = town;
						this.adminTownList.add(new AdminTown(town.name, town.owner, town.owner, town.owner, town.owner, town.world.getName()+","+town.x+","+town.y+","+town.z, args[0]));

					}

				}
				this.townList.remove(townToRemove);

			}
			return true;

		}
		
		if(cmd.getName().equalsIgnoreCase("dungeon") && sender.isOp()) {
			
			if(args.length == 7 && args[0].equalsIgnoreCase("claim")) {
				
				Location loc = new Location(((Player) sender).getWorld(), (double) Integer.parseInt(args[1]), (double) Integer.parseInt(args[2]), (double) Integer.parseInt(args[3]));
				if(loc.getX() > Integer.parseInt(args[4]) || loc.getY() > Integer.parseInt(args[5]) || loc.getZ() > Integer.parseInt(args[6])) {
					
					sender.sendMessage(ChatColor.RED + "The first location must be more negative than the second in all dimensions.");
					return false;
					
				}
				dungeonList.add(new DungeonClaim(loc, Integer.parseInt(args[4]) - ((int) loc.getX()), Integer.parseInt(args[6]) - ((int) loc.getZ()), Integer.parseInt(args[5]) - ((int) loc.getY()), "", ""));
				sender.sendMessage(ChatColor.GREEN + "Dungeon Created");
				return true;
				
			}
			else if(args.length == 2 && args[0].equalsIgnoreCase("add")) {
				
				if(args[1].equalsIgnoreCase("place")) {
					
					for(DungeonClaim dungeon : dungeonList) {
						
						if(dungeon.inClaim(((Player) sender).getLocation())) {
							
							Material newMaterial = ((Player) sender).getInventory().getItemInMainHand().getType();
							if(newMaterial == Material.DIODE) {
								
								dungeon.placeList.add(Material.DIODE_BLOCK_OFF);
								dungeon.placeList.add(Material.DIODE_BLOCK_ON);
								
							}
							else if(newMaterial == Material.REDSTONE) {
								
								dungeon.placeList.add(Material.REDSTONE_WIRE);
								
							}
							else if(newMaterial == Material.REDSTONE_COMPARATOR) {
								
								dungeon.placeList.add(Material.REDSTONE_COMPARATOR_OFF);
								dungeon.placeList.add(Material.REDSTONE_COMPARATOR_ON);
								
							}
							dungeon.placeList.add(newMaterial);
							sender.sendMessage(ChatColor.GREEN + "Material Added");
							return true;
							
						}
						
					}
					
				}
				else if(args[1].equalsIgnoreCase("break")) {
					
					for(DungeonClaim dungeon : dungeonList) {
						
						if(dungeon.inClaim(((Player) sender).getLocation())) {
							
							Material newMaterial = ((Player) sender).getInventory().getItemInMainHand().getType();
							if(newMaterial == Material.DIODE) {
								
								dungeon.breakList.add(Material.DIODE_BLOCK_OFF);
								dungeon.breakList.add(Material.DIODE_BLOCK_ON);
								
							}
							else if(newMaterial == Material.REDSTONE) {
								
								dungeon.breakList.add(Material.REDSTONE_WIRE);
								
							}
							else if(newMaterial == Material.REDSTONE_COMPARATOR) {
								
								dungeon.breakList.add(Material.REDSTONE_COMPARATOR_OFF);
								dungeon.breakList.add(Material.REDSTONE_COMPARATOR_ON);
								
							}
							dungeon.breakList.add(newMaterial);
							sender.sendMessage(ChatColor.GREEN + "Material Added");
							return true;
							
						}
						
					}
					
				}
				
			}
			else if(args.length == 2 && args[0].equalsIgnoreCase("remove")) {
				
				if(args[1].equalsIgnoreCase("place")) {
					
					for(DungeonClaim dungeon : dungeonList) {
						
						if(dungeon.inClaim(((Player) sender).getLocation())) {
							
							Material newMaterial = ((Player) sender).getInventory().getItemInMainHand().getType();
							if(newMaterial == Material.DIODE) {
								
								dungeon.placeList.remove(Material.DIODE_BLOCK_OFF);
								dungeon.placeList.remove(Material.DIODE_BLOCK_ON);
								
							}
							else if(newMaterial == Material.REDSTONE) {
								
								dungeon.placeList.remove(Material.REDSTONE_WIRE);
								
							}
							else if(newMaterial == Material.REDSTONE_COMPARATOR) {
								
								dungeon.placeList.remove(Material.REDSTONE_COMPARATOR_OFF);
								dungeon.placeList.remove(Material.REDSTONE_COMPARATOR_ON);
								
							}
							dungeon.placeList.remove(newMaterial);
							sender.sendMessage(ChatColor.RED + "Material Removed");
							return true;
							
						}
						
					}
					
				}
				else if(args[1].equalsIgnoreCase("break")) {
					
					for(DungeonClaim dungeon : dungeonList) {
						
						if(dungeon.inClaim(((Player) sender).getLocation())) {
							
							Material newMaterial = ((Player) sender).getInventory().getItemInMainHand().getType();
							if(newMaterial == Material.DIODE) {
								
								dungeon.breakList.remove(Material.DIODE_BLOCK_OFF);
								dungeon.breakList.remove(Material.DIODE_BLOCK_ON);
								
							}
							else if(newMaterial == Material.REDSTONE) {
								
								dungeon.breakList.remove(Material.REDSTONE_WIRE);
								
							}
							else if(newMaterial == Material.REDSTONE_COMPARATOR) {
								
								dungeon.breakList.remove(Material.REDSTONE_COMPARATOR_OFF);
								dungeon.breakList.remove(Material.REDSTONE_COMPARATOR_ON);
								
							}
							dungeon.breakList.remove(newMaterial);
							sender.sendMessage(ChatColor.RED + "Material Removed");
							return true;
							
						}
						
					}
					
				}
				
			}
			
		}
		
		if(cmd.getName().equalsIgnoreCase("web") && sender instanceof Player) {
			
			sender.sendMessage(ChatColor.YELLOW + "Here's our website:\n" + ChatColor.AQUA + "minelife-community.weebly.com");
			return true;
			
		}
		
		if(cmd.getName().equalsIgnoreCase("minelife")) {
			
			sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "MineLife Help:\n" + ChatColor.RESET + ChatColor.AQUA + "/town help\n/claim help");
			return true;
			
		}
		
		if(cmd.getName().equalsIgnoreCase("t") && sender instanceof Player) {
				
			sender.sendMessage("The plugin is working!");
			return true;
			
		}
		
		return false;

	}*/

	public void removeInventoryItems(PlayerInventory inv, Material type, int amount) {
		for (ItemStack is : inv.getContents()) {
			if (is != null && is.getType() == type) {
				int newamount = is.getAmount() - amount;
				if (newamount > 0) {
					is.setAmount(newamount);
					break;
				} else {
					inv.remove(is);
					amount = -newamount;
					if (amount == 0) break;
				}
			}
		}
	}

	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    private String removeLastChar(String str) {
    	
    	if(str != null && str.length() > 0) {
    		
    		return str.substring(0,str.length()-1);
    		
    	}
    	else {
    		
    		return "";
    		
    	}
    	
    }

}
