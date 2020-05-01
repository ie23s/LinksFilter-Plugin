package com.ie23s.minecraft.plugin.linksfilter.utils.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WhiteList implements com.ie23s.minecraft.plugin.linksfilter.model.WhiteList {
    private final MySQLUtil mySQLUtil;

    public WhiteList(MySQLUtil mySQLUtil) {
        this.mySQLUtil = mySQLUtil;
    }

    public void init() throws SQLException {
        mySQLUtil.executeSync("CREATE TABLE IF NOT EXISTS `" + mySQLUtil.getPrefix() + "whitelist` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `host` varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `host` (`host`)\n" +
                ") ENGINE=MyISAM DEFAULT CHARSET=utf8;");

    }

    public boolean addToList(String host) throws SQLException {
        host = mySQLUtil.strip(host);

        ResultSet resultSet = mySQLUtil.executeQuery(
                String.format("SELECT COUNT(*) FROM `" + mySQLUtil.getPrefix() + "whitelist` WHERE `host` = '%s'",
                        mySQLUtil.strip(host))
        );

        if (resultSet.next() && resultSet.getInt("COUNT(*)") != 0)
            return false;
        mySQLUtil.executeSync(
                String.format("INSERT INTO `" + mySQLUtil.getPrefix() + "whitelist`(`id`, `host`) VALUES (null,'%s')", mySQLUtil.strip(host))
        );
        return true;
    }

    public boolean removeFromList(String host) throws SQLException {
        host = mySQLUtil.strip(host);
        ResultSet resultSet = mySQLUtil.executeQuery(
                String.format("SELECT COUNT(*) FROM `" + mySQLUtil.getPrefix() + "whitelist` WHERE `host` = '%s'", mySQLUtil.strip(host))
        );
        if (resultSet.next() && resultSet.getInt("COUNT(*)") == 0)
            return false;
        mySQLUtil.executeSync(
                String.format("DELETE FROM `" + mySQLUtil.getPrefix() + "whitelist` WHERE `host` = '%s'", mySQLUtil.strip(host))
        );
        return true;
    }

    public boolean findInList(String host, String... subHosts) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM `" + mySQLUtil.getPrefix() + "whitelist` WHERE ");
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
