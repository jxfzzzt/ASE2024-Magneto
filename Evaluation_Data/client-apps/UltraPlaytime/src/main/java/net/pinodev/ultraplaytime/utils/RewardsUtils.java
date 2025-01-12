package net.pinodev.ultraplaytime.utils;

import net.pinodev.ultraplaytime.cache.User;
import org.bukkit.OfflinePlayer;
import org.xerial.snappy.BitShuffle;
import org.xerial.snappy.Snappy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static net.pinodev.ultraplaytime.UltraPlaytime.RewardManager;
import static net.pinodev.ultraplaytime.UltraPlaytime.UserManager;

public class RewardsUtils {

    public byte[] compressed(int[] rewards) throws IOException {
        final byte[] shuffledRewards = BitShuffle.shuffle(rewards);
        return Snappy.compress(shuffledRewards);
    }

    public int[] decompressed(byte[] compressedRewards) throws IOException {
        final byte[] uncompressedRewards = Snappy.uncompress(compressedRewards);
        return BitShuffle.unshuffleIntArray(uncompressedRewards);
    }

    public HashSet<Integer> toHashSet(int[] array){
        HashSet<Integer> rewardSet = new HashSet<>(array.length);
        for(int rewardID : array){
            rewardSet.add(rewardID);
        }
        return rewardSet;
    }

    public int[] toArray(HashSet<Integer> set) {
        int[] array = new int[set.size()];
        int index = 0;
        for (int value : set) {
            array[index++] = value;
        }
        return array;
    }

    public String getCurrentReward(OfflinePlayer player){
        final UUID uuid = player.getUniqueId();
        if(UserManager.getCachedUsers().containsKey(uuid)){
            final User user = UserManager.getCachedUsers().get(uuid);
            final HashSet<Integer> rewards = user.getRewardsAchieved();
            int maxNumber = 0;
            for (int currentNumber : rewards) {
                if (currentNumber > maxNumber) {
                    maxNumber = currentNumber;
                }
            }
            return String.valueOf(maxNumber);
        }
        return "0";
    }

    public int[] calculateRewards(long playtime){

        List<Integer> rewardList = new ArrayList<>();
        for (int rewardID : RewardManager.getRegisteredRewards().keySet()) {
            final int secondsRequirement = RewardManager.getRegisteredRewards().get(rewardID);
            long millisRequirement = (long) secondsRequirement * 1000;
            if (playtime >= millisRequirement) {
                rewardList.add(rewardID);
            }
        }

        int[] rewards = new int[rewardList.size()];
        for (int i = 0; i < rewardList.size(); i++) {
            rewards[i] = rewardList.get(i);
        }

        return rewards;
    }

}
