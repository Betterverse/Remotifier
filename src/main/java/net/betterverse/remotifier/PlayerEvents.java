package net.betterverse.remotifier;

import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(hasVotedRecently(event.getPlayer())) return;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Remotifier.Instance, new KickTask(event.getPlayer()), 20L * 60 * Config.Options.KICK.getInt());
		event.getPlayer().sendMessage(Config.Options.WARN_MSG.getString());
	}

	class KickTask implements Runnable {

		private Player _player;

		public KickTask(Player player) {
			_player = player;
		}

		public void run() {
			if(!hasVotedRecently(_player))
			_player.kickPlayer(Config.Options.KICK_MSG.getString());
		}
	}
	
	public boolean hasVotedRecently(Player plr) {
		long now = new Date().getTime();
		long lastVote = Remotifier.Instance.DB.LastVote(plr.getName());
		long diff = now - lastVote;
		if (diff <= 1000 * 60 * 60 * 24) {
			return true;
		}
		lastVote = Remotifier.Instance.DB.LastVoteByIP(plr.getAddress().getAddress().getHostAddress());
		diff = now - lastVote;
		if (diff <= 1000 * 60 * 60 * 24) {
			return true;
		}
		return false;
	}
}
