package net.veroxuniverse.pvp_prot.utils;

public class TimeUtils {

    public static String formatTicks(int ticks) {
        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);

    }
}