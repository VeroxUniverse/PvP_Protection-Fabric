package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TeamPlayersCommand {
    public static LiteralCommandNode<ServerCommandSource> register() {
        return literal("info")
                .then(argument("team_name", StringArgumentType.string())
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            String teamName = StringArgumentType.getString(context, "team_name");

                            Team team = source.getServer().getScoreboard().getTeam(teamName);
                            if (team == null) {
                                source.sendError(Text.literal("Team not found.")
                                        .styled(s -> s.withColor(Formatting.RED)));
                                return 0;
                            }

                            Collection<String> players = team.getPlayerList();
                            if (players.isEmpty()) {
                                source.sendMessage(Text.literal("Team has no players.")
                                        .styled(s -> s.withColor(Formatting.RED)));
                            } else {
                                source.sendMessage(Text.literal("Players in " + teamName + ":")
                                        .styled(s -> s.withColor(Formatting.YELLOW)));
                                for (String player : players) {
                                    source.sendMessage(Text.literal("- " + player));
                                }
                            }
                            return 1;
                        })
                ).build();
    }
}

