package net.betterverse.remotifier;

import java.util.HashMap;
import com.sun.org.apache.xpath.internal.operations.Bool;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.config.Configuration;

public class Config {
	private static Configuration configuration;

	public enum Options {
		MYSQL_HOST("mysql.host", "localhost"),
		MYSQL_PORT("mysql.port", 3306),
		MYSQL_DATABASE("mysql.database", "votingdatabase"),
		MYSQL_USER("mysql.user","root"),
		MYSQL_PASSWORD("mysql.password","password"),
		DEBUG("debug", true),
		MONEY("reward.money",1000),
		ITEM("reward.item",0),
		ITEM_COUNT("reward.itemCount",0),
		MESSAGE("message","%s just voted!"),
		KICK("kick.minutes", 1),
		KICK_MSG("kick.message", "Please vote at xxx url to continue playing"),
		WARN_MSG("warn.message", "You have not voted in the last 24 hours, please vote at xxx.")

		;

		private String path;
		private Object def;

		Options(String path, Object def) {
			this.path = path;
			this.def = def;
		}

		public Object get() {
			if (configuration.getProperty(path) == null) {
				configuration.setProperty(path, def);
			}
			return configuration.getProperty(path);
		}

		public String getString() {
			return configuration.getString(path, def.toString());
		}

		public int getInt() {
			return configuration.getInt(path, (Integer)def);
		}

		public double getDouble() {
			return configuration.getDouble(path, (Double)def);
		}

		public boolean getBoolean() {
			return configuration.getBoolean(path, (Boolean)def);
		}
	}

	public static void Load() {
		configuration = Remotifier.Instance.getConfiguration();
		configuration.load();
		loadDefaults();
		configuration.save();
	}

	private static void loadDefaults() {
		for (Options option : Options.values()) {
			option.get();
		}
	}
}
