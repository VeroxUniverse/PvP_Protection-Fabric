package net.veroxuniverse.pvp_prot.mixins;

import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ChatMessageMixin {

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)

    private void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) {
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        String message = packet.chatMessage();

        Team team = player.getScoreboardTeam();
        Text prefix = team != null ? team.getPrefix() : Text.literal("");
        Text playerName = Text.literal(player.getName().getString()).styled(style -> style.withColor(Formatting.WHITE));
        Text formattedMessage = Text.empty()
                .append(prefix)
                .append(playerName)
                .append(Text.literal(": "))
                .append(Text.literal(message).styled(style -> style.withColor(Formatting.GRAY)));

        if (player.getServer() == null) {
            return;
        } else {
            player.getServer().getPlayerManager().broadcast(formattedMessage, false);
        }

        ci.cancel();
    }
}
