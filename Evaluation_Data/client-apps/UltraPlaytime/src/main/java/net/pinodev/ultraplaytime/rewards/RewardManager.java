package net.pinodev.ultraplaytime.rewards;

import lombok.Getter;
import net.pinodev.ultraplaytime.configs.files.Rewards;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.ConcurrentHashMap;

import static net.pinodev.ultraplaytime.UltraPlaytime.logger;
import static net.pinodev.ultraplaytime.UltraPlaytime.RewardsYML;

public class RewardManager {

    @Getter
    private final ConcurrentHashMap<Integer, Integer> registeredRewards;

    public RewardManager(){
        registeredRewards = new ConcurrentHashMap<>();
        loadRegisteredRewards();
    }

    public void loadRegisteredRewards(){
        logger.info("Registering rewards...");
        final ConfigurationSection rewardSection = RewardsYML.getFileConfiguration().getConfigurationSection("Rewards");
        if(rewardSection != null && ! rewardSection.getKeys(false).isEmpty()){
            for(String reward : rewardSection.getKeys(false)){
                final int rewardID = Integer.parseInt(reward);
                final int rewardRequirement = Rewards.REWARD_REQUIREMENT.getInt(rewardID);
                registeredRewards.put(rewardID, rewardRequirement);
            }
        }
        logger.info("Registered " + registeredRewards.size() + " rewards");
    }
}
