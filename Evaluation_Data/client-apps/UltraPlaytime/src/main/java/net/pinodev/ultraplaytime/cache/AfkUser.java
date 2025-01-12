package net.pinodev.ultraplaytime.cache;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class AfkUser {

    @Getter
    @Setter
    private Location location;

    @Getter
    @Setter
    private Integer seconds;

    public AfkUser(Location location, Integer seconds) {
        this.location = location;
        this.seconds = seconds;
    }
}
