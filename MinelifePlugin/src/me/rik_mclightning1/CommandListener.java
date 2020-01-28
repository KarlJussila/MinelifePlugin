package me.rik_mclightning1;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.milkbowl.vault.economy.EconomyResponse;

public class CommandListener implements CommandExecutor {

	private final Minelife MainPlugin;

	public CommandListener(Minelife MainPlugin) {

		this.MainPlugin = MainPlugin;

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("town") && sender instanceof Player) {

			Player player = (Player) sender;
			int length = args.length;

			if(length > 1) {

				if(args[0].equalsIgnoreCase("rename")) {

					for(Town town : MainPlugin.townList) {

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
					for(AdminTown town : MainPlugin.adminTownList) {

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

					for(Town town : MainPlugin.townList) {

						if(town.inTown(player.getLocation())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equals(sender.getName())) {

								player.sendMessage("Your town has been transfered to "+args[1]);
								town.owner = Bukkit.getServer().getPlayer(args[1]).getUniqueId().toString();
								return true;

							}

						}

					}
					for(AdminTown town : MainPlugin.adminTownList) {

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

					for(Town town : MainPlugin.townList) {

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

					for(Town town : MainPlugin.townList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(town.getRank(((Player) sender).getUniqueId().toString()).equals("owner") || town.getRank(((Player) sender).getUniqueId().toString()).equals("manager")) {

								if(args.length > 2) {

									String ID = "";
									for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {

										if(onlinePlayer.getName().equalsIgnoreCase(args[1])) ID = onlinePlayer.getUniqueId().toString();

									}
									if(ID.equalsIgnoreCase("")) {

										sender.sendMessage(ChatColor.RED + "That player is offline.");
										return true;

									}

									if(args[2].equalsIgnoreCase("citizen")) {

										town.citizens.add(ID);
										sender.sendMessage(ChatColor.GREEN + "Citizen added!");

									}
									else if(args[2].equalsIgnoreCase("builder")) {

										town.builders.add(ID);
										sender.sendMessage(ChatColor.GREEN + "Builder added!");

									}
									else if(args[2].equalsIgnoreCase("manager")) {

										town.managers.add(ID);
										sender.sendMessage(ChatColor.GREEN + "Manager added!");

									}

									return true;

								}

							}

						}

					}
					for(AdminTown town : MainPlugin.adminTownList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(town.owner.equals(((Player) sender).getUniqueId().toString())) {

								if(args.length > 2) {

									String ID = "";
									for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {

										if(onlinePlayer.getName().equalsIgnoreCase(args[1])) ID = onlinePlayer.getUniqueId().toString();

									}
									if(ID.equalsIgnoreCase("")) {

										sender.sendMessage(ChatColor.RED + "That player is offline.");
										return true;

									}

									if(args[2].equalsIgnoreCase("citizen")) {

										town.citizens.add(ID);
										sender.sendMessage(ChatColor.GREEN + "Citizen added!");

									}
									else if(args[2].equalsIgnoreCase("builder")) {

										town.builders.add(ID);
										sender.sendMessage(ChatColor.GREEN + "Builder added!");

									}
									else if(args[2].equalsIgnoreCase("manager")) {

										town.managers.add(ID);
										sender.sendMessage(ChatColor.GREEN + "Manager added!");

									}

									return true;

								}

							}

						}

					}

				}
				else if(args[0].equalsIgnoreCase("remove")) {

					for(Town town : MainPlugin.townList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(args.length > 1 && !args[1].equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

								if(town.getRank(((Player) sender).getUniqueId().toString()).equals("owner")) {

									String ID = "";
									for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {

										if(onlinePlayer.getName().equalsIgnoreCase(args[1])) ID = onlinePlayer.getUniqueId().toString();

									}
									if(!ID.equalsIgnoreCase("")) {

										for(OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {

											if(offlinePlayer.getName().equalsIgnoreCase(args[1])) ID = offlinePlayer.getUniqueId().toString();

										}

									}
									if(ID.equalsIgnoreCase("")) {

										sender.sendMessage(ChatColor.RED + "That player could not be found.");
										return true;

									}

									town.citizens.remove(ID);
									town.builders.remove(ID);
									town.managers.remove(ID);

									sender.sendMessage(ChatColor.RED + "Player removed!");

									if(town.citizens.size() == 0) {

										town.citizens.add(town.owner);

									}
									if(town.builders.size() == 0) {

										town.builders.add(town.owner);

									}
									if(town.managers.size() == 0) {

										town.managers.add(town.owner);

									}

									return true;

								}
								else {

									for(String manager : town.managers) {

										if(((Player) sender).getUniqueId().equals(manager)) {

											String ID = "";
											for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {

												if(onlinePlayer.getName().equalsIgnoreCase(args[1])) ID = onlinePlayer.getUniqueId().toString();

											}
											if(!ID.equalsIgnoreCase("")) {

												for(OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {

													if(offlinePlayer.getName().equalsIgnoreCase(args[1])) ID = offlinePlayer.getUniqueId().toString();

												}

											}
											if(ID.equalsIgnoreCase("")) {

												sender.sendMessage(ChatColor.RED + "That player could not be found.");
												return true;

											}

											if(args.length > 1) {

												town.citizens.remove(ID);
												town.builders.remove(ID);

												sender.sendMessage(ChatColor.RED + "Player removed!");

												if(town.citizens.size() == 0) {

													town.citizens.add(town.owner);

												}
												if(town.builders.size() == 0) {

													town.builders.add(town.owner);

												}
												if(town.managers.size() == 0) {

													town.managers.add(town.owner);

												}

											}

										}

									}

								}
								
							}

						}

					}
					for(AdminTown town : MainPlugin.adminTownList) {

						if(town.inTown(((Player) sender).getLocation())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName().equalsIgnoreCase(sender.getName())) {

								if(args.length > 1 && !args[1].equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

									String ID = "";
									for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {

										if(onlinePlayer.getName().equalsIgnoreCase(args[1])) ID = onlinePlayer.getUniqueId().toString();

									}
									if(!ID.equalsIgnoreCase("")) {

										for(OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {

											if(offlinePlayer.getName().equalsIgnoreCase(args[1])) ID = offlinePlayer.getUniqueId().toString();

										}

									}
									if(ID.equalsIgnoreCase("")) {

										sender.sendMessage(ChatColor.RED + "That player could not be found.");
										return true;

									}
									
									town.citizens.remove(ID);
									town.builders.remove(ID);
									town.managers.remove(ID);
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

										String ID = "";
										for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {

											if(onlinePlayer.getName().equalsIgnoreCase(args[1])) ID = onlinePlayer.getUniqueId().toString();

										}
										if(!ID.equalsIgnoreCase("")) {

											for(OfflinePlayer offlinePlayer : Bukkit.getServer().getOfflinePlayers()) {

												if(offlinePlayer.getName().equalsIgnoreCase(args[1])) ID = offlinePlayer.getUniqueId().toString();

											}

										}
										if(ID.equalsIgnoreCase("")) {

											sender.sendMessage(ChatColor.RED + "That player could not be found.");
											return true;

										}
										
										if(args.length > 1) {

											town.citizens.remove(ID);
											town.builders.remove(ID);
											if(town.citizens.size() == 0) {

												town.citizens.add(town.owner);

											}
											if(town.builders.size() == 0) {

												town.builders.add(town.owner);

											}
											if(town.managers.size() == 0) {

												town.managers.add(town.owner);

											}

										}

									}

								}

							}

						}

					}

				}
				else if(args[0].equalsIgnoreCase("leave")) {

					for(Town town : MainPlugin.townList) {

						if(town.inTown(((Player) sender).getLocation()) && !sender.getName().equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

							town.citizens.remove(((Player) sender).getUniqueId());
							town.builders.remove(((Player) sender).getUniqueId());
							town.managers.remove(((Player) sender).getUniqueId());

							sender.sendMessage(ChatColor.RED + "You have left this town.");

							if(town.citizens.size() == 0) {

								town.citizens.add(town.owner);

							}
							if(town.builders.size() == 0) {

								town.builders.add(town.owner);

							}
							if(town.managers.size() == 0) {

								town.managers.add(town.owner);

							}

						}

					}
					for(AdminTown town : MainPlugin.adminTownList) {

						if(town.inTown(((Player) sender).getLocation()) && !sender.getName().equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

							String ID = ((Player) sender).getUniqueId().toString();
							
							town.citizens.remove(ID);
							town.builders.remove(ID);
							town.managers.remove(ID);

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

					for(Town town : MainPlugin.townList) {

						if(town.inTown(((Player) sender).getLocation())) {

							ArrayList<String> citizens = new ArrayList<String>();
							ArrayList<String> builders = new ArrayList<String>();
							ArrayList<String> managers = new ArrayList<String>();

							for(String ID : town.citizens) {
								
								boolean found = false;
								
								for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
									
									if(onlinePlayer.getUniqueId().toString().equals(ID)) {
										
										citizens.add(onlinePlayer.getName());
										found = true;
										break;
										
									}
									
								}
								if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
									
									if(offlinePlayer.getUniqueId().toString().equals(ID)) {
										
										citizens.add(offlinePlayer.getName());
										break;
										
									}
									
								}
								
							}
							
							for(String ID : town.builders) {
								
								boolean found = false;
							
								for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
									
									if(onlinePlayer.getUniqueId().toString().equals(ID)) {
										
										builders.add(onlinePlayer.getName());
										found = true;
										break;
										
									}
									
								}
								if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
									
									if(offlinePlayer.getUniqueId().toString().equals(ID)) {
										
										builders.add(offlinePlayer.getName());
										break;
										
									}
									
								}
							
							}
							
							for(String ID : town.managers) {
								
								boolean found = false;
								
								for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
									
									if(onlinePlayer.getUniqueId().toString().equals(ID)) {
										
										managers.add(onlinePlayer.getName());
										found = true;
										break;
										
									}
									
								}
								if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
									
									if(offlinePlayer.getUniqueId().toString().equals(ID)) {
										
										managers.add(offlinePlayer.getName());
										break;
										
									}
									
								}
								
							}

							String townOwner = "";
							boolean found = false;
							for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
								
								if(onlinePlayer.getUniqueId().toString().equals(town.owner)) {
									
									townOwner = onlinePlayer.getName();
									found = true;
									break;
									
								}
								
							}
							if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) if(offlinePlayer.getUniqueId().equals(town.owner)) {
								
								townOwner = offlinePlayer.getName();
								break;
								
							}
							
							
							sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Town Info\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + townOwner + "\nEnergy: [" + town.energy + "/1000000]" + "\nEnergy: [" + town.stockpileEnergy + "/75000]" + "\nRadius: " + town.radius + "\nCitizens: " + ChatColor.WHITE + citizens + ChatColor.GOLD + "\nBuilders: " + ChatColor.WHITE + builders + ChatColor.GOLD + "\nManagers: " + ChatColor.WHITE + managers);
							return true;

						}

					}

				}

			}

		}

		if(cmd.getName().equalsIgnoreCase("town")) {

			if(args.length > 0) {

				if(args[0].equalsIgnoreCase("leave")) {

					for(Town town : MainPlugin.townList) {

						if(town.inTown(((Player) sender).getLocation()) && !sender.getName().equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

							String ID = ((Player) sender).getUniqueId().toString();
							
							town.citizens.remove(ID);
							town.builders.remove(ID);
							town.managers.remove(ID);

							sender.sendMessage(ChatColor.RED + "You have left this town.");

							if(town.citizens.size() == 0) {

								town.citizens.add(town.owner);

							}
							if(town.builders.size() == 0) {

								town.builders.add(town.owner);

							}
							if(town.managers.size() == 0) {

								town.managers.add(town.owner);

							}

						}

					}
					for(AdminTown town : MainPlugin.adminTownList) {

						if(town.inTown(((Player) sender).getLocation()) && !sender.getName().equals(Bukkit.getServer().getPlayer(UUID.fromString(town.owner)).getName())) {

							String ID = ((Player) sender).getUniqueId().toString();
							
							town.citizens.remove(ID);
							town.builders.remove(ID);
							town.managers.remove(ID);

							sender.sendMessage(ChatColor.RED + "You have left this town.");

							if(town.citizens.size() == 0) {

								town.citizens.add(town.owner);

							}
							if(town.builders.size() == 0) {

								town.builders.add(town.owner);

							}
							if(town.managers.size() == 0) {

								town.managers.add(town.owner);

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

					for(Town town : MainPlugin.townList) {

						if(town.inTown(((Player) sender).getLocation())) {

							ArrayList<String> citizens = new ArrayList<String>();
							ArrayList<String> builders = new ArrayList<String>();
							ArrayList<String> managers = new ArrayList<String>();

							for(String ID : town.citizens) {
								
								boolean found = false;
								for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
									
									if(onlinePlayer.getUniqueId().toString().equals(ID)) {
										
										citizens.add(onlinePlayer.getName());
										found = true;
										break;
										
									}
									
								}
								if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
									
									if(offlinePlayer.getUniqueId().toString().equals(ID)) {
										
										citizens.add(offlinePlayer.getName());
										break;
										
									}
									
								}
								
							}
							
							for(String ID : town.builders) {
								
								boolean found = false;
								for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
									
									if(onlinePlayer.getUniqueId().toString().equals(ID)) {
										
										builders.add(onlinePlayer.getName());
										found = true;
										break;
										
									}
									
								}
								if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
									
									if(offlinePlayer.getUniqueId().toString().equals(ID)) {
										
										builders.add(offlinePlayer.getName());
										break;
										
									}
									
								}
								
							}
							
							for(String ID : town.managers) {
								
								boolean found = false;
								for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
									
									if(onlinePlayer.getUniqueId().toString().equals(ID)) {
										
										managers.add(onlinePlayer.getName());
										found = true;
										break;
										
									}
									
								}
								if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
									
									if(offlinePlayer.getUniqueId().toString().equals(ID)) {
										
										managers.add(offlinePlayer.getName());
										break;
										
									}
									
								}
								
							}
							
							String townOwner = "";
							boolean found = false;
							for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
								
								if(town.owner.equals(onlinePlayer.getUniqueId().toString())) {
									
									townOwner = onlinePlayer.getName();
									found = true;
									break;
									
								}
								
							}
							if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
								
								if(town.owner.equals(offlinePlayer.getUniqueId().toString())) {
									
									townOwner = offlinePlayer.getName();
									break;
									
								}
								
							}
							
							sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Town Info\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + townOwner + "\nEnergy: [" + town.energy + "/1000000]" + "\nEnergy: [" + town.stockpileEnergy + "/75000]" + "\nRadius: " + town.radius + "\nCitizens: " + ChatColor.WHITE + citizens + ChatColor.GOLD + "\nBuilders: " + ChatColor.WHITE + builders + ChatColor.GOLD + "\nManagers: " + ChatColor.WHITE + managers);
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
				for(Plot plot : MainPlugin.plotList) {

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

							for(Town town : MainPlugin.townList) {

								if(town.getRank(((Player) sender).getName()).equals("owner") && town.inTown(new Location(plot.world, plot.x, plot.y, plot.z))) {

									plotToRemove = plot;

								}

							}

						}

					}

				}

				if(!(plotToRemove == null)) {

					MainPlugin.plotList.remove(plotToRemove);
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

					for(Plot plot : MainPlugin.plotList) {

						if(plot.inPlot((int) loc.getX(),(int) loc.getY(),(int) loc.getZ(),loc.getWorld())) {

							if(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName().equals(sender.getName())) {

								if(args[1].equalsIgnoreCase("add")) {

									String ID = "";
									for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
										
										if(onlinePlayer.getName().equalsIgnoreCase(args[2])) ID = onlinePlayer.getUniqueId().toString();
										
									}
									if(ID.equalsIgnoreCase("")) {
										
										sender.sendMessage(ChatColor.RED + "Could not find player.");
										return true;
										
									}
									
									plot.residents.add(ID);
									((Player) sender).sendMessage(args[2] + " was added to the plot.");
									return true;

								}
								else if(args[1].equalsIgnoreCase("remove")) {

									String ID = "";
									for(Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
										
										if(onlinePlayer.getName().equalsIgnoreCase(args[2])) ID = onlinePlayer.getUniqueId().toString();
										
									}
									if(ID.equalsIgnoreCase("")) {
										
										sender.sendMessage(ChatColor.RED + "Could not find player.");
										return true;
										
									}
									
									plot.residents.remove(ID);
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
				for(Plot plot : MainPlugin.plotList) {

					if(plot.inPlot((int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ(), player.getLocation().getWorld())) {

						if(plot.getRank(player.getUniqueId().toString()).equals("owner")) {

							plot.price = Integer.parseInt(args[1]);
							return true;

						}

					}

				}

			}
			else if(args[0].equalsIgnoreCase("buy")) {

				Player player = (Player) sender;

				for(Plot plot : MainPlugin.plotList) {

					if(plot.inPlot((int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ(), player.getLocation().getWorld()) && plot.price != 0) {

						Player onlinePlotOwner = null;
						OfflinePlayer offlinePlotOwner = null;
						boolean found = false;
						for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
							
							if(onlinePlayer.getUniqueId().toString().equals(plot.owner)) {
								
								onlinePlotOwner = onlinePlayer;
								found = true;
								break;
								
							}
							
						}
						if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
							
							if(offlinePlayer.getUniqueId().toString().equalsIgnoreCase(plot.owner)) {
								
								offlinePlotOwner = offlinePlayer;
								break;
								
							}
							
						}
						
						EconomyResponse r = MainPlugin.econ.withdrawPlayer(player.getName(), plot.price);
						if(r.transactionSuccess()) {

							if(onlinePlotOwner != null) {
								
								MainPlugin.econ.depositPlayer(onlinePlotOwner,plot.price);
								
							}
							else {
								
								MainPlugin.econ.depositPlayer(offlinePlotOwner,plot.price);
								
							}
							ArrayList<String> residentsList = new ArrayList<String>();
							residentsList.add(player.getUniqueId().toString());
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

				for(Plot plot : MainPlugin.plotList) {

					if(Bukkit.getServer().getPlayer(UUID.fromString(plot.owner)).getName().equals(sender.getName())) {

						if(plot.inPlot(((Player)sender).getLocation().getBlockX(), ((Player)sender).getLocation().getBlockY(), ((Player)sender).getLocation().getBlockZ(), ((Player)sender).getLocation().getWorld())) {

							EconomyResponse r = MainPlugin.econ.withdrawPlayer(sender.getName(), 1500);
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

				for(Plot plot : MainPlugin.plotList) {

					if(plot.inPlot((int)((Player) sender).getLocation().getX(), (int)((Player) sender).getLocation().getY(), (int)((Player) sender).getLocation().getZ(), ((Player) sender).getLocation().getWorld())) {

						String upgraded = "No";
						if(plot.upgraded) upgraded = "Yes";

						ArrayList<String> residents = new ArrayList<String>();
						for(String ID : plot.residents) {
							
							boolean found = false;
							
							for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
								
								if(onlinePlayer.getUniqueId().toString().equals(ID)) {
									
									residents.add(onlinePlayer.getName());
									found = true;
									break;
									
								}
								
							}
							if(found) continue;
							for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
								
								if(offlinePlayer.getUniqueId().toString().equals(ID)) {
									
									residents.add(offlinePlayer.getName());
									break;
									
								}
								
							}
							
						}
						
						String plotOwner = "";
						boolean found = false;
						for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
							
							if(onlinePlayer.getUniqueId().toString().equals(plot.owner)) {
								
								plotOwner = onlinePlayer.getName();
								found = true;
								break;
								
							}
							
						}
						if(!found) for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
							
							if(offlinePlayer.getUniqueId().toString().equals(plot.owner)) {
							
								plotOwner = offlinePlayer.getName();
								break;
								
							}
							
						}

						sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Claim Info\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + plotOwner + "\nEnergy: [" + plot.energy + "/50000]" + ChatColor.GREEN + "\nPvE Upgraded: " + upgraded + ChatColor.GOLD + "\nResidents: " + ChatColor.WHITE + residents);
						return true;

					}

				}

			}
			else if(args[0].equalsIgnoreCase("list")) {

				for(Plot plot : MainPlugin.plotList) {

					if(((Player) sender).getUniqueId().toString().equals(plot.owner)) {

						String upgraded = "No";
						if(plot.upgraded) upgraded = "Yes";

						ArrayList<String> residents = new ArrayList<String>();
						for(String ID : plot.residents) {
							
							boolean found = false;
							
							for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
								
								if(onlinePlayer.getUniqueId().toString().equals(ID)) {
									
									residents.add(onlinePlayer.getName());
									found = true;
									break;
									
								}
								
							}
							if(found) continue;
							for(OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
								
								if(offlinePlayer.getUniqueId().toString().equals(ID)) {
									
									residents.add(offlinePlayer.getName());
									break;
									
								}
								
							}
							
						}
						
						sender.sendMessage(ChatColor.RED + plot.world.getName() + ": " + ChatColor.BLUE + "" + ChatColor.BOLD + "(" + plot.x + "," + plot.y + "," + plot.z + ")\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + sender.getName() + "\nEnergy: [" + plot.energy + "/50000]" + ChatColor.GREEN + "\nPvE Upgraded: " + upgraded + ChatColor.GOLD + "\nResidents: " + ChatColor.WHITE + residents);

					}

				}
				
				return true;

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
			FileConfiguration config = MainPlugin.getConfig();
			if(!config.contains(player.getName())) {

				config.set(player.getName(), false);

			}

			if(length >= 1) {

				if(args[0].equalsIgnoreCase("on")) {

					config.set(player.getName(), true);
					player.sendMessage("Claiming mode on");
					for(Plot plot : MainPlugin.plotList) {

						plot.showBorders((Player) sender);

					}
					return true;

				}
				else if(args[0].equalsIgnoreCase("off")) {

					config.set(player.getName(), false);
					player.sendMessage("Claiming mode off");
					for(Plot plot : MainPlugin.plotList) {

						plot.hideBorders((Player) sender);

					}
					if(!MainPlugin.dungeonList.isEmpty()) {
						for(DungeonClaim dungeon : MainPlugin.dungeonList) {
	
							dungeon.hideBorders((Player) sender);
	
						}
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
						for(Plot plot : MainPlugin.plotList) {

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
						for(Plot plot : MainPlugin.plotList) {

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
				for(Town town : MainPlugin.townList) {

					if(town.inTown(((Player) sender).getLocation())) {

						townToRemove = town;
						MainPlugin.adminTownList.add(new AdminTown(town.name, town.owner, town.owner, town.owner, town.owner, town.world.getName()+","+town.x+","+town.y+","+town.z, args[0]));

					}

				}
				MainPlugin.townList.remove(townToRemove);

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
				MainPlugin.dungeonList.add(new DungeonClaim(loc, Integer.parseInt(args[4]) - ((int) loc.getX()), Integer.parseInt(args[6]) - ((int) loc.getZ()), Integer.parseInt(args[5]) - ((int) loc.getY()), "", ""));
				sender.sendMessage(ChatColor.GREEN + "Dungeon Created");
				return true;

			}
			else if(args.length == 2 && args[0].equalsIgnoreCase("add")) {

				if(args[1].equalsIgnoreCase("place")) {

					for(DungeonClaim dungeon : MainPlugin.dungeonList) {

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

					for(DungeonClaim dungeon : MainPlugin.dungeonList) {

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

					for(DungeonClaim dungeon : MainPlugin.dungeonList) {

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

					for(DungeonClaim dungeon : MainPlugin.dungeonList) {

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
		
		if(cmd.getName().equalsIgnoreCase("setradius")) {
			
			if(sender.isOp() && args.length == 1) {
				
				for(AdminTown town : MainPlugin.adminTownList) {
					
					if(town.inTown(((Player) sender).getLocation())) {
						
						town.radius = Integer.parseInt(args[0]);
						return true;
						
					}
					
				}
				
			}
			
		}
		
		if(cmd.getName().equalsIgnoreCase("mute")) {
			
			if(sender.isOp() && args.length == 2) {
				
				MainPlugin.muteList.add(args[0]);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
					public void run() {
						MainPlugin.muteList.remove(args[0]);
					}
				}, Integer.parseInt(args[1])*1200);
				return true;
				
			}
			
		}
		if(cmd.getName().equalsIgnoreCase("unmute")) {
			
			if(sender.isOp() && args.length == 1) {
				
				MainPlugin.muteList.remove(args[0]);
				return true;
				
			}
			
		}
		if(cmd.getName().equalsIgnoreCase("bal") || cmd.getName().equalsIgnoreCase("money") || cmd.getName().equalsIgnoreCase("balance")) {
			
			sender.sendMessage(ChatColor.DARK_GREEN + "Balance: " + ChatColor.RESET + "$" + MainPlugin.econ.getBalance(((Player) sender).getName()));
			return true;
			
		}

		return false;

	}

}
