package net.betterverse.remotifier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sun.plugin2.main.server.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;
import com.vexsoftware.votifier.Votifier;

import static net.betterverse.remotifier.Config.Options;

public class PollTask implements Runnable {
	private Method method;

	public PollTask() {
		Methods.setMethod(Remotifier.Instance.getServer().getPluginManager());
		method = Methods.getMethod();
	}

	public void run() {
		ResultSet res = Remotifier.Instance.DB.SelectNew();
		while (Iterate(res)) {
			String player = GetString(res, "player");
			method.getAccount(player).add(Options.MONEY.getInt());

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
		}
		catch (SQLException ignored) {}
		return false;
	}

	public String GetString(ResultSet set, String key) {
		try {
			return set.getString(key);
		}
		catch (SQLException ignored) {}
		return "";
	}
}
