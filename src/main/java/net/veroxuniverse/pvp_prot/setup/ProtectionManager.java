package net.veroxuniverse.pvp_prot.setup;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.config.ProtectionConfig;
import net.veroxuniverse.pvp_prot.util.TimeUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class ProtectionManager {
    private static final Map<UUID, Integer> timers = new HashMap<>();

    public static void addPlayer(ServerPlayerEntity player) {
        UUID id = player.getUuid();
        timers.put(id, ProtectionConfig.protectionMinutes * 60 * 20);

        player.sendMessage(Text.translatable("message.pvp_prot.protected")
                .styled(style -> style.withColor(Formatting.GREEN) ), true);
        player.getServer().getPlayerManager().broadcast(Text.translatable("message.pvp_prot.announce", player.getName())
                .styled(style -> style.withColor(Formatting.RED) ), true);
    }

    public static boolean isProtected(ServerPlayerEntity player) {
        return timers.containsKey(player.getUuid());
    }

    public static void removePlayerProtection(ServerPlayerEntity player) {
        UUID id = player.getUuid();
        if (timers.containsKey(id)) {
            timers.remove(id);
            player.sendMessage(Text.translatable("message.pvp_prot.protection_removed")
                    .styled(style -> style.withColor(Formatting.RED)), true);
        }
    }

    public static boolean handleDamage(ServerPlayerEntity target, ServerPlayerEntity attacker) {
        if (timers.containsKey(target.getUuid())) {
            attacker.sendMessage(Text.translatable("message.pvp_prot.blocked")
                    .styled(style -> style.withColor(Formatting.RED) ), true);
            return false;
        }
        return true;
    }

    public static void tick(MinecraftServer server) {
        Iterator<Map.Entry<UUID, Integer>> iterator = timers.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Integer> entry = iterator.next();
            UUID playerId = entry.getKey();
            int time = entry.getValue() - 1;
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerId);

            if (time <= 0) {
                if (player != null) {
                    player.sendMessage(Text.translatable("message.pvp_prot.ended")
                            .styled(style -> style.withColor(Formatting.RED) ), true);
                }
                iterator.remove();
            } else {
                entry.setValue(time);

                if (time % 20 == 0 && player != null) {
                    String timeStr = TimeUtils.formatTicks(time);
                    player.sendMessage(Text.translatable("message.pvp_prot.timer", timeStr)
                            .styled(style -> style.withColor(Formatting.YELLOW) ), true);
                }
            }
        }
    }
}
