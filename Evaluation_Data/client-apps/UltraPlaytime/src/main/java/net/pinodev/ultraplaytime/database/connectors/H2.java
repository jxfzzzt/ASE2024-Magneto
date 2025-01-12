package net.pinodev.ultraplaytime.database.connectors;

import com.zaxxer.hikari.HikariDataSource;
import net.pinodev.ultraplaytime.database.DatabaseManager;

import static net.pinodev.ultraplaytime.UltraPlaytime.MainInstance;
import static net.pinodev.ultraplaytime.configs.files.Settings.*;
import static net.pinodev.ultraplaytime.configs.files.Settings.DB_MAX_POOL_SIZE;

public class H2 implements DatabaseManager {

    private final HikariDataSource dataSource;

    @Override
    public String getDatabaseType() {
        return "H2";
    }

    @Override
    public HikariDataSource getHikariDataSource() {
        return dataSource;
    }

    public H2() {
        dataSource = new HikariDataSource();
        dataSource.setPoolName("UltraPlaytime-Pool");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:" + MainInstance.getDataFolder().getAbsolutePath() + "/data/h2-playtime;mode=mysql");
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
