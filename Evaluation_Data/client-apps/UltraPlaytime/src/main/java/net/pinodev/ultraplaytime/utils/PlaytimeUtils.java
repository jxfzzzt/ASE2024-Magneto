package net.pinodev.ultraplaytime.utils;

import net.pinodev.ultraplaytime.configs.files.Locale;

public class PlaytimeUtils {

    public String formatted(long playtimeInMilliseconds) {
        long playtimeInSeconds = playtimeInMilliseconds / 1000;

        long years = playtimeInSeconds / (60 * 60 * 24 * 365);
        playtimeInSeconds %= (60 * 60 * 24 * 365);

        long months = playtimeInSeconds / (60 * 60 * 24 * 30);
        playtimeInSeconds %= (60 * 60 * 24 * 30);

        long days = playtimeInSeconds / (60 * 60 * 24);
        playtimeInSeconds %= (60 * 60 * 24);

        long hours = playtimeInSeconds / (60 * 60);
        playtimeInSeconds %= (60 * 60);

        long minutes = playtimeInSeconds / 60;
        long seconds = playtimeInSeconds % 60;

        StringBuilder formattedTime = new StringBuilder();

        appendTime(formattedTime, years, Locale.TIME_YEAR.getString(), Locale.TIME_YEARS.getString());
        appendTime(formattedTime, months, Locale.TIME_MONTH.getString(), Locale.TIME_MONTHS.getString());
        appendTime(formattedTime, days, Locale.TIME_DAY.getString(), Locale.TIME_DAYS.getString());
        appendTime(formattedTime, hours, Locale.TIME_HOUR.getString(), Locale.TIME_HOURS.getString());
        appendTime(formattedTime, minutes, Locale.TIME_MINUTE.getString(), Locale.TIME_MINUTES.getString());
        appendTime(formattedTime, seconds, Locale.TIME_SECOND.getString(), Locale.TIME_SECONDS.getString());

        return formattedTime.toString().trim();
    }

    private void appendTime(StringBuilder sb, long value, String singular, String plural) {
        if (value > 1) {
            sb.append(value).append(" ").append(plural).append(" ");
        } else if (value == 1) {
            sb.append(value).append(" ").append(singular).append(" ");
        }
    }

    public boolean isInteger(String toParse){
        int number;
        try{
            number = Integer.parseInt(toParse);
            return true;
        }catch (NumberFormatException exception){
            return false;
        }
    }


}
