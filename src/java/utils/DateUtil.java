package utils;

import java.util.Date;

public class DateUtil {
    public static String getRelativeTime(Date date) {
        long diffInMilliSeconds = new Date().getTime() - date.getTime();
        long seconds = diffInMilliSeconds / 1000 % 60;
        long minutes = diffInMilliSeconds / (60 * 1000) % 60;
        long hours = diffInMilliSeconds / (60 * 60 * 1000) % 24;
        long days = diffInMilliSeconds / (24 * 60 * 60 * 1000);

        if (days > 0) {
            return days + (days > 1 ? " days ago" : " day ago");
        } else if (hours > 0) {
            return hours + (hours > 1 ? " hours ago" : " hour ago");
        } else if (minutes > 0) {
            return minutes + (minutes > 1 ? " minutes ago" : " minute ago");
        } else {
            return seconds + (seconds > 1 ? " seconds ago" : " second ago");
        }
    }
}
