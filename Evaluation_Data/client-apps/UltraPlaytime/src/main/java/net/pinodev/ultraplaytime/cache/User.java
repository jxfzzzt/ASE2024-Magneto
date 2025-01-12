package net.pinodev.ultraplaytime.cache;


import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.UUID;

public class User {

    @Getter
    private final UUID uuid;

    @Getter
    @Setter
    private Long playtime;

    @Getter
    @Setter
    private HashSet<Integer> rewardsAchieved;

    @Getter
    @Setter
    private boolean isNewbie;

    @Getter
    @Setter
    private boolean isOffline;

    @Getter
    @Setter
    private boolean isAfk;

    public User(UUID uuid, Long playtime, HashSet<Integer> rewardAchieved, boolean isNewbie, boolean isOffline, boolean isAfk) {
        this.uuid = uuid;
        this.playtime = playtime;
        this.rewardsAchieved = rewardAchieved;
        this.isNewbie = isNewbie;
        this.isOffline = isOffline;
        this.isAfk = isAfk;
    }
}
