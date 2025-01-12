package net.pinodev.ultraplaytime.configs.files;


import java.util.List;

import static net.pinodev.ultraplaytime.UltraPlaytime.*;

public enum Rewards {

    REWARD_REQUIREMENT("Rewards.X.requirement"),
    REWARD_DISPLAYNAME("Rewards.X.display_name"),
    REWARD_MESSAGES("Rewards.X.messages"),
    REWARD_UPPER_TITLE("Rewards.X.title.upperTitle"),
    REWARD_SUB_TITLE("Rewards.X.title.subTitle"),
    REWARD_SOUND("Rewards.X.sound"),
    REWARD_COMMANDS("Rewards.X.commands");

    private final String key;

    Rewards(String key) {
        this.key = key;
    }

    public String getString(int rewardID) {
        return RewardsYML.getFileConfiguration().getString(key.replace("X", String.valueOf(rewardID)));
    }

    public Integer getInt(int rewardID){
        return RewardsYML.getFileConfiguration().getInt(key.replace("X", String.valueOf(rewardID)));
    }

    public List<String> getStringList(int rewardID){
        return RewardsYML.getFileConfiguration().getStringList(key.replace("X", String.valueOf(rewardID)));
    }
}
