package com.ie23s.minecraft.plugin.linksfilter.utils.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlackList implements com.ie23s.minecraft.plugin.linksfilter.model.BlackList {
	private final MySQLUtil mySQLUtil;

	public BlackList(MySQLUtil mySQLUtil) {
		this.mySQLUtil = mySQLUtil;
	}

	@Override
	public void init() throws SQLException {
		mySQLUtil.executeSync("CREATE TABLE IF NOT EXISTS `" + mySQLUtil.getPrefix() + "blacklist` (\n" +
				"  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
				"  `host` varchar(255) NOT NULL,\n" +
				"  PRIMARY KEY (`id`),\n" +
				"  UNIQUE KEY `host` (`host`)\n" +
				") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
	}


	@Override
	public boolean addToList(String host) throws SQLException {
		host = mySQLUtil.strip(host);
		ResultSet resultSet = mySQLUtil.executeQuery(
				String.format("SELECT COUNT(*) FROM `" + mySQLUtil.getPrefix() + "blacklist` WHERE `host` = '%s'",
						mySQLUtil.strip(host))
		);

		if (resultSet.next() && resultSet.getInt("COUNT(*)") != 0)
			return false;
		mySQLUtil.executeSync(
				String.format("INSERT INTO `" + mySQLUtil.getPrefix() + "blacklist`(`id`, `host`)" +
						"VALUES (null,'%s')", mySQLUtil.strip(host))
		);
		return true;
	}

	//0 - OK, 1 - isn't exists, 2 - ERROR
	@Override
	public boolean removeFromList(String host) throws SQLException {
		host = mySQLUtil.strip(host);
		ResultSet resultSet = mySQLUtil.executeQuery(
				String.format("SELECT COUNT(*) FROM `" + mySQLUtil.getPrefix() + "blacklist` WHERE `host` = '%s'",
						mySQLUtil.strip(host))
		);
		if (resultSet.next() && resultSet.getInt("COUNT(*)") == 0)
			return false;
		mySQLUtil.executeSync(
				String.format("DELETE FROM `" + mySQLUtil.getPrefix() + "blacklist` WHERE `host` = '%s'",
						mySQLUtil.strip(host))
		);

		return true;
	}

	//0 - is exists, 1 - isn't exists, 2 - ERROR
	@Override
	public boolean findInList(String host, String... subHosts) throws SQLException {
		StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM `" + mySQLUtil.getPrefix() + "blacklist` WHERE ");
		if (subHosts != null && subHosts.length > 0) {
			for (String subHost :
					subHosts) {
				query.append(String.format("`host` = '%s' OR ", mySQLUtil.strip(subHost)));
			}
		}
		query.append(String.format("`host` = '%s'", mySQLUtil.strip(host)));
		ResultSet resultSet = mySQLUtil.executeQuery(query.toString());
		return resultSet.next() && resultSet.getInt("COUNT(*)") != 0;
	}
}
