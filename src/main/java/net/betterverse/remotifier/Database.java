package net.betterverse.remotifier;

import java.sql.ResultSet;
import java.sql.SQLException;

import static net.betterverse.remotifier.Config.Options;

public class Database {
	MySQLConnection db;

	public Database() {
		try {
			db = new MySQLConnection(
				Options.MYSQL_HOST.getString(),
				Options.MYSQL_PORT.getInt(),
				Options.MYSQL_DATABASE.getString(),
				Options.MYSQL_USER.getString(),
				Options.MYSQL_PASSWORD.getString());
		} catch (Exception e) {
			System.out.println("Remotifier failed to connect to MySQL database");
		}

		if (!db.connect()) {
			System.out.println("Remotifier failed to connect to MySQL database");
		}

		if (db.tableExists(Options.MYSQL_DATABASE.getString(),"votes")) return;

		Query(
			"CREATE TABLE `votes` (" +
				"`time` DATETIME," +
				"`ip` VARCHAR(50)," +
				"`player` VARCHAR(50)," +
				"`id` INTEGER AUTO_INCREMENT PRIMARY KEY" +
				")", true);
		System.out.println("Votes table created.");

	}

	public ResultSet SelectNew() {
		String query = "SELECT player FROM votes WHERE time > NOW() - INTERVAL 60 SECOND";
		return Query(query, false);
	}

	public  ResultSet Query(String query, boolean modify) {
		try {
			return  db.executeQuery(query, modify);
		} catch (SQLException e) {
			if (!db.connect()) {
				return null;
			}
			Query(query, modify);
		}
		return null;
	}

	public void Insert(String player, String ip) {
		String query = String.format("INSERT INTO votes (player, time, ip) VALUES ('%s',NOW(), '%s')", player, ip);
		System.out.println("Pushed " + player + "'s vote.");
		Query(query, true);
	}

	public long LastVote(String player) {
		String query = "SELECT MAX(unix_timestamp(`time`)) AS last FROM votes WHERE player = '" + player +"'";
		ResultSet set = Query(query, false);
		if (!Iterate(set)) {
			return 0;
		}
		try {
			return set.getLong("last");
		} catch (Exception ignored) {}
		return 0;
	}

	public long LastVoteByIP(String ip) {
		String query = "SELECT MAX(unix_timestamp(`time`)) AS last FROM votes WHERE ip = '" + ip +"'";
		ResultSet set = Query(query, false);
		if (!Iterate(set)) {
			return 0;
		}
		try {
			return set.getLong("last");
		} catch (Exception ignored) {}
		return 0;
	}

	public int TimesVoted(String player) {
		String query = "SELECT COUNT(player) FROM votes GROUP BY player WHERE player ='" + player + "'";
		ResultSet rs = Query(query, false);
		int result = 0;
		while (Iterate(rs)) {
			try {result = rs.getInt(0);
			} catch (SQLException ignored) {};
		}
		return result;
	}

	private boolean Iterate(ResultSet set) {
		if (set==null) {
			return false;
		}
		try {
			return set.next();
		} catch (SQLException ignored) {}
		return false;
	}
}
