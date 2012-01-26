package org.blockface.remotifier;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import java.util.Date;

public class PlayerEvents extends PlayerListener{

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        long now = new Date().getTime();
        long lastVote = Remotifier.Instance.DB.LastVote(event.getPlayer().getName());
        long diff = now-lastVote;
        if(diff <= 1000*60*60*24) return;
        lastVote = Remotifier.Instance.DB.LastVoteByIP(event.getPlayer().getAddress().getAddress().getHostAddress());
        diff = now-lastVote;
        if(diff <= 1000*60*60*24) return;
        Bukkit.getScheduler().scheduleSyncDelayedTask(Remotifier.Instance, new KickTask(event.getPlayer()), 20L * 60 * Config.Options.KICK.getInt());
        event.getPlayer().sendMessage(Config.Options.WARN_MSG.getString());
    }

    class KickTask implements Runnable {

        private Player _player;

        public KickTask(Player player) {
            _player = player;
        }

        public void run() {
            _player.kickPlayer(Config.Options.KICK_MSG.getString());
        }
    }
}
