package net.pinodev.ultraplaytime.cache;


import lombok.Getter;

import java.util.UUID;

public class TopUser {

    @Getter
    private final UUID uuid;

    @Getter
    private final Long playtime;

    @Getter
    private final String userName;

    public TopUser(UUID uuid, Long playtime, String userName){
        this.uuid = uuid;
        this.playtime = playtime;
        this.userName = userName;
    }
}
