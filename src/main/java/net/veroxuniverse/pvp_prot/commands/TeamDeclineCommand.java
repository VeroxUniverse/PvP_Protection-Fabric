package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.setup.InvitationManager;

import static net.minecraft.server.command.CommandManager.literal;

public class TeamDeclineCommand {
    public static LiteralCommandNode<ServerCommandSource> register() {
        return literal("decline")
                .executes(TeamDeclineCommand::execute)
                .build();
    }

    private static int execute(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        InvitationManager.declineInvitation(player);
        source.sendMessage(Text.literal("You have declined the invitation.")
                .styled(style -> style.withColor(Formatting.RED) ));

        return 1;
    }
}