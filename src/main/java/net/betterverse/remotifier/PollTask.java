package net.betterverse.remotifier;

import net.betterverse.creditsshop.PlayerListener;
import net.betterverse.remotifier.Config.Options;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PollTask implements Runnable {

	public PollTask() {
	}

	public void run() {
		ResultSet res = Remotifier.Instance.DB.SelectNew();
		while (Iterate(res)) {
			String player = GetString(res, "player");
			Remotifier.economy.depositPlayer(player, Options.MONEY.getInt());

            if (Remotifier.hasCreditsShop() && Options.CREDITS.getInt() > 0) {
                PlayerListener.addCredits(player, Options.CREDITS.getInt());
            }

			String message = String.format(Options.MESSAGE.getString(), player);
			Bukkit.getServer().broadcastMessage(message);
			if (Options.ITEM.getInt() < 1) {
				continue;
			}

			Bukkit.getServer().getPlayer(player).getInventory().addItem(new ItemStack(Options.ITEM.getInt(), Options.ITEM_COUNT.getInt()));
		}
	}

	private boolean Iterate(ResultSet set) {
		if (set == null) {
			return false;
		}
		try {
			return set.next();
		} catch (SQLException ignored) {
		}
		return false;
	}

	public String GetString(ResultSet set, String key) {
		try {
			return set.getString(key);
		} catch (SQLException ignored) {
		}
		return "";
	}
}
