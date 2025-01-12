package net.pinodev.ultraplaytime.database.queries;

import static net.pinodev.ultraplaytime.configs.files.Settings.DB_TABLE_NO_LEADERBOARD;
import static net.pinodev.ultraplaytime.configs.files.Settings.DB_TABLE_USERDATA;

public enum Statements {



    CREATE_TABLE_USERS("CREATE TABLE IF NOT EXISTS `" +
            DB_TABLE_USERDATA.getString() +
            "` (`player_uuid` BINARY(16) NOT NULL," +
            " `playtime` BIGINT," +
            " `rewards` BLOB," +
            " PRIMARY KEY (`player_uuid`))"),
    CREATE_TABLE_NO_LEADERBOARD("CREATE TABLE IF NOT EXISTS `" +
            DB_TABLE_NO_LEADERBOARD.getString() +
            "` (`player_uuid` BINARY(16) NOT NULL," +
            " PRIMARY KEY (`player_uuid`))"),
    GET_USER_DATA("SELECT * FROM `" +
            DB_TABLE_USERDATA.getString() +
            "` WHERE `player_uuid`=? LIMIT 1"),
    GET_TOP_USERS("SELECT u.`player_uuid`, u.`playtime` FROM `" +
            DB_TABLE_USERDATA.getString() + "` u " +
            "LEFT JOIN `" + DB_TABLE_NO_LEADERBOARD.getString() +
            "` e ON u.`player_uuid` = e.`player_uuid` " +
            "WHERE e.`player_uuid` IS NULL " +
            "ORDER BY `playtime` DESC LIMIT 10;"),
    INSERT_USER_DATA("INSERT INTO `" +
            DB_TABLE_USERDATA.getString() +
            "` (`player_uuid`,`playtime`,`rewards`) VALUES(?,?,?)"),
    UPDATE_USER_DATA("UPDATE `" +
            DB_TABLE_USERDATA.getString() +
            "` SET playtime=? , rewards=? WHERE player_uuid=?"),
    SAVE_USER_DATA("INSERT INTO `" +
            DB_TABLE_USERDATA.getString() +
            "` (`player_uuid`,`playtime`,`rewards`) VALUES (?,?,?) ON DUPLICATE KEY UPDATE" +
            " `playtime` = VALUES(playtime), `rewards` = VALUES(rewards)"),

    GET_NO_LEADERBOARD_USER("SELECT `player_uuid` FROM `" +
            DB_TABLE_NO_LEADERBOARD.getString() +
            "` WHERE `player_uuid`=?"),
    ADD_NO_LEADERBOARD_USER("INSERT INTO `" +
            DB_TABLE_NO_LEADERBOARD.getString() +
            "` (`player_uuid`) VALUES (?)"),
    REMOVE_NO_LEADERBOARD_USER("DELETE FROM `" +
            DB_TABLE_NO_LEADERBOARD.getString() +
            "` WHERE `player_uuid` = ?");


    public final String statement;

    Statements(String statement){
        this.statement = statement;
    }

    public String getStatement() {
        return statement;
    }
}
