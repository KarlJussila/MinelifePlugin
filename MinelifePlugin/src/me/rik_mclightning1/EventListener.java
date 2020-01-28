package me.rik_mclightning1;

import java.util.ArrayList;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class EventListener implements Listener {

	Minelife MainPlugin;
	FileConfiguration config;

	public EventListener(Minelife plugin) {

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		MainPlugin = plugin;

	}

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent e) {

		boolean cancel = false;
		
		//TOWN HANDLING
		for(Town town : MainPlugin.townList) {

			if(town.inTown(e.getIgnitingBlock().getLocation())) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//CITIZEN
				else if(playerRank.equals("citizen")) {

					cancel = true;

				}
				//CITIZEN//

			}

		}
		//TOWN HANDLING//

		//ADMIN TOWN HANDLING//
		for(AdminTown town : MainPlugin.adminTownList) {

			if(town.inTown(e.getIgnitingBlock().getLocation())) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());

				//NO RANK
				if(playerRank.equals("none") || playerRank.equals("citizen")) {

					cancel = true;
					break;

				}
				//NO RANK//

			}

		}
		//ADMIN TOWN HANDLING//

		//PLOT HANDLING
		for(Plot plot : MainPlugin.plotList) {

			if(plot.inPlot(e.getIgnitingBlock().getX(), e.getIgnitingBlock().getY(), e.getIgnitingBlock().getZ(),e.getIgnitingBlock().getWorld())) {

				String playerRank = plot.getRank(e.getPlayer().getUniqueId().toString());

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//RESIDENT AND OWNER
				else if(playerRank.equals("resident") || playerRank.equals("owner")) {

					if(plot.getRank(e.getPlayer().getUniqueId().toString()).equals("none")) {

						cancel = true;

					}
					else {

						cancel = false;

					}

					break;

				}
				//RESIDENT AND OWNER//

			}

		}
		//PLOT HANDLING//

		//DUNGEON HANDLING//
		for(DungeonClaim dungeon : MainPlugin.dungeonList) {

			if(dungeon.inClaim(e.getIgnitingBlock().getLocation())) {

				if(!e.getPlayer().isOp()) {
					
					e.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "A mysterious force prevents you from doing this.");
					e.setCancelled(true);
					return;
					
				}

			}

		}
		//DUNGEON HANDLING//
		
		//CANCELLING
		if(cancel) e.getPlayer().sendMessage(ChatColor.RED + "You cannot ignite here.");
		e.setCancelled(cancel);
		//CANCELLING

	}

	///////////////////////
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e) {

		boolean cancel = false;
		
		Location loc2;
		BlockFace face = e.getBlockFace();
		if(face == BlockFace.UP) {
			
			loc2 = new Location(e.getBlockClicked().getWorld(), (double) e.getBlockClicked().getX(), (double) e.getBlockClicked().getY() + 1, e.getBlockClicked().getZ());
			
		}
		else if(face == BlockFace.DOWN) {
			
			loc2 = new Location(e.getBlockClicked().getWorld(), (double) e.getBlockClicked().getX(), (double) e.getBlockClicked().getY() - 1, e.getBlockClicked().getZ());
			
		}
		else if(face == BlockFace.NORTH) {
			
			loc2 = new Location(e.getBlockClicked().getWorld(), (double) e.getBlockClicked().getX(), (double) e.getBlockClicked().getY(), (double) e.getBlockClicked().getZ() - 1);
			
		}
		else if(face == BlockFace.SOUTH) {
			
			loc2 = new Location(e.getBlockClicked().getWorld(), (double) e.getBlockClicked().getX(), (double) e.getBlockClicked().getY(), (double) e.getBlockClicked().getZ() + 1);
			
		}
		else if(face == BlockFace.EAST) {
			
			loc2 = new Location(e.getBlockClicked().getWorld(), (double) e.getBlockClicked().getX() + 1, (double) e.getBlockClicked().getY(), (double) e.getBlockClicked().getZ());
			
		}
		else {
			
			loc2 = new Location(e.getBlockClicked().getWorld(), (double) e.getBlockClicked().getX() - 1, (double) e.getBlockClicked().getY(), (double) e.getBlockClicked().getZ());
			
		}
		
		//TOWN HANDLING
		for(Town town : MainPlugin.townList) {

			if(town.inTown(e.getBlockClicked().getLocation()) || town.inTown(loc2)) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//CITIZEN
				else if(playerRank.equals("citizen")) {

					cancel = true;

				}
				//CITIZEN//

			}

		}
		//TOWN HANDLING//

		//ADMIN TOWN HANDLING//
		for(AdminTown town : MainPlugin.adminTownList) {

			if(town.inTown(e.getBlockClicked().getLocation()) || town.inTown(loc2)) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

			}

		}
		//ADMIN TOWN HANDLING//

		//PLOT HANDLING
		for(Plot plot : MainPlugin.plotList) {

			if(plot.inPlot(e.getBlockClicked().getX(), e.getBlockClicked().getY(), e.getBlockClicked().getZ(),e.getBlockClicked().getWorld()) || plot.inPlot((int)loc2.getX(), (int)loc2.getY(), (int)loc2.getZ(), loc2.getWorld())) {

				String playerRank = plot.getRank(e.getPlayer().getUniqueId().toString());

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//RESIDENT AND OWNER
				else if(playerRank.equals("resident") || playerRank.equals("owner")) {

					if(plot.getRank(e.getPlayer().getUniqueId().toString()).equals("none")) {

						cancel = true;

					}
					else {

						cancel = false;

					}

					break;

				}
				//RESIDENT AND OWNER//

			}

		}
		//PLOT HANDLING//
		
		//DUNGEON HANDLING//
		for(DungeonClaim dungeon : MainPlugin.dungeonList) {

			if(dungeon.inClaim(e.getBlockClicked().getLocation()) || dungeon.inClaim(loc2)) {

				if(!e.getPlayer().isOp()) {
							
					e.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "A mysterious force prevents you from doing this.");
					e.setCancelled(true);
					return;
							
				}

			}

		}
		//DUNGEON HANDLING//

		//CANCELLING
		if(cancel) e.getPlayer().sendMessage(ChatColor.RED + "You cannot empty that here.");
		e.setCancelled(cancel);
		//CANCELLING

	}
	//////////////////////

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {

		//if(e.getBlock().getType() == Material.BLACK_SHULKER_BOX || e.getBlock().getType() == Material.BLUE_SHULKER_BOX || e.getBlock().getType() == Material.BROWN_SHULKER_BOX || e.getBlock().getType() == Material.CYAN_SHULKER_BOX || e.getBlock().getType() == Material.GRAY_SHULKER_BOX || e.getBlock().getType() == Material.GREEN_SHULKER_BOX || e.getBlock().getType() == Material.LIGHT_BLUE_SHULKER_BOX || e.getBlock().getType() == Material.LIME_SHULKER_BOX || e.getBlock().getType() == Material.MAGENTA_SHULKER_BOX || e.getBlock().getType() == Material.ORANGE_SHULKER_BOX || e.getBlock().getType() == Material.PINK_SHULKER_BOX || e.getBlock().getType() == Material.PURPLE_SHULKER_BOX || e.getBlock().getType() == Material.RED_SHULKER_BOX || e.getBlock().getType() == Material.SILVER_SHULKER_BOX || e.getBlock().getType() == Material.WHITE_SHULKER_BOX || e.getBlock().getType() == Material.YELLOW_SHULKER_BOX) {
		
		boolean inTown = false;
		boolean inPlot = false;
		boolean cancel = false;

		//TOWN HANDLING
		for(Town town : MainPlugin.townList) {

			if(town.inTown(e.getBlock().getLocation())) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());
				inTown = true;

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//CITIZEN
				else if(playerRank.equals("citizen")) {

					if(!(e.getBlock().getType() == Material.MELON_STEM || e.getBlock().getType() == Material.NETHER_WART_BLOCK || e.getBlock().getType() == Material.POTATO || e.getBlock().getType() == Material.PUMPKIN_STEM || e.getBlock().getType() == Material.RED_MUSHROOM || e.getBlock().getType() == Material.BEETROOT_BLOCK || e.getBlock().getType() == Material.CACTUS || e.getBlock().getType() == Material.BROWN_MUSHROOM || e.getBlock().getType() == Material.CARROT || e.getBlock().getType() == Material.WHEAT || e.getBlock().getType() == Material.VINE || e.getBlock().getType() == Material.SUGAR_CANE_BLOCK)) {

						cancel = true;

					}

				}
				//CITIZEN//

				//BUILDER AND MANAGER
				else if(playerRank.equals("builder") || playerRank.equals("manager")) {

					if(e.getBlock().getType() == Material.SPONGE) {

						cancel = true;

					}

				}
				//BUILDER AND MANAGER//

				//OWNER
				if(playerRank.equals("owner") || town.getRank(e.getPlayer().getUniqueId().toString()).equals("manager")) {

					for(Plot plot : MainPlugin.plotList) {

						if(plot.inPlot(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), e.getBlock().getWorld())) inPlot = true;

					}

					if(MainPlugin.getConfig().getBoolean(e.getPlayer().getName()) && (town.energy < 1000000 || town.stockpileEnergy < 75000) && !inPlot) {
						
						if(e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2) {
							
							if(town.stockpileEnergy == 75000) {

								town.energy += 100;
								if(town.energy > 1000000) {

									town.energy = 1000000;

								}

							}
							else {

								town.stockpileEnergy += 100;
								if(town.stockpileEnergy > 75000) {

									town.stockpileEnergy = 75000;

								}


							}
							e.getPlayer().sendMessage(ChatColor.GREEN + "+100\nEnergy = " + town.energy + "\nStockpile Energy = " + town.stockpileEnergy);
							e.setCancelled(true);

						}
						else if(e.getBlock().getType() == Material.GOLD_BLOCK) {

							if(town.stockpileEnergy == 75000) {

								town.energy += 25000;
								if(town.energy > 1000000) {

									town.energy = 1000000;

								}

							}
							else {

								town.stockpileEnergy += 25000;
								if(town.stockpileEnergy > 75000) {

									town.stockpileEnergy = 75000;

								}

							}
							e.getPlayer().sendMessage(ChatColor.GREEN + "+25000\nEnergy = " + town.energy + "\nStockpile Energy = " + town.stockpileEnergy);
							e.setCancelled(true);

						}
						else if(e.getBlock().getType() == Material.SPONGE) {

							cancel = true;

						}
						if(e.getBlock().getType() == Material.GOLD_BLOCK || e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2) {

							if(!town.growthFrozen && (e.getBlock().getType() == Material.GOLD_BLOCK || e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2)) {

								town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));

							}
							if(e.getBlock().getType() == Material.GOLD_BLOCK) {

								if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLD_BLOCK) {
									
									ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
									if(itemInMainHand.getAmount() == 1) {
										
										e.getPlayer().getInventory().setItemInMainHand(null);
										
									}
									else {
										
										itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
										e.getPlayer().getInventory().setItemInMainHand(itemInMainHand);
										
									}
									
								}
								else if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.GOLD_BLOCK) {
									
									ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();
									if(itemInOffHand.getAmount() == 1) {
										
										e.getPlayer().getInventory().setItemInOffHand(null);
										
									}
									else {
										
										itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
										e.getPlayer().getInventory().setItemInOffHand(itemInOffHand);
										
									}
									
								}
								//MainPlugin.removeInventoryItems(e.getPlayer().getInventory(), Material.GOLD_BLOCK, 1);

							}
							else if(e.getBlock().getType() == Material.LOG) {

								if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LOG) {
									
									ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
									if(itemInMainHand.getAmount() == 1) {
										
										e.getPlayer().getInventory().setItemInMainHand(null);
										
									}
									else {
										
										itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
										e.getPlayer().getInventory().setItemInMainHand(itemInMainHand);
										
									}
									
								}
								else if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.LOG) {
									
									ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();
									if(itemInOffHand.getAmount() == 1) {
										
										e.getPlayer().getInventory().setItemInOffHand(null);
										
									}
									else {
										
										itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
										e.getPlayer().getInventory().setItemInOffHand(itemInOffHand);
										
									}
									
								}
								//MainPlugin.removeInventoryItems(e.getPlayer().getInventory(), Material.LOG, 1);

							}
							else if(e.getBlock().getType() == Material.LOG_2) {

								if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LOG_2) {
									
									ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
									if(itemInMainHand.getAmount() == 1) {
										
										e.getPlayer().getInventory().setItemInMainHand(null);
										
									}
									else {
										
										itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
										e.getPlayer().getInventory().setItemInMainHand(itemInMainHand);
										
									}
									
								}
								else if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.LOG_2) {
									
									ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();
									if(itemInOffHand.getAmount() == 1) {
										
										e.getPlayer().getInventory().setItemInOffHand(null);
										
									}
									else {
										
										itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
										e.getPlayer().getInventory().setItemInOffHand(itemInOffHand);
										
									}
									
								}
								//MainPlugin.removeInventoryItems(e.getPlayer().getInventory(), Material.LOG_2, 1);

							}

							return;

						}

					}

				}
				//OWNER//

			}

		}
		//TOWN HANDLING//

		//ADMIN TOWN HANDLING//
		for(AdminTown town : MainPlugin.adminTownList) {

			if(town.inTown(e.getBlock().getLocation())) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());
				inTown = true;

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//CITIZEN
				else if(playerRank.equals("citizen")) {

					if(!(e.getBlock().getType() == Material.MELON_STEM || e.getBlock().getType() == Material.NETHER_WART_BLOCK || e.getBlock().getType() == Material.POTATO || e.getBlock().getType() == Material.PUMPKIN_STEM || e.getBlock().getType() == Material.RED_MUSHROOM || e.getBlock().getType() == Material.BEETROOT_BLOCK || e.getBlock().getType() == Material.CACTUS || e.getBlock().getType() == Material.BROWN_MUSHROOM || e.getBlock().getType() == Material.CARROT || e.getBlock().getType() == Material.WHEAT || e.getBlock().getType() == Material.VINE || e.getBlock().getType() == Material.SUGAR_CANE_BLOCK)) {

						cancel = true;

					}

				}
				//CITIZEN//

				//BUILDER AND MANAGER
				else if(playerRank.equals("builder") || playerRank.equals("manager")) {

					if(e.getBlock().getType() == Material.SPONGE) {

						cancel = true;

					}

				}
				//BUILDER AND MANAGER//

				//Owner
				else if(playerRank.equals("owner")) {

					if(e.getBlock().getType() == Material.SPONGE) {

						cancel = true;

					}

				}

			}

		}
		//ADMIN TOWN HANDLING//

		//PLOT HANDLING
		for(Plot plot : MainPlugin.plotList) {

			if(plot.inPlot(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(),e.getBlock().getWorld())) {

				String playerRank = plot.getRank(e.getPlayer().getUniqueId().toString());
				inPlot = true;

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//RESIDENT AND OWNER
				else if(playerRank.equals("resident") || playerRank.equals("owner")) {

					if(inTown && (e.getBlock().getType() == Material.SPONGE)) {

						cancel = true;

					}
					else if(MainPlugin.getConfig().getBoolean(e.getPlayer().getName())) {

						Material blockType = e.getBlock().getType();

						if(blockType == Material.LOG || blockType == Material.LOG_2 || blockType == Material.GOLD_BLOCK) {

							if(plot.getRank(e.getPlayer().getUniqueId().toString()).equals("owner")) {

								if(plot.energy < 50000) {

									if(blockType == Material.LOG || blockType == Material.LOG_2) {

										plot.energy += 100;
										if(plot.energy > 50000) {

											plot.energy = 50000;

										}
										e.getPlayer().sendMessage(ChatColor.GREEN + "+100 Energy. Total Energy = " + plot.energy);
										if(blockType == Material.LOG) {
											
											if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LOG) {
												
												ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
												if(itemInMainHand.getAmount() == 1) {
													
													e.getPlayer().getInventory().setItemInMainHand(null);
													
												}
												else {
													
													itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
													e.getPlayer().getInventory().setItemInMainHand(itemInMainHand);
													
												}
												
											}
											else if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.LOG) {
												
												ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();
												if(itemInOffHand.getAmount() == 1) {
													
													e.getPlayer().getInventory().setItemInOffHand(null);
													
												}
												else {
													
													itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
													e.getPlayer().getInventory().setItemInOffHand(itemInOffHand);
													
												}
												
											}
											
										}
										if(blockType == Material.LOG_2) {
											
											if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LOG_2) {
												
												ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
												if(itemInMainHand.getAmount() == 1) {
													
													e.getPlayer().getInventory().setItemInMainHand(null);
													
												}
												else {
													
													itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
													e.getPlayer().getInventory().setItemInMainHand(itemInMainHand);
													
												}
												
											}
											else if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.LOG_2) {
												
												ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();
												if(itemInOffHand.getAmount() == 1) {
													
													e.getPlayer().getInventory().setItemInOffHand(null);
													
												}
												else {
													
													itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
													e.getPlayer().getInventory().setItemInOffHand(itemInOffHand);
													
												}
												
											}
											
										}
										e.setCancelled(true);
										return;

									}
									else {

										plot.energy += 25000;
										if(plot.energy > 50000) {

											plot.energy = 50000;

										}
										e.getPlayer().sendMessage(ChatColor.GREEN + "+25000. Total Energy = " + plot.energy);
										if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLD_BLOCK) {
											
											ItemStack itemInMainHand = e.getPlayer().getInventory().getItemInMainHand();
											if(itemInMainHand.getAmount() == 1) {
												
												e.getPlayer().getInventory().setItemInMainHand(null);
												
											}
											else {
												
												itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
												e.getPlayer().getInventory().setItemInMainHand(itemInMainHand);
												
											}
											
										}
										else if(e.getPlayer().getInventory().getItemInOffHand().getType() == Material.GOLD_BLOCK) {
											
											ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();
											if(itemInOffHand.getAmount() == 1) {
												
												e.getPlayer().getInventory().setItemInOffHand(null);
												
											}
											else {
												
												itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
												e.getPlayer().getInventory().setItemInOffHand(itemInOffHand);
												
											}
											
										}
										e.setCancelled(true);
										return;

									}

								}

							}

						}

					}
					else {

						cancel = false;

					}

					break;

				}
				//RESIDENT AND OWNER//

			}

		}
		//PLOT HANDLING//
		
		//DUNGEON HANDLING//
		for(DungeonClaim dungeon : MainPlugin.dungeonList) {

			if(dungeon.inClaim(e.getBlock().getLocation())) {

				if(!e.getPlayer().isOp()) {
					
					for(Material item : dungeon.placeList) {
						if(e.getBlock().getType() == item) {
							
							return;
							
						}
						
					}
					
					e.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "A mysterious force prevents you from doing this.");
					e.setCancelled(true);
					return;
					
				}

			}

		}
		//DUNGEON HANDLING//

		//MISCELLANEOUS RULES
		if(e.getBlock().getType() == Material.REDSTONE_TORCH_ON && inPlot && MainPlugin.getConfig().getBoolean(e.getPlayer().getName())) {

			cancel = true;

		}
		//MISCELLANEOUS RULES//

		//CREATION OF TOWNS AND PLOTS
		if(!cancel) {

			//TOWNS
			if(e.getBlock().getType() == Material.SPONGE && !inTown) {

				for(Town town : MainPlugin.townList) {

					if(town.world != e.getBlock().getLocation().getWorld()) continue;
					
					if(e.getBlock().getLocation().distance(new Location(town.world,town.x,e.getBlock().getY(),town.z)) <= 400) {

						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "This sponge is too close to another.");
						return;

					}

				}
				for(AdminTown town : MainPlugin.adminTownList) {

					if(town.world != e.getBlock().getLocation().getWorld()) continue;
					if(e.getBlock().getWorld().getEnvironment() == Environment.NETHER) return;
					
					if(e.getBlock().getLocation().distance(new Location(town.world,town.x,e.getBlock().getY(),town.z)) <= 400) {

						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "This sponge is too close to another.");
						return;

					}

				}

				MainPlugin.townList.add(new Town("NewTown",e.getPlayer().getUniqueId().toString(),e.getPlayer().getUniqueId().toString(),e.getPlayer().getUniqueId().toString(),e.getPlayer().getUniqueId().toString(),e.getBlock().getLocation().getWorld().getName()+","+(int)e.getBlock().getLocation().getX()+","+(int)e.getBlock().getLocation().getY()+","+(int)e.getBlock().getLocation().getZ(),"75000","250000","20","false"));
				e.getPlayer().sendMessage("New town created!");

			}
			//TOWNS//

			//PLOTS
			else if(e.getBlock().getType() == Material.REDSTONE_TORCH_ON && MainPlugin.getConfig().getBoolean(e.getPlayer().getName())) {

				PlayerInventory playerInv = e.getPlayer().getInventory();
				if(playerInv.contains(Material.GOLD_NUGGET, 9)) {

					boolean cancelPlot = false;
					for(Plot plot : MainPlugin.plotList) {

						if(plot.inPlot(e.getBlock().getX()+4, e.getBlock().getY()+7, e.getBlock().getZ()+4, e.getBlock().getWorld()) || plot.inPlot(e.getBlock().getX()+4, e.getBlock().getY()+7, e.getBlock().getZ()-4, e.getBlock().getWorld()) || plot.inPlot(e.getBlock().getX()-4, e.getBlock().getY()+7, e.getBlock().getZ()+4, e.getBlock().getWorld()) || plot.inPlot(e.getBlock().getX()-4, e.getBlock().getY()+7, e.getBlock().getZ()-4, e.getBlock().getWorld()) || plot.inPlot(e.getBlock().getX()+4, e.getBlock().getY()-1, e.getBlock().getZ()+4, e.getBlock().getWorld()) || plot.inPlot(e.getBlock().getX()+4, e.getBlock().getY()-1, e.getBlock().getZ()-4, e.getBlock().getWorld()) || plot.inPlot(e.getBlock().getX()-4, e.getBlock().getY()-1, e.getBlock().getZ()+4, e.getBlock().getWorld()) || plot.inPlot(e.getBlock().getX()-4, e.getBlock().getY()-1, e.getBlock().getZ()-4, e.getBlock().getWorld())) {

							cancelPlot = true;

						}

					}
					if(cancelPlot) {

						e.getPlayer().sendMessage(ChatColor.RED + "This plot is too close to its neighbor!");
						e.setCancelled(true);
						return;

					}
					else {

						e.getPlayer().sendMessage("Plot Created!");
						MainPlugin.removeInventoryItems(e.getPlayer().getInventory(), Material.GOLD_NUGGET, 9);
						MainPlugin.plotList.add(new Plot(e.getBlock().getLocation().getWorld().getName(), "" + e.getBlock().getX(), "" + e.getBlock().getY(), "" + e.getBlock().getZ(), "500", e.getPlayer().getUniqueId().toString(), e.getPlayer().getUniqueId().toString(), "0", "false", "0"));
						e.setCancelled(true);
						return;

					}

				}

			}
			//PLOTS//
		}
		//CREATION OF TOWNS AND PLOTS//

		//CANCELLING
		else {

			if(cancel) e.getPlayer().sendMessage(ChatColor.RED + "You cannot place here.");
			e.setCancelled(true);

		}
		//CANCELLING

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {

		boolean inTown = false;
		boolean cancel = false;

		//TOWN HANDLING
		for(Town town : MainPlugin.townList) {

			if(town.inTown(e.getBlock().getLocation())) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());
				inTown = true;

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//CITIZEN
				else if(playerRank.equals("citizen")) {

					if(!(e.getBlock().getType() == Material.MELON_BLOCK || e.getBlock().getType() == Material.NETHER_WART_BLOCK || e.getBlock().getType() == Material.POTATO || e.getBlock().getType() == Material.PUMPKIN || e.getBlock().getType() == Material.RED_MUSHROOM || e.getBlock().getType() == Material.BEETROOT_BLOCK || e.getBlock().getType() == Material.CACTUS || e.getBlock().getType() == Material.BROWN_MUSHROOM || e.getBlock().getType() == Material.CARROT || e.getBlock().getType() == Material.WHEAT || e.getBlock().getType() == Material.VINE || e.getBlock().getType() == Material.SUGAR_CANE_BLOCK)) {

						cancel = true;

					}

				}
				//CITIZEN//

				//BUILDER AND MANAGER
				else if(playerRank.equals("builder") || playerRank.equals("manager")) {

					if(e.getBlock().getType() == Material.SPONGE) {

						cancel = true;

					}

				}
				//BUILDER AND MANAGER//
				
				if(e.getBlock().getType() == Material.SPONGE) {
					
					e.getPlayer().sendMessage(ChatColor.RED + "You cannot remove the sponge!");
					e.setCancelled(true);
					return;
					
				}

			}

		}
		//TOWN HANDLING//

		//ADMIN TOWN HANDLING
		for(AdminTown town : MainPlugin.adminTownList) {

			if(town.inTown(e.getBlock().getLocation())) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());
				inTown = true;

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//CITIZEN//
				else if(playerRank.equals("citizen")) {

					if(!(e.getBlock().getType() == Material.MELON_BLOCK || e.getBlock().getType() == Material.NETHER_WART_BLOCK || e.getBlock().getType() == Material.POTATO || e.getBlock().getType() == Material.PUMPKIN || e.getBlock().getType() == Material.RED_MUSHROOM || e.getBlock().getType() == Material.BEETROOT_BLOCK || e.getBlock().getType() == Material.CACTUS || e.getBlock().getType() == Material.BROWN_MUSHROOM || e.getBlock().getType() == Material.CARROT || e.getBlock().getType() == Material.WHEAT || e.getBlock().getType() == Material.VINE || e.getBlock().getType() == Material.SUGAR_CANE_BLOCK)) {

						cancel = true;

					}

				}
				//CITIZEN//

				//BUILDER AND MANAGER
				else if(playerRank.equals("builder") || playerRank.equals("manager")) {

					if(e.getBlock().getType() == Material.SPONGE) {

						cancel = true;

					}

				}
				//BUILDER AND MANAGER//

			}

		}
		//ADMIN TOWN HANDLING//

		//PLOT HANDLING
		for(Plot plot : MainPlugin.plotList) {

			if(plot.inPlot(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(),e.getBlock().getWorld())) {

				String playerRank = plot.getRank(e.getPlayer().getUniqueId().toString());
				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//RESIDENT AND OWNER
				else if(playerRank.equals("resident") || playerRank.equals("owner")) {

					if(inTown && e.getBlock().getType() == Material.SPONGE) {

						cancel = true;

					}
					else {

						cancel = false;

					}

					break;

				}
				//RESIDENT AND OWNER//

			}

		}
		//PLOT HANDLING//

		//ADMIN TOWN HANDLING
		for(AdminTown town : MainPlugin.adminTownList) {

			if(town.inTown(e.getBlock().getLocation())) {

				String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());
				inTown = true;

				//NO RANK
				if(playerRank.equals("none")) {

					cancel = true;
					break;

				}
				//NO RANK//

				//CITIZEN
				else if(playerRank.equals("citizen")) {

					if(!(e.getBlock().getType() == Material.MELON_BLOCK || e.getBlock().getType() == Material.NETHER_WART_BLOCK || e.getBlock().getType() == Material.POTATO || e.getBlock().getType() == Material.PUMPKIN || e.getBlock().getType() == Material.RED_MUSHROOM || e.getBlock().getType() == Material.BEETROOT_BLOCK || e.getBlock().getType() == Material.CACTUS || e.getBlock().getType() == Material.BROWN_MUSHROOM || e.getBlock().getType() == Material.CARROT || e.getBlock().getType() == Material.WHEAT || e.getBlock().getType() == Material.VINE || e.getBlock().getType() == Material.SUGAR_CANE_BLOCK)) {

						cancel = true;

					}

				}
				//CITIZEN//

				//BUILDER AND MANAGER
				else if(playerRank.equals("builder") || playerRank.equals("manager")) {

					if(e.getBlock().getType() == Material.SPONGE) {

						cancel = true;

					}

				}
				//BUILDER AND MANAGER//
				else if(playerRank.equals("owner")) {
					
					if(e.getBlock().getType() == Material.SPONGE) {
						
						e.getPlayer().sendMessage(ChatColor.RED + "You cannot break the sponge.");
						e.setCancelled(true);
						return;
						
					}
					
				}

			}

		}
		//ADMIN TOWN HANDLING//
		
		//DUNGEON HANDLING//
		for(DungeonClaim dungeon : MainPlugin.dungeonList) {

			if(dungeon.inClaim(e.getBlock().getLocation())) {

				if(!e.getPlayer().isOp()) {
					
					for(Material item : dungeon.breakList) {
						
						if(e.getBlock().getType() == item) {
							
							return;
							
						}
						
					}
					
					e.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "A mysterious force prevents you from doing this.");
					e.setCancelled(true);
					return;
					
				}

			}

		}
		//DUNGEON HANDLING//

		//CANCELLING
		if(cancel) {

			if(cancel) e.getPlayer().sendMessage(ChatColor.RED + "You cannot break here.");
			e.setCancelled(true);

		}
		//CANCELLING//

	}

	@EventHandler
	public void onBlockInteract(PlayerInteractEvent e) {

		if(e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.DISPENSER || e.getClickedBlock().getType() == Material.DROPPER || e.getClickedBlock().getType() == Material.FURNACE || e.getClickedBlock().getType() == Material.HOPPER || e.getClickedBlock().getType() == Material.TRAPPED_CHEST || e.getClickedBlock().getType() == Material.ANVIL || e.getClickedBlock().getType() == Material.STONE_BUTTON || e.getClickedBlock().getType() == Material.WOOD_BUTTON || e.getClickedBlock().getType() == Material.WOOD_DOOR || e.getClickedBlock().getType() == Material.FENCE_GATE || e.getClickedBlock().getType() == Material.TRAP_DOOR || e.getClickedBlock().getType() == Material.STONE_PLATE || e.getClickedBlock().getType() == Material.BIRCH_DOOR || e.getClickedBlock().getType() == Material.SPRUCE_DOOR || e.getClickedBlock().getType() == Material.DARK_OAK_DOOR || e.getClickedBlock().getType() == Material.ACACIA_DOOR || e.getClickedBlock().getType() == Material.JUNGLE_DOOR || e.getClickedBlock().getType() == Material.BURNING_FURNACE || e.getClickedBlock().getType() == Material.SPRUCE_FENCE_GATE || e.getClickedBlock().getType() == Material.DARK_OAK_FENCE_GATE || e.getClickedBlock().getType() == Material.BIRCH_FENCE_GATE || e.getClickedBlock().getType() == Material.ACACIA_FENCE_GATE || e.getClickedBlock().getType() == Material.JUNGLE_FENCE_GATE || e.getClickedBlock().getType() == Material.WHITE_SHULKER_BOX || e.getClickedBlock().getType() == Material.BLACK_SHULKER_BOX || e.getClickedBlock().getType() == Material.RED_SHULKER_BOX || e.getClickedBlock().getType() == Material.BLUE_SHULKER_BOX || e.getClickedBlock().getType() == Material.BROWN_SHULKER_BOX || e.getClickedBlock().getType() == Material.CYAN_SHULKER_BOX || e.getClickedBlock().getType() == Material.GRAY_SHULKER_BOX || e.getClickedBlock().getType() == Material.GREEN_SHULKER_BOX || e.getClickedBlock().getType() == Material.LIGHT_BLUE_SHULKER_BOX || e.getClickedBlock().getType() == Material.LIME_SHULKER_BOX || e.getClickedBlock().getType() == Material.MAGENTA_SHULKER_BOX || e.getClickedBlock().getType() == Material.ORANGE_SHULKER_BOX || e.getClickedBlock().getType() == Material.PINK_SHULKER_BOX || e.getClickedBlock().getType() == Material.PURPLE_SHULKER_BOX || e.getClickedBlock().getType() == Material.SILVER_SHULKER_BOX || e.getClickedBlock().getType() == Material.YELLOW_SHULKER_BOX || e.getClickedBlock().getType() == Material.LEVER) {

			//			boolean isDoor = false;
			//			
			//			if(e.getClickedBlock().getType() == Material.WOODEN_DOOR || e.getClickedBlock().getType() == Material.BIRCH_DOOR || e.getClickedBlock().getType() == Material.SPRUCE_DOOR || e.getClickedBlock().getType() == Material.DARK_OAK_DOOR || e.getClickedBlock().getType() == Material.ACACIA_DOOR || e.getClickedBlock().getType() == Material.JUNGLE_DOOR) {
			//				
			//				isDoor = true;
			//				BlockState doorState = e.getClickedBlock().getState();
			//				
			//			}

			boolean cancel = false;

			//TOWN HANDLING
			for(Town town : MainPlugin.townList) {

				if(town.inTown(e.getClickedBlock().getLocation())) {

					String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());
					//NO RANK
					if(playerRank.equals("none")) {

						cancel = true;
						break;

					}
					//NO RANK//

					//CITIZEN
					else if(playerRank.equals("citizen")) {

						cancel = true;
						break;

					}
					//CITIZEN//

					//BUILDER
					else if(playerRank.equals("builder")) {

						cancel = false;

					}
					//BUILDER//

				}

			}
			//TOWN HANDLING//

			//PLOT HANDLING
			for(Plot plot : MainPlugin.plotList) {

				if(plot.inPlot(e.getClickedBlock().getX(), e.getClickedBlock().getY(), e.getClickedBlock().getZ(), e.getClickedBlock().getWorld())) {

					String playerRank = plot.getRank(e.getPlayer().getUniqueId().toString());
					//NO RANK
					if(playerRank.equals("none")) {

						cancel = true;
						break;

					}
					//NO RANK//

					//OWNER OR RESIDENT
					else {

						cancel = false;

					}
					//OWNER OR RESIDENT//

				}

			}
			//PLOT HANDLING//

			//ADMIN TOWN HANDLING
			for(AdminTown town : MainPlugin.adminTownList) {

				if(town.inTown(e.getClickedBlock().getLocation())) {

					String playerRank = town.getRank(e.getPlayer().getUniqueId().toString());
					//NO RANK
					if(playerRank.equals("none")) {

						cancel = true;
						break;

					}
					//NO RANK//

					//CITIZEN
					else if(playerRank.equals("citizen")) {

						cancel = true;
						break;

					}
					//CITIZEN//

					//BUILDER
					else if(playerRank.equals("builder")) {

						cancel = true;

					}
					//BUILDER//

				}

			}
			//ADMIN TOWN HANDLING//

			//CANCELLING
			if(cancel) e.getPlayer().sendMessage(ChatColor.RED + "You cannot interact with that here.");
			e.setCancelled(cancel);
			//CANCELLING//

		}

	}
	
	@EventHandler
	public void onBlockDamage(BlockDamageEvent e) {

		if(MainPlugin.getConfig().getBoolean(e.getPlayer().getName())) {

			final Player player = e.getPlayer();
			boolean inTown = false;

			ArrayList<Town> townRemove = new ArrayList<Town>();

			for(Town town : MainPlugin.townList) {

				if(town.inTown(e.getBlock().getLocation()) && town.getRank(e.getPlayer().getUniqueId().toString()).equals("none")) {

					if(!MainPlugin.raidCooldownList.contains(player)) {

						if(e.getItemInHand().getType() == Material.DIAMOND_SWORD) {

							if(town.stockpileEnergy <= 0) {

								town.energy -= 12;
								if(town.energy <= 0) {

									townRemove.add(town);

								}

							}
							else {

								town.stockpileEnergy -= 12;

							}
							MainPlugin.raidCooldownList.add(player);
							player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
							if(1561 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
								public void run() {
									MainPlugin.raidCooldownList.remove(player);
								}
							}, 50);

						}
						else if(e.getItemInHand().getType() == Material.IRON_SWORD) {

							if(town.stockpileEnergy <= 0) {

								town.energy -= 9;
								if(town.energy <= 0) {

									townRemove.add(town);

								}

							}
							else {

								town.stockpileEnergy -= 9;

							}
							MainPlugin.raidCooldownList.add(player);
							player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
							if(250 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
								public void run() {
									MainPlugin.raidCooldownList.remove(player);
								}
							}, 50);

						}
						else if(e.getItemInHand().getType() == Material.STONE_SWORD) {

							if(town.stockpileEnergy <= 0) {

								town.energy -= 6;
								if(town.energy <= 0) {

									townRemove.add(town);

								}

							}
							else {

								town.stockpileEnergy -= 6;

							}
							MainPlugin.raidCooldownList.add(player);
							player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
							if(131 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
								public void run() {
									MainPlugin.raidCooldownList.remove(player);
								}
							}, 50);

						}
						else if(e.getItemInHand().getType() == Material.WOOD_SWORD) {

							if(town.stockpileEnergy <= 0) {

								town.energy -= 3;
								if(town.energy <= 0) {

									townRemove.add(town);

								}

							}
							else {

								town.stockpileEnergy -= 3;

							}
							MainPlugin.raidCooldownList.add(player);
							player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
							if(59 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
								public void run() {
									MainPlugin.raidCooldownList.remove(player);
								}
							}, 50);

						}
						else if(e.getItemInHand().getType() == Material.GOLD_SWORD) {

							if(town.stockpileEnergy <= 0) {

								town.energy -= 16;
								if(town.energy <= 0) {

									townRemove.add(town);

								}

							}
							else {

								town.stockpileEnergy -= 16;

							}
							MainPlugin.raidCooldownList.add(player);
							player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
							if(32 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
								public void run() {
									MainPlugin.raidCooldownList.remove(player);
								}
							}, 50);

						}
						town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));
						player.sendMessage("The town is now at " + (town.energy+town.stockpileEnergy) + " energy.");

					}

					inTown = true;

				}

			}
			for(Town town : townRemove) {

				MainPlugin.townList.remove(town);

			}

			//Extra Security Check//
			for(Town town : MainPlugin.townList) {

				if(town.inTown(e.getBlock().getLocation())) inTown = true;

			}
			for(AdminTown town : MainPlugin.adminTownList) {

				if(town.inTown(e.getBlock().getLocation())) inTown = true;

			}
			//Extra Security Check//

			if(!inTown) {

				ArrayList<Plot> plotRemove = new ArrayList<Plot>();

				for(Plot plot : MainPlugin.plotList) {

					if(plot.inPlot(e.getBlock().getX(),e.getBlock().getY(),e.getBlock().getZ(),e.getBlock().getWorld()) && plot.getRank(e.getPlayer().getUniqueId().toString()).equals("none")) {

						if(!MainPlugin.raidCooldownList.contains(player)) {

							if(e.getItemInHand().getType() == Material.DIAMOND_SWORD) {

								plot.energy -= 24;
								if(plot.energy <= 0) {

									plotRemove.add(plot);

								}
								MainPlugin.raidCooldownList.add(player);
								player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
								if(1561 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
									public void run() {
										MainPlugin.raidCooldownList.remove(player);
									}
								}, 100);
								
							}
							else if(e.getItemInHand().getType() == Material.IRON_SWORD) {

								plot.energy -= 18;
								if(plot.energy <= 0) {

									plotRemove.add(plot);

								}
								MainPlugin.raidCooldownList.add(player);
								player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
								if(250 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
									public void run() {
										MainPlugin.raidCooldownList.remove(player);
									}
								}, 100);
								
							}
							else if(e.getItemInHand().getType() == Material.STONE_SWORD) {

								plot.energy -= 12;
								if(plot.energy <= 0) {

									plotRemove.add(plot);

								}
								MainPlugin.raidCooldownList.add(player);
								player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
								if(131 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
									public void run() {
										MainPlugin.raidCooldownList.remove(player);
									}
								}, 100);
								
							}
							else if(e.getItemInHand().getType() == Material.WOOD_SWORD) {

								plot.energy -= 6;
								if(plot.energy <= 0) {

									plotRemove.add(plot);

								}
								MainPlugin.raidCooldownList.add(player);
								player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
								if(59 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
									public void run() {
										MainPlugin.raidCooldownList.remove(player);
									}
								}, 100);
								
							}
							else if(e.getItemInHand().getType() == Material.GOLD_SWORD) {

								plot.energy -= 32;
								if(plot.energy <= 0) {

									plotRemove.add(plot);

								}
								MainPlugin.raidCooldownList.add(player);
								player.getInventory().getItemInMainHand().setDurability((short) (player.getInventory().getItemInMainHand().getDurability()+10));
								if(32 - player.getInventory().getItemInMainHand().getDurability() < 0) player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
								Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
									public void run() {
										MainPlugin.raidCooldownList.remove(player);
									}
								}, 100);
							}

							player.sendMessage("The plot is now at " + plot.energy + " energy.");

						}

					}

				}
				for(Plot plot : plotRemove) {

					MainPlugin.plotList.remove(plot);

				}

			}

		}

		if(e.getBlock().getType() == Material.SPONGE) {

			e.getPlayer().sendMessage(ChatColor.GREEN + "Spawn set!");
			MainPlugin.getConfig().set(e.getPlayer().getUniqueId().toString(),e.getBlock().getWorld().getName()+","+e.getBlock().getX()+","+e.getBlock().getY()+","+e.getBlock().getZ());

		}

	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {

		String townName = null;
		for(Town town : MainPlugin.townList) if(town.inTown(e.getPlayer().getLocation())) townName = town.name;
		for(AdminTown town : MainPlugin.adminTownList) if(town.inTown(e.getPlayer().getLocation())) townName = town.name;
		int plotPrice = 0;
		for(Plot plot : MainPlugin.plotList) if(plot.inPlot((int)e.getPlayer().getLocation().getX(),(int)e.getPlayer().getLocation().getY(),(int)e.getPlayer().getLocation().getZ(),e.getPlayer().getLocation().getWorld())) plotPrice = plot.price;
		if(townName != null && plotPrice == 0) {

			ActionBarAPI.sendActionBar(e.getPlayer(),townName);

		}
		else if(plotPrice != 0) {

			ActionBarAPI.sendActionBar(e.getPlayer(),ChatColor.GREEN+"Plot for Sale: $"+plotPrice + " (/claim buy)");

		}
		
		for(StaffMember SM : MainPlugin.staffList) {
			
			if(SM.monitoring) {
			
				if(SM.player == e.getPlayer()) {
					
					for(Player player : MainPlugin.getServer().getOnlinePlayers()) {
						
						if(player.canSee(SM.player)) player.hidePlayer(SM.player);
						
					}
					
				}
				
			}
			
		}
		
		if(MainPlugin.getConfig().getBoolean(e.getPlayer().getName())) {

			for(Plot plot : MainPlugin.plotList) {

				if(plot.world == e.getPlayer().getWorld()) {
					if(new Location(plot.world,plot.x,plot.y,plot.z).distance(e.getPlayer().getLocation()) < 20) plot.showBorders(e.getPlayer());
				}
				
			}
			for(DungeonClaim dungeon : MainPlugin.dungeonList) {
				
				if(dungeon.loc.getWorld() == e.getPlayer().getWorld()) {
					
					if(new Location(dungeon.loc.getWorld(),dungeon.loc.getX() + (dungeon.xW/2), dungeon.loc.getY() + (dungeon.h/2), dungeon.loc.getZ() + (dungeon.zW/2)).distance(e.getPlayer().getLocation()) <= 30) {
						
						dungeon.showBorders(e.getPlayer());
						
					}
					
				}
				
			}

		}

	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {

		String[] strLoc = MainPlugin.getConfig().getString(e.getPlayer().getUniqueId().toString()).split(",");
		e.setRespawnLocation(new Location(Bukkit.getServer().getWorld(strLoc[0]),(double)Integer.parseInt(strLoc[1]),(double)Integer.parseInt(strLoc[2])+1,(double)Integer.parseInt(strLoc[3])));

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {

		boolean skip = false;
		
		for(StaffMember SM : MainPlugin.staffList) {
			
			if(SM.player == e.getPlayer()) skip = true;
			
		}
		if(!skip && e.getPlayer().hasPermission("minelife.monitor")) MainPlugin.staffList.add(new StaffMember(e.getPlayer()));
		
		for(Plot plot : MainPlugin.plotList) {

			if(plot.removeTime > MainPlugin.resets) {

				if(plot.getRank(e.getPlayer().getUniqueId().toString()).equals("owner")) {

					e.getPlayer().sendMessage(ChatColor.RED + "Your plot at " + ChatColor.GREEN + "(" + plot.x + ", " + plot.y + ", " + plot.z + ")" + ChatColor.RED + " will be deleted in " + (plot.removeTime-MainPlugin.resets)/4.0 + " days should you continue to fail to pay taxes!");

				}

			}

		}

		MainPlugin.getConfig().set(e.getPlayer().getName(), false);

	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

		if(!(e.getEntity() instanceof Player) && (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow)) {

			for(Plot plot : MainPlugin.plotList) {

				if(plot.inPlot((int)e.getEntity().getLocation().getX(),(int)e.getEntity().getLocation().getY(),(int)e.getEntity().getLocation().getZ(),e.getEntity().getLocation().getWorld()) && plot.upgraded) {

					if(e.getDamager() instanceof Player) {
						
						if(!plot.getRank(((Player)e.getDamager()).getUniqueId().toString()).equals("owner") && !plot.getRank(((Player)e.getDamager()).getUniqueId().toString()).equals("resident")) {
							
							e.getDamager().sendMessage(ChatColor.RED + "You cannot attack that here.");
							e.setCancelled(true);
							return;
							
						}
						
					}
					else if(e.getDamager() instanceof Arrow){
						
						if(((Arrow)e.getDamager()).getShooter() instanceof Player) {
							
							if(plot.getRank((((Player)((Arrow)e.getDamager()).getShooter()).getUniqueId().toString())).equals("none")) {
								
								((Player)((Arrow)e.getDamager()).getShooter()).sendMessage(ChatColor.RED + "You cannot attack that here.");
								e.setCancelled(true);
								return;
								
							}
							
						}
						
					}
					
				}
				
				if(plot.inPlot((int)e.getEntity().getLocation().getX(),(int)e.getEntity().getLocation().getY(),(int)e.getEntity().getLocation().getZ(),e.getEntity().getLocation().getWorld())) {
					
					if(e.getEntity() instanceof ItemFrame || e.getEntity() instanceof Minecart || e.getEntity() instanceof ArmorStand) {
						
						if(e.getDamager() instanceof Player) {
							
							if(plot.getRank(((Player)e.getDamager()).getUniqueId().toString()).equals("none")) {
								
								((Player)e.getDamager()).sendMessage(ChatColor.RED + "You cannot attack that here.");
								e.setCancelled(true);
								return;
								
							}
							
						}
						else if(e.getDamager() instanceof Arrow) {
							
							if(((Arrow)e.getDamager()).getShooter() instanceof Player) {
								
								if(plot.getRank(((Player)((Arrow)e.getDamager()).getShooter()).getUniqueId().toString()).equals("none")) {
									
									((Player)((Arrow)e.getDamager()).getShooter()).sendMessage(ChatColor.RED + "You cannot attack that here.");
									e.setCancelled(true);
									return;
									
								}
							
							}
							
						}
						
					}
					
				}

			}

			for(AdminTown town : MainPlugin.adminTownList) {
				
				if(town.inTown(e.getEntity().getLocation())) {
					
					String playerRank = "";
					if(e.getDamager() instanceof Player) {
						
						playerRank = town.getRank(((Player) e.getDamager()).getUniqueId().toString());
						
					}
					if(e.getDamager() instanceof Arrow) {
						
						if(((Arrow) e.getDamager()).getShooter() instanceof Player) {
							
							Player attacker = (Player) ((Arrow) e.getDamager()).getShooter();
							playerRank = town.getRank(attacker.getUniqueId().toString());
							
						}
						else {
							
							e.setCancelled(true);
							return;
							
						}
						
					}
					if(playerRank.equalsIgnoreCase("owner") || playerRank.equalsIgnoreCase("manager") || playerRank.equalsIgnoreCase("builder")) {
						
						return;
						
					}
					if(e.getEntity() instanceof ItemFrame || e.getEntity() instanceof ArmorStand) { 
						e.setCancelled(true);
					}
					
				}
				
			}
			
		}
		else if((e.getDamager() instanceof Player || (e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player)) && e.getEntity() instanceof Player) {
			
			for(AdminTown town : MainPlugin.adminTownList) {
				
				if(town.inTown(e.getEntity().getLocation())) {
					
					e.setCancelled(true);
					return;
					
				}
				
			}
			
		}

	}

	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent e) {

		Plot pistonPlot = null;
		Town pistonTown = null;
		
		for(Town town : MainPlugin.townList) {

			if(town.inTown(e.getBlock().getLocation())) {

				pistonTown = town;

			}

		}
		for(Plot plot : MainPlugin.plotList) {

			if(plot.inPlot(e.getBlock().getX(), e.getBlock().getY(), e.getBlock().getZ(), e.getBlock().getWorld())) {

				pistonPlot = plot;

			}

		}

		BlockFace face = e.getDirection();
		int moveX = 0;
		int moveY = 0;
		int moveZ = 0;
		if(face == BlockFace.DOWN) {

			moveY = -1;

		}
		else if(face == BlockFace.UP) {

			moveY = 1;

		}
		else if(face == BlockFace.NORTH) {

			moveZ = -1;

		}
		else if(face == BlockFace.SOUTH) {

			moveZ = 1;

		}
		else if(face == BlockFace.EAST) {

			moveX = 1;

		}
		else if(face == BlockFace.WEST) {

			moveX = -1;

		}

		for(Block block : e.getBlocks()) {

			Town raidedTown = null;
			Plot raidedPlot = null;
			Location loc = block.getLocation();
			Location newLoc = new Location(loc.getWorld(),loc.getX() + moveX,loc.getY() + moveY,loc.getZ() + moveZ);
			
			for(Town town : MainPlugin.townList) {

				if(town.inTown(newLoc)) {
					
					if(town != pistonTown) {
						
						if(pistonTown != null) {
						
							if(pistonTown.owner.equals(town.owner)) {
									
								return;
									
							}
							
						}
						raidedTown = town;
						break;
						
					}
					
				}

			}
			
			if(raidedTown == null) {

				for(Plot plot : MainPlugin.plotList) {

					if(plot.inPlot((int)loc.getX() + moveX, (int)loc.getY() + moveY, (int)loc.getZ() + moveZ, loc.getWorld())) {
						
						if(plot != pistonPlot) {
							
							if(pistonPlot != null) {
								
								if(pistonPlot.owner.equals(plot.owner)) {
									
									return;
									
								}
								
							}
							raidedPlot = plot;
							break;
							
						}
						
					}

				}

			}
			
			if(raidedPlot != null || raidedTown != null) {
				
				final Plot plotRaided = raidedPlot;
				final Town townRaided = raidedTown;

				for(Player player : Bukkit.getOnlinePlayers()) {

					if(player.getLocation().distance(e.getBlock().getLocation()) <= 100) {

						player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1, 1);

					}

				}
				
				int raidDamage = 0;
				for(Block raidingBlock : e.getBlocks()) {
					
					if(raidingBlock.getType() == Material.GOLD_BLOCK) {
						
						raidDamage = 384;
						break;
						
					}
					else if(raidingBlock.getType() == Material.DIAMOND_BLOCK && raidDamage < 384) {
						
						raidDamage = 288;
						
					}
					else if(raidingBlock.getType() == Material.IRON_BLOCK && raidDamage < 288) {
						
						raidDamage = 216;
						
					}
					else if(raidingBlock.getType() == Material.COBBLESTONE && raidDamage < 216) {
						
						raidDamage = 144;
						
					}
					else if((raidingBlock.getType() == Material.LOG || raidingBlock.getType() == Material.LOG_2) && raidDamage < 144) {
						
						raidDamage = 72;
						
					}
					
				}
				
				if(raidDamage > 0) {
					
					if(raidedPlot != null) {
						
						if(!MainPlugin.plotRaidCooldownList.contains(raidedPlot)) {
							
							raidedPlot.energy -= raidDamage;
							if(raidedPlot.energy <= 0) {
								
								MainPlugin.plotList.remove(raidedPlot);
								
							}
							MainPlugin.plotRaidCooldownList.add(plotRaided);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
								public void run() {
									MainPlugin.plotRaidCooldownList.remove(plotRaided);
								}
							}, 1200);
							
						}
						
					}
					else {
						
						if(!MainPlugin.townRaidCooldownList.contains(raidedTown)) {
							
							if(raidedTown.stockpileEnergy > 0) {
								
								raidedTown.stockpileEnergy -= raidDamage;
								if(raidedTown.stockpileEnergy < 0) raidedTown.stockpileEnergy = 0;
								
							}
							else {
								
								raidedTown.energy -= raidDamage;
								if(raidedTown.energy <= 0) {
									
									MainPlugin.townList.remove(raidedTown);
									
								}
								raidedTown.radius = (int) ((0.00000000016*Math.pow(raidedTown.energy,2)) + (0.00004*raidedTown.energy));
								
							}
							MainPlugin.townRaidCooldownList.add(townRaided);
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(MainPlugin, new Runnable() {
								public void run() {
									MainPlugin.townRaidCooldownList.remove(townRaided);
								}
							}, 1200);
							
						}
						
					}
					
				}

				e.setCancelled(true);
				return;
				
			}

		}

		Location loc = e.getBlock().getLocation();
		Location newLoc = new Location(loc.getWorld(),loc.getX() + moveX,loc.getY() + moveY,loc.getZ() + moveZ);
		for(Town town : MainPlugin.townList) {

			if(town.inTown(newLoc) && !town.inTown(loc)) {

				e.setCancelled(true);
				return;

			}

		}
		for(Plot plot : MainPlugin.plotList) {

			if(plot.inPlot((int) newLoc.getX(), (int) newLoc.getY(), (int) newLoc.getZ(), newLoc.getWorld()) && !plot.inPlot((int) loc.getX(), (int) loc.getY(), (int) loc.getZ(), loc.getWorld())) {

				e.setCancelled(true);
				return;

			}

		}

	}
	
	@EventHandler
	public void onPistonRetract(BlockPistonRetractEvent e) {
		
		if(e.isSticky()) {
			
			Plot pistonPlot = null;
			for(Plot plot : MainPlugin.plotList) {
				
				if(plot.inPlot(e.getBlock().getLocation().getBlockX(), e.getBlock().getLocation().getBlockY(), e.getBlock().getLocation().getBlockZ(), e.getBlock().getLocation().getWorld())) {
					
					pistonPlot = plot;
					break;
					
				}
				
			}
			
			for(Block block : e.getBlocks()) {
				
				for(Plot plot : MainPlugin.plotList) {
					
					if(plot.inPlot(block.getLocation().getBlockX(), block.getLocation().getBlockY(), block.getLocation().getBlockZ(), block.getLocation().getWorld())) {
						
						if(!plot.owner.equalsIgnoreCase(pistonPlot.owner)) {
							
							e.setCancelled(true);
							return;
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}

	@EventHandler
	public void onArrowHit(ProjectileHitEvent e) {

		if(e.getEntity() instanceof Arrow) {

			if(e.getEntity().getShooter() instanceof Player) {

				if(MainPlugin.getConfig().getBoolean(((Player) e.getEntity().getShooter()).getName())) {

					Plot hitPlot = null;
					Town hitTown = null;
					boolean removePlot = false;
					boolean removeTown = false;

					for(Town town : MainPlugin.townList) {

						if(town.inTown(e.getEntity().getLocation()) && town.getRank(((Player) e.getEntity().getShooter()).getUniqueId().toString()).equals("none")) {

							hitTown = town;
							if(town.stockpileEnergy > 0) {

								town.stockpileEnergy -= 3;
								if(town.stockpileEnergy < 0) {

									town.stockpileEnergy = 0;

								}
								
								((Player) e.getEntity().getShooter()).sendMessage("The town's stockpile is " + town.stockpileEnergy);

							}
							else {

								town.energy -= 3;
								if(town.energy <= 0) {

									removeTown = true;

								}
								
								((Player) e.getEntity().getShooter()).sendMessage("The town's energy is " + town.energy);
								town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));

							}
							e.getEntity().remove();
							break;
							
						}

					}
					if(removeTown) MainPlugin.townList.remove(hitTown);

					if(hitTown == null) {

						for(Plot plot : MainPlugin.plotList) {

							if(plot.inPlot((int) e.getEntity().getLocation().getX(), (int) e.getEntity().getLocation().getY(), (int) e.getEntity().getLocation().getZ(), e.getEntity().getLocation().getWorld()) && plot.getRank(((Player) e.getEntity().getShooter()).getUniqueId().toString()).equals("none")) {

								hitPlot = plot;
								plot.energy -= 3;
								if(plot.energy <= 0) {

									removePlot = true;

								}
								((Player) e.getEntity().getShooter()).sendMessage("The plot's energy is " + plot.energy);
								e.getEntity().remove();
								break;

							}

						}
						if(removePlot) MainPlugin.plotList.remove(hitPlot);

					}

				}

			}
			else {

				Plot hitPlot = null;
				Town hitTown = null;
				boolean removePlot = false;
				boolean removeTown = false;

				for(Town town : MainPlugin.townList) {

					if(town.inTown(e.getEntity().getLocation())) {

						hitTown = town;
						if(town.stockpileEnergy > 0) {

							town.stockpileEnergy -= 1;
							if(town.stockpileEnergy < 0) {

								town.stockpileEnergy = 0;

							}

						}
						else {

							town.energy -= 1;
							if(town.energy <= 0) {

								removeTown = true;

							}

						}
						e.getEntity().remove();
						break;
						
					}

				}
				if(removeTown) MainPlugin.townList.remove(hitTown);

				if(hitTown == null) {

					for(Plot plot : MainPlugin.plotList) {

						if(plot.inPlot((int) e.getEntity().getLocation().getX(), (int) e.getEntity().getLocation().getY(), (int) e.getEntity().getLocation().getZ(), e.getEntity().getLocation().getWorld())) {

							hitPlot = plot;
							plot.energy -= 1;
							if(plot.energy <= 0) {

								removePlot = true;

							}
							e.getEntity().remove();
							break;

						}

					}
					if(removePlot) MainPlugin.plotList.remove(hitPlot);

				}

			}

		}

	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {

		if(e.getEntity() instanceof ElderGuardian) {

			e.getDrops().clear();

		}

	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
		e.setCancelled(true);
		if(MainPlugin.muteList.contains(e.getPlayer().getName())) {
			
			e.getPlayer().sendMessage(ChatColor.RED + "You have been muted.");
			return;
			
		}
		
		ChatColor nameColor = ChatColor.YELLOW;
		if(e.getPlayer().hasPermission("voice.darkPurple")) nameColor = ChatColor.DARK_PURPLE;
		else if(e.getPlayer().hasPermission("voice.lightPurple")) nameColor = ChatColor.LIGHT_PURPLE;
		else if(e.getPlayer().hasPermission("voice.darkRed")) nameColor = ChatColor.DARK_RED;
		else if(e.getPlayer().hasPermission("voice.lightRed")) nameColor = ChatColor.RED;
		else if(e.getPlayer().hasPermission("voice.darkGreen")) nameColor = ChatColor.DARK_GREEN;
		else if(e.getPlayer().hasPermission("voice.lightGreen")) nameColor = ChatColor.GREEN;
		else if(e.getPlayer().hasPermission("voice.darkBlue")) nameColor = ChatColor.DARK_BLUE;
		else if(e.getPlayer().hasPermission("voice.lightBlue")) nameColor = ChatColor.BLUE;
		else if(e.getPlayer().hasPermission("voice.darkAqua")) nameColor = ChatColor.DARK_AQUA;
		else if(e.getPlayer().hasPermission("voice.lightAqua")) nameColor = ChatColor.AQUA;
		else if(e.getPlayer().hasPermission("voice.gold")) nameColor = ChatColor.GOLD;
		else if(e.getPlayer().hasPermission("voice.darkGray")) nameColor = ChatColor.DARK_GRAY;
		else if(e.getPlayer().hasPermission("voice.lightGray")) nameColor = ChatColor.GRAY;
		else if(e.getPlayer().hasPermission("voice.black")) nameColor = ChatColor.BLACK;
		else if(e.getPlayer().hasPermission("voice.white")) nameColor = ChatColor.WHITE;
		
		int blockDistance = 20;
		String msg = e.getMessage();
		Player p = e.getPlayer();
		String name = e.getPlayer().getDisplayName();
		ChatColor color = ChatColor.GRAY;
		Location playerLocation = p.getLocation();
		boolean needToTrim = false;
		boolean someoneHeard = false;
		boolean playerCanHear = false;
		if (msg.startsWith("!"))
		{
			blockDistance = 75;
			color = ChatColor.LIGHT_PURPLE;
			needToTrim = true;
		}
		else if (msg.startsWith("*"))
		{
			blockDistance = 0;
			color = ChatColor.WHITE;
			needToTrim = true;
		}
		if (needToTrim) {
			msg = msg.substring(1);
		}
		String msg1 = nameColor + name + ChatColor.YELLOW + ": " + color + msg;
		if (p.hasPermission("voice.color")) {
			msg1 = ChatColor.translateAlternateColorCodes('&', msg1);
		}
		Set<Player> players = e.getRecipients();
		players.remove(p);
		for (Player o : players)
		{
			playerCanHear = (p.getWorld() == o.getWorld()) && (
					o.getLocation().distance(playerLocation) <= 
					blockDistance || blockDistance == 0);
			if ((blockDistance == 0) || (playerCanHear))
			{
				o.sendMessage(msg1);
				someoneHeard = true;
			}
		}
		if (!someoneHeard) {
			p.sendMessage(ChatColor.RED + "Nobody can hear you!");
		} else {
			p.sendMessage(msg1);
		}
		if(color == ChatColor.GRAY) msg1 = "[Local] " + msg1;
		if(color == ChatColor.LIGHT_PURPLE) msg1 = "[Shout] " + msg1;
		if(color == ChatColor.WHITE) msg1 = "[Global] " + msg1;
		MainPlugin.getServer().getConsoleSender().sendMessage(msg1);
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if(e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL && e.getCause() != PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) return;
		Location to = e.getTo();
		for(Plot plot : MainPlugin.plotList) {

			if(plot.inPlot((int) to.getX(), (int) to.getY(), (int) to.getZ(), to.getWorld())) {

				if(plot.getRank(e.getPlayer().getUniqueId().toString()).equals("none")) {

					e.setCancelled(true);
					return;

				}

			}

		}
		for(DungeonClaim dungeon : MainPlugin.dungeonList) {
			
			if(dungeon.inClaim(to)) {
				
				e.setCancelled(true);
				return;
				
			}
			
		}

	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		
		Town townToRemove = null;
		
		for(Town town : MainPlugin.townList) {
			
			if(town.location.distance(e.getLocation()) <= town.radius + 30) e.blockList().clear();
			if(town.location.distance(e.getLocation()) <= town.radius + 5) {
				
				if(town.stockpileEnergy > 0) {
					town.stockpileEnergy -= 250;
					if(town.stockpileEnergy < 0) town.stockpileEnergy = 0;
				}
				else {
					town.energy -= 250;
					town.radius = (int) ((0.00000000016*Math.pow(town.energy,2)) + (0.00004*town.energy));
					if(town.energy <= 0) townToRemove = town;
				}
				break;
				
			}
			
		}
		
		ArrayList<Block> blocksToKeep = new ArrayList<Block>();
		
		for(Plot plot : MainPlugin.plotList) {
			
			for(Block block : e.blockList()) {
				
				if(plot.inPlot(block.getX(), block.getY(), block.getZ(), block.getWorld())) {
					
					blocksToKeep.add(block);
					
				}
				
			}
			
		}
		for(Block block : blocksToKeep) {
			
			e.blockList().remove(block);
			
		}
		
		MainPlugin.townList.remove(townToRemove);
		
	}
	
	@EventHandler
	public void onTrample(PlayerInteractEvent e) {
		
		if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL) {
			
			for(AdminTown town : MainPlugin.adminTownList) {
				
				if(town.inTown(e.getClickedBlock().getLocation())) {
					
					e.setCancelled(true);
					break;
					
				}
				
			}
			
		}
		
	}
	
//	@EventHandler
//	public void explodeHeight(EntityExplodeEvent e) {
//		
//        if(e.getEntityType() == EntityType.PRIMED_TNT) {
//        	
//        	Town townToRemove = null;
//        	
//        	for(Block block : e.blockList()) {
//        		
//        		for(Town town : MainPlugin.townList) {
//        			
//        			if(town.inTown(block.getLocation())) {
//        				
//        				if(town.stockpileEnergy > 0) {
//        					
//        					town.stockpileEnergy -= 100;
//        					if(town.stockpileEnergy < 0) {
//        						
//        						town.stockpileEnergy = 0;
//        						
//        					}
//        					
//        				}
//        				else {
//        					
//        					town.energy -= 100;
//        					if(town.energy <= 0) {
//        						
//        						townToRemove = town;
//        						
//        					}
//        					
//        				}
//        				
//        				
//        			}
//        			
//        		}
//        		
//        		MainPlugin.townList.remove(townToRemove);
//        		
//        	}
//            e.blockList().clear();
//        }
//        
//        
//    }

//	@EventHandler()
//	public void enderPearlThrown(PlayerTeleportEvent e) {
//		if (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
//		Location to = e.getTo();
//		if(MainPlugin.murdererList.contains(e.getPlayer())) {
//
//			if(to.getX() > -40 && to.getX() < 65) {
//
//				if(to.getZ() > 250 && to.getZ() < 355) {
//
//					e.setCancelled(true);
//					e.getPlayer().sendMessage(ChatColor.RED + "You have just killed someone. You cannot enter spawn right now.");
//					return;
//
//				}
//
//			}
//
//		}
//
//	}

	//	@EventHandler
	//	public void onPlayerAnimation(PlayerAnimationEvent e) {
	//		
	//		if(e.getAnimationType() == PlayerAnimationType.ARM_SWING) {
	//			
	//			if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.REDSTONE_TORCH_ON) {
	//				
	//				for(Plot plot : MainPlugin.plotList) {
	//					
	//					if(plot.inPlot((int)e.getPlayer().getLocation().getX(), (int)e.getPlayer().getLocation().getY(), (int)e.getPlayer().getLocation().getZ(), e.getPlayer().getLocation().getWorld())) {
	//						
	//						String upgraded = "No";
	//						if(plot.upgraded) upgraded = "Yes";
	//						e.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Claim Info\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + plot.ownerName + "\nEnergy: [" + plot.energy + "/50000]" + ChatColor.GREEN + "\nPvE Upgraded: " + upgraded + ChatColor.GOLD + "\nResidents: " + ChatColor.WHITE + plot.residents);
	//						return;
	//						
	//					}
	//					
	//				}
	//				for(Town town : MainPlugin.townList) {
	//					
	//					if(town.inTown(e.getPlayer().getLocation())) {
	//						
	//						e.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Town Info\n" + ChatColor.RESET + "" + ChatColor.GOLD + "Owner: " + town.ownerName + "\nEnergy: [" + town.energy + "/1000000]" + "\nEnergy: [" + town.stockpileEnergy + "/75000]" + "\nRadius: " + town.radius + "\nCitizens: " + ChatColor.WHITE + town.citizens + ChatColor.GOLD + "\nBuilders: " + ChatColor.WHITE + town.builders + ChatColor.GOLD + "\nManagers: " + ChatColor.WHITE + town.managers);
	//						
	//					}
	//					
	//				}
	//				
	//			}
	//			
	//		}
	//		
	//	}

}
