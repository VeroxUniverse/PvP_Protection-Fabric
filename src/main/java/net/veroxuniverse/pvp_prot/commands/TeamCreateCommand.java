package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.utils.TeamTrackerUtil;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TeamCreateCommand {
    public static LiteralCommandNode<ServerCommandSource> register() {
        return literal("create")
                .then(argument("team_name", StringArgumentType.string())
                        .then(argument("color", StringArgumentType.string())
                                .then(argument("display_name", StringArgumentType.string())
                                        .then(argument("friendly_fire", StringArgumentType.string())
                                                .executes(TeamCreateCommand::execute)
                                        )
                                )
                        )
                ).build();
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String teamName = StringArgumentType.getString(context, "team_name");
        String colorString = StringArgumentType.getString(context, "color");
        String displayName = StringArgumentType.getString(context, "display_name");
        String friendlyFire = StringArgumentType.getString(context, "friendly_fire");

        MinecraftServer server = source.getServer();
        Scoreboard scoreboard = server.getScoreboard();

        if (scoreboard.getTeam(teamName) != null) {
            source.sendError(Text.literal("A team with this name already exists!")
                    .styled(style -> style.withColor(Formatting.RED) ));
            return 0;
        }

        Team team = scoreboard.addTeam(teamName);
        TeamTrackerUtil.add(teamName);
        team.setDisplayName(Text.literal(displayName));

        try {
            Formatting color = Formatting.valueOf(colorString.toUpperCase());
            team.setColor(color);
        } catch (IllegalArgumentException e) {
            source.sendError(Text.literal("Invalid Color: " + colorString));
            return 0;
        }

        boolean allowFriendlyFire = friendlyFire.equalsIgnoreCase("true");
        team.setFriendlyFireAllowed(allowFriendlyFire);

        if (source.getEntity() instanceof ServerPlayerEntity player) {
            scoreboard.addScoreHolderToTeam(player.getName().getString(), team);
            source.getEntity().sendMessage(Text.literal("You have been added to the team " + displayName + " .")
                    .styled(style -> style.withColor(Formatting.GREEN) ));
        }

        Objects.requireNonNull(source.getEntity()).sendMessage(Text.literal("Team " + teamName + " (" + displayName + ")" + " created!")
                .styled(style -> style.withColor(Formatting.GREEN) ));
        return Command.SINGLE_SUCCESS;
    }
}