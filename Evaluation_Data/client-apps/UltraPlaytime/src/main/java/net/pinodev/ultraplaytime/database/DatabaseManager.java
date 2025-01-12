package net.pinodev.ultraplaytime.database;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import static net.pinodev.ultraplaytime.UltraPlaytime.logger;
import static net.pinodev.ultraplaytime.UltraPlaytime.SettingsYML;
import static net.pinodev.ultraplaytime.database.queries.Statements.CREATE_TABLE_NO_LEADERBOARD;
import static net.pinodev.ultraplaytime.database.queries.Statements.CREATE_TABLE_USERS;


public interface DatabaseManager {

    /*
     Get the database type running ( H2 , MYSQL, MARIADB )
    */
    String getDatabaseType();

    /*
    Get the HikariDataSource properties and Drivers
     */
    HikariDataSource getHikariDataSource();

    /*
    Set the connection Pool extra properties defined in config
     */
    default void setHikariProperties(HikariDataSource dataSource){
        final ConfigurationSection properties = SettingsYML.getFileConfiguration().getConfigurationSection("Database.properties");
        if(properties != null){
            for(String key : properties.getKeys(false)){
                dataSource.addDataSourceProperty(key, properties.getString(key));
            }
        }

    }

    /*
    Test the connection towards the database on Startup
     */
    default Boolean testConnection(){
        Connection connection;
        try{
            connection = getConnection();
            logger.info("DB | Correctly established connection to the database - > " + getDatabaseType());
            connection.close();
            return true;
        }catch (SQLException sqlException){
            logger.log(Level.SEVERE, "ERROR ! Failed to connect to the database - > " + getDatabaseType());
            sqlException.printStackTrace();
            return false;
        }
    }

    /*
    Generating database tables
    */
    default void databaseInit(){
        try(Connection connection = getConnection();
            PreparedStatement tableUsers = connection.prepareStatement(CREATE_TABLE_USERS.getStatement());
            PreparedStatement tableNoLeaderboard = connection.prepareStatement(CREATE_TABLE_NO_LEADERBOARD.getStatement())){
            tableUsers.executeUpdate();
            tableNoLeaderboard.executeUpdate();
        }catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    /*
    Get a connection to the database
     */
    default Connection getConnection() throws SQLException {
        return getHikariDataSource().getConnection();
    }

    /*
    Close resources used to operate on the database
     */
    default void closeConnection(final Connection connection,
                                 final PreparedStatement preparedStatement,
                                 final ResultSet resultSet) {
        try {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    /*
    Close Connection Pool on shutdown or heavy plugin reload
     */
    default void closeHikariPool(){
        if(getHikariDataSource() != null && !getHikariDataSource().isClosed()) getHikariDataSource().close();
    }


}
