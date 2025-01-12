package net.pinodev.ultraplaytime.rewards;


import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Reward {

    @Getter
    private final UUID playerToReward;

    @Getter
    private final int rewardID;

    @Getter
    private final String upperTitle;

    @Getter
    private final String subTitle;

    @Getter
    private final String sound;

    @Getter
    private final List<String> messages;

    @Getter
    private final List<String> commands;

    public Reward(UUID playerToReward, int rewardID, String upperTitle, String subTitle, String sound, List<String> messages, List<String> commands) {
        this.playerToReward = playerToReward;
        this.rewardID = rewardID;
        this.upperTitle = upperTitle;
        this.subTitle = subTitle;
        this.sound = sound;
        this.messages = messages;
        this.commands = commands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reward reward = (Reward) o;
        return rewardID == reward.rewardID &&
                Objects.equals(playerToReward, reward.playerToReward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerToReward, rewardID);
    }

}
