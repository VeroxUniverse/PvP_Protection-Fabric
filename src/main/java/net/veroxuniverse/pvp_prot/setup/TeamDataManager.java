package net.veroxuniverse.pvp_prot.setup;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class TeamDataManager {
    public static class TeamData {
        public final String name;
        public final String color;
        public final UUID owner;
        public final Set<UUID> members = new HashSet<>();

        public int totalKills = 0;
        public int killsSinceLastReward = 0;
        public int maxKills = 0;

        public TeamData(String name, String color, UUID owner) {
            this.name = name;
            this.color = color;
            this.owner = owner;
            this.members.add(owner);
        }
    }

    private static final Map<UUID, String> playerToTeam = new HashMap<>();
    private static final Map<String, TeamData> teams = new HashMap<>();

    public static boolean createTeam(String name, String color, ServerPlayerEntity owner) {
        if (teams.containsKey(name)) return false;
        TeamData data = new TeamData(name, color, owner.getUuid());
        teams.put(name, data);
        playerToTeam.put(owner.getUuid(), name);
        return true;
    }

    public static boolean inviteToTeam(String teamName, ServerPlayerEntity player) {
        TeamData team = teams.get(teamName);
        if (team == null) return false;
        team.members.add(player.getUuid());
        playerToTeam.put(player.getUuid(), teamName);
        return true;
    }

    public static void addKill(ServerPlayerEntity killer) {
        String teamName = playerToTeam.get(killer.getUuid());
        if (teamName != null) {
            TeamData team = teams.get(teamName);
            team.totalKills++;
            team.killsSinceLastReward++;
            if (team.totalKills > team.maxKills) team.maxKills = team.totalKills;
        }
    }

    public static List<TeamData> getAllTeams() {
        return new ArrayList<>(teams.values());
    }

    public static TeamData getTeamByPlayer(ServerPlayerEntity player) {
        String name = playerToTeam.get(player.getUuid());
        return name != null ? teams.get(name) : null;
    }

    public static void resetKillCounts() {
        for (TeamData team : teams.values()) {
            team.killsSinceLastReward = 0;
        }
    }

    public static void removeTeam(String name) {
        TeamData removed = teams.remove(name);
        if (removed != null) {
            for (UUID uuid : removed.members) {
                playerToTeam.remove(uuid);
            }
        }
    }

    public static String getTeamName(UUID playerId) {
        return playerToTeam.get(playerId);
    }
}