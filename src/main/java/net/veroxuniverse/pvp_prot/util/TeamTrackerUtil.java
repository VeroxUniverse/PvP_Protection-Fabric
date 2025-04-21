package net.veroxuniverse.pvp_prot.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TeamTrackerUtil {
    private static final Set<String> modTeams = new HashSet<>();

    public static void add(String teamName) {
        modTeams.add(teamName);
    }

    public static void remove(String teamName) {
        modTeams.remove(teamName);
    }

    public static boolean isModTeam(String teamName) {
        return modTeams.contains(teamName);
    }

    public static Set<String> getAllModTeams() {
        return Collections.unmodifiableSet(modTeams);
    }
}