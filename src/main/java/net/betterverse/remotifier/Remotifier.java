package net.betterverse.remotifier;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class Remotifier extends JavaPlugin {
	public static Remotifier Instance;

	public Database DB;

	public void onDisable() {
		System.out.println(this + " is now disabled!");
	}

	public void onEnable() {
		Instance = this;
		Config.Load();
		DB = new Database();
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new PollTask(), 20L*60, 20L*60);
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, new PlayerEvents(), Event.Priority.Low, this);

		System.out.println(this + " is now enabled!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			return false;
		}
		DB.Insert(args[0], Bukkit.getPlayer(args[0]).getAddress().getAddress().getHostAddress());
		sender.sendMessage("Inserted row into table. Should trigger reward soon.");
		return false;
	}
}
