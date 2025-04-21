package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.literal;

public class TeamListCommand {
    public static LiteralCommandNode<ServerCommandSource> register() {
        return literal("list").executes(context -> {
            ServerCommandSource source = context.getSource();
            MinecraftServer server = source.getServer();
            Scoreboard scoreboard = server.getScoreboard();

            Collection<Team> teams = scoreboard.getTeams();
            if (teams.isEmpty()) {
                source.sendMessage(Text.literal("No teams exist.")
                        .styled(s -> s.withColor(Formatting.RED)));
                return 1;
            }

            source.sendMessage(Text.literal("All Teams:").styled(s -> s.withColor(Formatting.YELLOW)));
            for (Team team : teams) {
                String teamName = team.getName();
                Text displayName = team.getDisplayName();
                source.sendMessage(Text.literal("- " + teamName + " (" + displayName.getString() + ")"));
            }
            return 1;
        }).build();
    }
}