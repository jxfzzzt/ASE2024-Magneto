package net.pinodev.ultraplaytime.cache;


import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class UserManager {

    @Getter
    private final ConcurrentHashMap<UUID, User> cachedUsers;
    
    @Getter
    private final CopyOnWriteArrayList<TopUser> topUsers;


    public UserManager(){
        cachedUsers = new ConcurrentHashMap<>();
        topUsers = new CopyOnWriteArrayList<>();
    }

    public User getUser(UUID uuid){
        return cachedUsers.get(uuid);
    }

    public void addUser(UUID uuid, User user){
        cachedUsers.remove(uuid);
        cachedUsers.put(uuid, user);
    }

    public void removeUser(UUID uuid){
        cachedUsers.remove(uuid);
    }

    public void addTopUser(TopUser topUser){

        if(topUsers.size() < 10) topUsers.add(topUser);
    }

    public TopUser getTopUserAt(int topPosition){
        if(topUsers.size() > topPosition){
            return topUsers.get(topPosition);
        }
        return null;
    }

}
