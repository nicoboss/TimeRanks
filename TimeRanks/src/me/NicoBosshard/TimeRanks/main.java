package me.NicoBosshard.TimeRanks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{
	
	long timestamp1;
	long timestamp2;
	int i_index;
	
	  List<String> Players_List = new ArrayList<String>();
	  List<Long> RimeRanks_Time = new ArrayList<Long>();
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		LoadConfig();
		System.out.println("TimeRank v1.3 von NicoBosshard ist nun aktiviert!");
	}
	
	@Override
	public void onDisable() {
		for(Player all:getServer().getOnlinePlayers()){
			long SpielzeitTotal=Spielzeit_Total(all.getName());
			this.getConfig().set("Config.Time.Players." + all.getName(), SpielzeitTotal);
			if(SpielzeitTotal>360000) {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + all.getName() + " group set Stammspieler");
			}
			Players_List.remove(i_index);
			RimeRanks_Time.remove(i_index);
		}
		this.saveConfig();
		
		System.out.println("TimeRank v1.3 ist nun deaktiviert!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

			if(cmd.getName().equalsIgnoreCase("Spielzeit") || cmd.getName().equalsIgnoreCase("sz")) {
				if(args.length==0) {
					if(sender instanceof Player) {
					Player p = (Player) sender;
					long SpielzeitTotal=Spielzeit_Total(p.getName());				
					p.sendMessage(ChatColor.GREEN + "Du spielst schon seit " + SpielzeitTotal / 3600 + "h " + (SpielzeitTotal % 3600) / 60 + "min " + SpielzeitTotal % 60 + "s" + " auf Eldercraft!");				
					return true;
					} else {
						System.out.println("Um /sz in der Konsole auszufuehren muss ein Playername als Argument agegeben werden!");
						return true;
					}
				} else {
					

					String Playername="";
					 if (args.length >= 1) {
						 Playername = args[0];
						 for (int i = 1; i < args.length; i++) {
							 Playername = Playername + " " + args[i];
						  }
						 }
					 
					 Player p = Bukkit.getPlayer(Playername);
					 long SpielzeitTotal=Spielzeit_Total(p.getName());
						
					 sender.sendMessage(ChatColor.GREEN + p.getName() + " spielst schon seit " + SpielzeitTotal / 3600 + "h " + (SpielzeitTotal % 3600) / 60 + "min " + SpielzeitTotal % 60 + "s" + " auf Eldercraft!");
					 return true;
				}		
			}
			return false;	
		}
	
	
	@EventHandler(priority = EventPriority.NORMAL) 
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Players_List.add(p.getName());
		RimeRanks_Time.add(System.currentTimeMillis() / 1000L);
		//e.setJoinMessage(ChatColor.GREEN + p.getName() + ChatColor.WHITE + "hat das Spiel betreten. " + timestamp1);
	}
	
	@EventHandler(priority = EventPriority.NORMAL) 
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		long SpielzeitTotal=Spielzeit_Total(p.getName());	
		
		//e.setQuitMessage("Player " + Players_List.get(i_index) + " hat nach " + (timestamp2 - RimeRanks_Time.get(i_index)) + "h den Server verlassen! Totale Spielzeit: " + SpielzeitTotal);
		this.getConfig().set("Config.Time.Players." + p.getName(), SpielzeitTotal);
		this.saveConfig();
		Players_List.remove(i_index);
		RimeRanks_Time.remove(i_index);
	}
	
	public long Spielzeit_Total(Object Playername) {
		timestamp2 = System.currentTimeMillis() / 1000L;
		i_index = Players_List.indexOf(Playername);
		long OnlineTime = timestamp2 - RimeRanks_Time.get(i_index);
		long OnlineTimeAlt=0;
		
		if(this.getConfig().getInt("Config.Time.Players." + Playername) > 0) {
				OnlineTimeAlt = this.getConfig().getInt("Config.Time.Players." + Playername);
			} else {
				System.out.println("[TimeRanks] Warnung: return OnlineTime => Error oder neuer Spieler?");
				return OnlineTime;
			}
		 
		long OnlineTimeTotal = OnlineTimeAlt + OnlineTime;
		
		if(OnlineTimeTotal>360000) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + Playername + " group set Admin");
		}
		
		return OnlineTimeTotal;
	}
	
	public void setRank() {
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user ");
	}
	
	public void LoadConfig() {
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();		
	}
	
	
	
}