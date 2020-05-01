package com.ie23s.minecraft.plugin.linksfilter.utils.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public class ShortLink extends com.ie23s.minecraft.plugin.linksfilter.model.ShortLink {
    private final MySQLUtil mySQLUtil;
    private final String url;

    public ShortLink(MySQLUtil mySQLUtil, String url) {
        this.mySQLUtil = mySQLUtil;
        this.url = url;
    }

    @Override
    public void init() throws SQLException {
        mySQLUtil.executeSync("CREATE TABLE IF NOT EXISTS `" + mySQLUtil.getPrefix() + "shortlink` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `url` text NOT NULL,\n" +
                "  `username` varchar(255) NOT NULL,\n" +
                "  `time` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=MyISAM DEFAULT CHARSET=utf8;");
    }

    @Override
    public String cutLink(String link, String name) throws SQLException {
        ResultSet resultSet = mySQLUtil.executeQuery("SELECT MIN(`t1`.ID + 1) AS `nextID` " +
                "FROM `" + mySQLUtil.getPrefix() + "shortlink` `t1` " +
                "LEFT JOIN `" + mySQLUtil.getPrefix() + "shortlink` `t2` ON `t1`.ID + 1 = `t2`.ID " +
                "WHERE `t2`.ID IS NULL;");
        resultSet.next();
        int id = resultSet.getInt("nextID");
        mySQLUtil.executeSync("INSERT INTO `" + mySQLUtil.getPrefix() + "shortlink`(`id`, `url`, `username`, `time`)" +
                "VALUES (" + id + ", '" + mySQLUtil.strip(link) + "', '" + mySQLUtil.strip(link) + "'," +
                Instant.now().getEpochSecond() + ")");
        return url.replace("{id}", Integer.toHexString(id));
    }
}
