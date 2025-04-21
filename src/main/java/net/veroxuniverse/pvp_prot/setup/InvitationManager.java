package net.veroxuniverse.pvp_prot.setup;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Map;

public class InvitationManager {
    private static final Map<ServerPlayerEntity, String> pendingInvitations = new HashMap<>();
    private static final Map<ServerPlayerEntity, Long> lastInvitationSent = new HashMap<>();
    private static final long COOLDOWN_TIME = 30000;

    public static void sendInvitation(ServerPlayerEntity player, String teamName) {
        long currentTime = System.currentTimeMillis();
        Long lastSentTime = lastInvitationSent.get(player);

        if (lastSentTime != null && currentTime - lastSentTime < COOLDOWN_TIME) {
            long remainingTime = COOLDOWN_TIME - (currentTime - lastSentTime);
            player.sendMessage(Text.literal("You need to wait " + remainingTime / 1000 + " seconds before sending another invitation.")
                    .styled(style -> style.withColor(Formatting.RED)), false);
            return;
        }

        pendingInvitations.put(player, teamName);
        lastInvitationSent.put(player, currentTime);

        Text acceptText = Text.literal("ACCEPT")
                .styled(style -> style.withColor(Formatting.GREEN)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pvp_teams accept"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to accept the invitation"))));

        Text declineText = Text.literal("DECLINE")
                .styled(style -> style.withColor(Formatting.RED)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pvp_teams decline"))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to decline the invitation"))));

        Text separatorText = Text.literal(" - ")
                .styled(style -> style.withColor(Formatting.GRAY));

        Text message = Text.literal("You have been invited to join team \"" + teamName + "\". ")
                .append(acceptText)
                .append(separatorText)
                .append(declineText);

        player.sendMessage(message, false);
    }

    public static boolean hasInvitation(ServerPlayerEntity player) {
        return pendingInvitations.containsKey(player);
    }

    public static boolean acceptInvitation(ServerPlayerEntity player, Scoreboard scoreboard) {
        String teamName = pendingInvitations.get(player);
        if (teamName == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInvitationSent.get(player) >= COOLDOWN_TIME) {
            player.sendMessage(Text.literal("The invitation has expired.").styled(style -> style.withColor(Formatting.RED)), false);
            pendingInvitations.remove(player);
            lastInvitationSent.remove(player);
            return false;
        }

        Team team = scoreboard.getTeam(teamName);
        if (team != null) {
            scoreboard.addScoreHolderToTeam(player.getName().getString(), team);
            player.sendMessage(Text.literal("You have joined the team \"" + teamName + "\".")
                    .styled(style -> style.withColor(Formatting.GREEN)), false);
            pendingInvitations.remove(player);
            lastInvitationSent.remove(player);
            return true;
        }
        return false;
    }

    public static void declineInvitation(ServerPlayerEntity player) {
        String teamName = pendingInvitations.get(player);
        if (teamName != null) {
            player.sendMessage(Text.literal("You have declined the invitation to join \"" + teamName + "\".")
                    .styled(style -> style.withColor(Formatting.RED)), false);
            pendingInvitations.remove(player);
            lastInvitationSent.remove(player);
        }
    }
}
