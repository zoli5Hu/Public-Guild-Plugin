package chanelingtest.guild.Database;

import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import org.slf4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XpConnector {
    private static Connection connection;
    private static Logger logger;

    public XpConnector(Logger logger) {
        XpConnector.logger = logger;
    }

    public static void initDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/statsystem";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
            logger.info("Successfully connected to the database.");
        } catch (SQLException e) {
            logger.error("Database connection failed!", e);
        }
    }
    //guild tábla létrehozása, ha még nem létezik
    //név, tulajdonos, xp,tagokszama,

    public int getxpbyname(String name) {
        String getxpSQL = "SELECT xp FROM experience WHERE name = ?";
        try {
            if (connection != null) {
                PreparedStatement pstmt = connection.prepareStatement(getxpSQL);
                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int xp = rs.getInt("xp");
                    logger.info("XP retrieved successfully for name: " + name);
                    return xp;
                } else {
                    logger.warn("No XP found for name: " + name);
                }
            } else {
                logger.error("Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve XP.", e);
        }
        return 0;
    }

    public int allxpbyguild(String guildname){
        int xps = 0;
        for(GuildObject g: Storage.guilds){
            if(g.getName().equals(guildname)){
                for(String s : g.getMembers()){
                    xps+= getxpbyname(s);
                }
            }
        }
        return xps;


    }
}
