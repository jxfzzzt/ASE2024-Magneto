package net.pinodev.ultraplaytime.placeholder;


import lombok.Getter;

public class Placeholder {

    @Getter
    private final String toSubstitute;

    @Getter
    private final String substitutor;

    public Placeholder(String toSubstitute, String substitutor) {
        this.toSubstitute = toSubstitute;
        this.substitutor = substitutor;
    }
}
