package net.pinodev.ultraplaytime.database.connectors;

import com.zaxxer.hikari.HikariDataSource;
import net.pinodev.ultraplaytime.database.DatabaseManager;

import static net.pinodev.ultraplaytime.configs.files.Settings.*;

public class MariaDB implements DatabaseManager {

    private final HikariDataSource dataSource;
    @Override
    public String getDatabaseType() {
        return "MariaDB";
    }

    @Override
    public HikariDataSource getHikariDataSource() {
        return dataSource;
    }

    public MariaDB(){
        dataSource = new HikariDataSource();
        dataSource.setPoolName("UltraPlaytime-Pool");
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        final String host = DB_HOST.getString();
        final String port = DB_PORT.getString();
        final String database_name = DB_NAME.getString();
        dataSource.setJdbcUrl("jdbc:mariadb://"+host+":"+port+"/"+database_name);
        dataSource.setUsername(DB_USER.getString());
        dataSource.setPassword(DB_PASSWORD.getString());
        dataSource.setConnectionTimeout(DB_CONNECTION_TIMEOUT.getLong());
        dataSource.setIdleTimeout(DB_IDLE_TIMEOUT.getLong());
        dataSource.setMaxLifetime(DB_MAX_LIFETIME.getLong());
        dataSource.setMaximumPoolSize(DB_MAX_POOL_SIZE.getInt());
        setHikariProperties(dataSource);
        if(testConnection()){
            databaseInit();
        }
    }

}
