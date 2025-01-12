package net.pinodev.ultraplaytime.configs.files;

import java.util.function.Function;

import static net.pinodev.ultraplaytime.UltraPlaytime.SettingsYML;

public enum Settings {
    MULTI_SERVER(Boolean.class, "MultiServer.enabled", SettingsYML.getFileConfiguration()::getBoolean),
    MULTI_SERVER_REWARDS(Boolean.class, "MultiServer.disable_rewards_here", SettingsYML.getFileConfiguration()::getBoolean),
    MULTI_SERVER_LATENCY(Long.class, "MultiServer.network_latency_milliseconds", SettingsYML.getFileConfiguration()::getLong),
    DB_TYPE(String.class, "Database.type", SettingsYML.getFileConfiguration()::getString),
    DB_NAME(String.class, "Database.name", SettingsYML.getFileConfiguration()::getString),
    DB_TABLE_USERDATA(String.class, "Database.table_userdata", SettingsYML.getFileConfiguration()::getString),
    DB_TABLE_NO_LEADERBOARD(String.class, "Database.table_no_leaderboard", SettingsYML.getFileConfiguration()::getString),
    DB_HOST(String.class, "Database.host", SettingsYML.getFileConfiguration()::getString),
    DB_PORT(String.class, "Database.port", SettingsYML.getFileConfiguration()::getString),
    DB_PASSWORD(String.class, "Database.password", SettingsYML.getFileConfiguration()::getString),
    DB_USER(String.class, "Database.user", SettingsYML.getFileConfiguration()::getString),
    DB_CONNECTION_TIMEOUT(Long.class, "Database.connectionTimeout", SettingsYML.getFileConfiguration()::getLong),
    DB_IDLE_TIMEOUT(Long.class, "Database.idleTimeout", SettingsYML.getFileConfiguration()::getLong),
    DB_MAX_LIFETIME(Long.class, "Database.maxLifeTime", SettingsYML.getFileConfiguration()::getLong),
    DB_MAX_POOL_SIZE(Integer.class, "Database.maxPoolSize", SettingsYML.getFileConfiguration()::getInt),
    LEADERBOARD_REFRESH(Integer.class, "Leaderboard.refreshMinutes", SettingsYML.getFileConfiguration()::getInt),
    AUTO_SAVE(Integer.class, "AutoSave.everyMinutes", SettingsYML.getFileConfiguration()::getInt),
    AFK_AFTER(Integer.class, "AntiAFK.afkAfterSeconds", SettingsYML.getFileConfiguration()::getInt);


    private final String key;
    private final Function<String, ?> value;

    Settings(Class<?> type, String key, Function<String, ?> value) {
        this.key = key;
        this.value = value;
    }

    public boolean getBoolean(){
        return (Boolean) value.apply(key);
    }


    public String getString() {
        return (String) value.apply(key);
    }

    public Integer getInt() {
        return (Integer) value.apply(key);
    }

    public Long getLong() {
        return (Long) value.apply(key);
    }

}
