package net.pinodev.ultraplaytime.utils;

public class UtilsManager {

    public final MessageUtils message;

    public final PlaytimeUtils playtime;

    public final RewardsUtils rewards;

    public final UuidUtils uuid;


    public UtilsManager(){
        this.message = new MessageUtils();
        this.playtime = new PlaytimeUtils();
        this.rewards = new RewardsUtils();
        this.uuid = new UuidUtils();
    }
}
