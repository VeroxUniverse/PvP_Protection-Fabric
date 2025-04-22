package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.setup.InvitationManager;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TeamInviteCommand {
    public static LiteralCommandNode<ServerCommandSource> register() {
        return literal("invite")
                .then(argument("player", EntityArgumentType.player())
                        .then(argument("team_name", StringArgumentType.string())
                                .executes(TeamInviteCommand::execute)
                        )
                ).build();
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity targetPlayer = EntityArgumentType.getPlayer(context, "player");
        String teamName = StringArgumentType.getString(context, "team_name");

        if (source.getName().equals(targetPlayer.getName().getString())) {
            source.sendError(Text.literal("You cannot invite yourself to a team!")
                    .styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }

        MinecraftServer server = source.getServer();
        Scoreboard scoreboard = server.getScoreboard();
        Team team = scoreboard.getTeam(teamName);

        if (team == null) {
            source.sendError(Text.literal("Team \"" + teamName + "\" does not exist!")
                    .styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }

        Team currentTeam = scoreboard.getScoreHolderTeam(targetPlayer.getName().getString());
        if (currentTeam != null) {
            source.sendMessage(Text.literal("Player is already in a team.")
                    .styled(style -> style.withColor(Formatting.RED)));
            return 1;
        }

        if (!isTeamMember(source, team)) {
            source.sendMessage(Text.literal("You must be in the team to invite other players.")
                    .styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }

        InvitationManager.sendInvitation(targetPlayer, teamName);
        source.sendMessage(Text.literal("Invitation sent to " + targetPlayer.getName().getString() + " to join team \"" + teamName + "\".")
                .styled(style -> style.withColor(Formatting.GREEN)));
        return 1;
    }

    private static boolean isTeamMember(ServerCommandSource source, Team team) {
        MinecraftServer server = source.getServer();
        Scoreboard scoreboard = server.getScoreboard();
        Team playerTeam = scoreboard.getScoreHolderTeam(source.getName());
        return playerTeam != null && playerTeam.equals(team);
    }
}
