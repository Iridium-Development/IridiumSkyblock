package com.iridium.iridiumskyblock.utils;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import java.time.Duration;

public class TimeUtils {

    public static String formatDuration(Duration duration) {
        String format = IridiumSkyblock.getInstance().getConfiguration().durationFormat;
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return format.replace("%hours%", String.valueOf(hours))
            .replace("%minutes%", String.valueOf(minutes))
            .replace("%seconds%", String.valueOf(seconds));
    }

}
