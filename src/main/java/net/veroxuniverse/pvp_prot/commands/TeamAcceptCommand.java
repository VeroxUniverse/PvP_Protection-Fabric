package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.setup.InvitationManager;

import static net.minecraft.server.command.CommandManager.literal;

public class TeamAcceptCommand {
    public static LiteralCommandNode<ServerCommandSource> register() {
        return literal("accept")
                .executes(TeamAcceptCommand::execute)
                .build();
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = (ServerPlayerEntity) source.getEntity();

        if (player == null) {
            source.sendMessage(Text.literal("Only players can accept invitations.").styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }

        MinecraftServer server = source.getServer();
        Scoreboard scoreboard = server.getScoreboard();

        if (!InvitationManager.hasInvitation(player)) {
            source.sendMessage(Text.literal("You don't have any pending invitations.").styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }
        
        boolean success = InvitationManager.acceptInvitation(player, scoreboard);
        if (!success) {
            source.sendMessage(Text.literal("There was an issue accepting the invitation.").styled(style -> style.withColor(Formatting.RED)));
            return 0;
        }

        return Command.SINGLE_SUCCESS;
    }
}
