package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TeamSetColorCommand {

    public static LiteralCommandNode<ServerCommandSource> register() {
        return literal("set")
                .then(literal("color")
                        .then(argument("team_name", StringArgumentType.string())
                                .then(argument("color", StringArgumentType.string())
                                        .executes(TeamSetColorCommand::execute)
                                )
                        )
                ).build();
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        String teamName = StringArgumentType.getString(context, "team_name");
        String colorString = StringArgumentType.getString(context, "color");

        MinecraftServer server = source.getServer();
        Scoreboard scoreboard = server.getScoreboard();

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            source.sendError(Text.literal("Team \"" + teamName + "\" does not exist!")
                    .styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }

        if (!isPlayerInTeam(source, team)) {
            source.sendError(Text.literal("You are not in the team \"" + teamName + "\" and cannot change its color.")
                    .styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }

        try {
            Formatting color = Formatting.valueOf(colorString.toUpperCase());
            team.setColor(color);
        } catch (IllegalArgumentException e) {
            source.sendError(Text.literal("Invalid Color: " + colorString)
                    .styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }

        source.sendMessage(Text.literal("The color for team \"" + teamName + "\" has been changed to " + colorString + ".")
                .styled(style -> style.withColor(Formatting.GREEN)));

        return Command.SINGLE_SUCCESS;
    }

    private static boolean isPlayerInTeam(ServerCommandSource source, Team team) {
        return team.getPlayerList().contains(source.getName());
    }
}
