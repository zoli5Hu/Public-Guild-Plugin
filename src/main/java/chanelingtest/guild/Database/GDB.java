package chanelingtest.guild.Database;

import chanelingtest.guild.Guild;
import chanelingtest.guild.Lib.Storage;
import chanelingtest.guild.Objects.GuildObject;
import com.velocitypowered.api.proxy.Player;
import org.slf4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GDB {
    private static Connection connection;
    private static Logger logger;

    public GDB(Logger logger) {
        GDB.logger = logger;
    }

    public static void initDatabaseConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/test";
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
    public void createTableIfNotExist() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS guilds (name VARCHAR(16) NOT NULL, owner VARCHAR(16) NOT NULL PRIMARY KEY, xp INT NOT NULL, members INT NOT NULL)";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                stmt.execute(createTableSQL);
                logger.info("Players table checked/created successfully.");
            } else {
                logger.error("Failed to create players table: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to create players table.", e);
        }
    }

    public void addGuild(String name, String owner, int xp, int members) {
        String addGuildSQL = "INSERT INTO guilds (name, owner, xp, members) VALUES ('" + name + "', '" + owner + "', " + xp + ", " + members + ")";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                stmt.execute(addGuildSQL);
                logger.info("Guild added successfully.");
            } else {
                logger.error("Failed to add guild: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to add guild.", e);
        }
    }

    public void addplayerguild(String  playername, String guildname) {
        String update = "UPDATE players SET guild = '" + guildname + "' WHERE username = '" + playername + "'";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                stmt.execute(update);
                logger.info("Player added to guild successfully.");
            } else {
                logger.error("Failed to add player to guild: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to add player to guild.", e);
        }
    }
    public void removeplayerguild(String  playername) {
        String update = "UPDATE players SET guild = NULL WHERE username = '" + playername + "'";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                stmt.execute(update);
                logger.info("Player removed from guild successfully.");
            } else {
                logger.error("Failed to remove player from guild: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to remove player from guild.", e);
        }
    }

    public void removelallguildlabel(String guildname){
        String update = "UPDATE players SET guild = NULL WHERE guild = '" + guildname + "'";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                stmt.execute(update);
                logger.info("All players removed from guild successfully.");
            } else {
                logger.error("Failed to remove all players from guild: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to remove all players from guild.", e);
        }
    }

    public void updateGxpandGmemberp(String guildname, int xp, boolean add) {
        String update = "UPDATE guilds SET xp = xp " + (add ? "+" : "-") + " " + xp + ", members = members " + (add ? "+" : "-") + " 1 WHERE name = '" + guildname + "'";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                stmt.execute(update);
                logger.info("Guild xp and member count updated successfully.");
            } else {
                logger.error("Failed to update guild xp and member count: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to update guild xp and member count.", e);
        }
    }

    public void removeguild(String guildname){
        String update = "DELETE FROM guilds WHERE name = '" + guildname + "'";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                stmt.execute(update);
                logger.info("Guild removed successfully.");
            } else {
                logger.error("Failed to remove guild: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to remove guild.", e);
        }
    }

    public void loaddata() {
        String getxpSQL = "SELECT * FROM guilds";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(getxpSQL);
                while (rs.next()) {
                    String name = rs.getString("name");
                    String owner = rs.getString("owner");
                    int xp = rs.getInt("xp");
                    int members = rs.getInt("members");
                    System.out.println("Guild: " + name + ", Owner: " + owner + ", XP: " + xp + ", Members: " + members);
                    try {
                        GuildObject guild = new GuildObject(name, owner, xp);
                        Storage.guilds.add(guild);
                        List<String> players = getplayerforplayerdatabase(name, connection);
                        if(players != null){
                            guild.setMembers(players);
                        }else {
                            logger.error("Failed to load players for guild: nulllllll" + name);
                        }
                    } catch (Exception e) {
                        logger.error("Failed to load guild: " + name, e);
                    }




                    // Process the data as needed
                    System.out.println("Guild: " + name + ", Owner: " + owner + ", XP: " + xp + ", Members: " + members);
                }
                logger.info("Guild data loaded successfully.");
            } else {
                logger.error("Failed to load guild data: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to load guild data.", e);
        }
    }

    private List<String> getplayerforplayerdatabase(String guildname, Connection connection) {
        String getxpSQL = "SELECT * FROM players WHERE guild = ?";
        try {
            if (connection != null) {
                PreparedStatement pstmt = connection.prepareStatement(getxpSQL);
                pstmt.setString(1, guildname);
                ResultSet rs = pstmt.executeQuery();
                List<String> players = new ArrayList<>();
                while (rs.next()) {
                    String name = rs.getString("username");
                    // Process the data as needed
//                    System.out.println("Player: " + name);
                    players.add(name);
                }
                return players;
            } else {
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve players for guild: " + guildname);
        }
        return null;

    }

    public void updateGXpsetvalu(String guildname, int xp) {
        String update = "UPDATE guilds SET xp = " + xp + " WHERE name = '" + guildname + "'";
        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                stmt.execute(update);
                logger.info("Guild xp updated successfully.");
            } else {
                logger.error("Failed to update guild xp: Database connection is null.");
            }
        } catch (SQLException e) {
            logger.error("Failed to update guild xp.", e);
        }
    }
}
