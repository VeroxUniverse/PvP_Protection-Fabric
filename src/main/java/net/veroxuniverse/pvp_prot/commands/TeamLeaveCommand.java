package net.veroxuniverse.pvp_prot.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.utils.TeamTrackerUtil;

import static net.minecraft.server.command.CommandManager.literal;

public class TeamLeaveCommand {
    public static LiteralCommandNode<ServerCommandSource> register() {
        return literal("leave").executes(context -> {
            ServerCommandSource source = context.getSource();
            ServerPlayerEntity player = source.getPlayer();
            if (player != null) {
                Scoreboard scoreboard = player.getScoreboard();
                String playerName = player.getName().getString();

                Team currentTeam = scoreboard.getScoreHolderTeam(playerName);
                if (currentTeam == null) {
                    source.sendMessage(Text.literal("You are not in a team.")
                            .styled(s -> s.withColor(Formatting.RED)));
                    return 1;
                }

                String teamName = currentTeam.getName();
                scoreboard.clearTeam(playerName);

                source.sendMessage(Text.literal("You left the team \"" + teamName + "\".")
                        .styled(s -> s.withColor(Formatting.YELLOW)));

                if (TeamTrackerUtil.isModTeam(teamName)
                        && currentTeam.getPlayerList().isEmpty()) {
                    scoreboard.removeTeam(currentTeam);
                    TeamTrackerUtil.remove(teamName);

                    source.sendMessage(Text.literal("Team \"" + teamName + "\" has been deleted.")
                            .styled(s -> s.withColor(Formatting.RED)));
                }

                return 1;
            }
            // This assumes that "0" is no change
            // Whilst it is unlikely that `getPlayer` will return null, it is better to handle the possibility than have
            // it surprise you later down the line.
            return 0;
        }).build();
    }
}