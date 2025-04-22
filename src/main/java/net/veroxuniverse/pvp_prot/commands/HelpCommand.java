package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HelpCommand {

    public static LiteralCommandNode<ServerCommandSource> register() {
        return CommandManager.literal("help")
                .executes(HelpCommand::executeHelpCommand)
                .build();
    }

    private static int executeHelpCommand(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();

        source.sendMessage(Text.literal("=== PvP Teams Commands ===")
                .styled(style -> style.withColor(Formatting.GREEN)));
        source.sendMessage(Text.literal("/pvp_teams create <team_name> <color> <display_name> <friendly_fire>")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("Creates a new team with the specified parameters.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("Parameters:")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("<team_name> - The name of the team (must be unique).")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("<color> - The color of the team. Valid colors are: 'BLACK', 'DARK_BLUE', 'DARK_GREEN', 'DARK_AQUA', 'DARK_RED', 'DARK_PURPLE', 'GOLD', 'GRAY', 'DARK_GRAY', 'BLUE', 'GREEN', 'AQUA', 'RED', 'LIGHT_PURPLE', 'YELLOW', 'WHITE'.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("<display_name> - The name shown to other players when they see the team. Example: 'The Red Team'.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("<friendly_fire> - Whether players in the team can damage each other. Use 'true' or 'false'.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal(""));
        source.sendMessage(Text.literal("Example usage: /pvp_teams create MyTeam BLUE 'The Mighty Team' true")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal(""));


        source.sendMessage(Text.literal("/pvp_teams set displayname <team_name> <display_name> - Changes the display name of the specified team.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("/pvp_teams set color <team_name> <color> - Changes the color of the specified team.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("/pvp_teams set friendlyfire <team_name> <true/false> - Sets whether players in the team can damage each other.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("/pvp_teams invite <player_name> - Invites a player to your team.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("/pvp_teams accept <player_name> - Accepts a team invite from another player.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("/pvp_teams decline <player_name> - Declines a team invite.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("/pvp_teams list - Lists all available teams.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("/pvp_teams players <team_name> - Shows players in the specified team.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal("/pvp_teams leave - Leaves your current team.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal(""));

        source.sendMessage(Text.literal("=== PvP System Commands ===")
                .styled(style -> style.withColor(Formatting.GREEN)));
        source.sendMessage(Text.literal("/pvp_system set time <minutes> - Sets the protection time in minutes.")
                .styled(style -> style.withColor(Formatting.GRAY)));
        source.sendMessage(Text.literal(""));



        return Command.SINGLE_SUCCESS;
    }
}
