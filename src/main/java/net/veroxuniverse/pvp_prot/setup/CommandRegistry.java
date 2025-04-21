package net.veroxuniverse.pvp_prot.setup;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;
import net.veroxuniverse.pvp_prot.commands.*;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandRegistry {
    public static void registerCommands() {

        CommandRegistrationCallback.EVENT.register((dispatcher, environment, serverInstance) -> {
            LiteralCommandNode<ServerCommandSource> rootTeams = literal("pvp_teams").build();

            rootTeams.addChild(TeamCreateCommand.register());
            rootTeams.addChild(TeamInviteCommand.register());
            rootTeams.addChild(TeamAcceptCommand.register());
            rootTeams.addChild(TeamDeclineCommand.register());
            rootTeams.addChild(TeamListCommand.register());
            rootTeams.addChild(TeamPlayersCommand.register());
            rootTeams.addChild(TeamLeaveCommand.register());
            rootTeams.addChild(HelpCommand.register());
            rootTeams.addChild(TeamSetColorCommand.register());
            rootTeams.addChild(TeamSetDisplayNameCommand.register());
            rootTeams.addChild(TeamSetFriendlyFireCommand.register());

            LiteralCommandNode<ServerCommandSource> rootSystem = literal("pvp_system").build();

            rootSystem.addChild(ProtectionConfigCommand.register());
            rootSystem.addChild(HelpCommand.register());

            dispatcher.getRoot().addChild(rootTeams);
            dispatcher.getRoot().addChild(rootSystem);
        });
    }
}