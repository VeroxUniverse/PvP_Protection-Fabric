package net.veroxuniverse.pvp_prot.setup;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

public class ProtectionHandler {
    public static void register() {
        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedPlayer) -> {
            if (killedPlayer instanceof ServerPlayerEntity) {
                ProtectionManager.addPlayer((ServerPlayerEntity) killedPlayer);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(ProtectionManager::tick);

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient && player instanceof ServerPlayerEntity attacker && entity instanceof ServerPlayerEntity target) {
                if (ProtectionManager.isProtected(target)) {
                    attacker.sendMessage(net.minecraft.text.Text.translatable("message.pvp_prot.blocked")
                            .styled(style -> style.withColor(Formatting.RED) ), true);
                    return ActionResult.FAIL;
                }

                if (ProtectionManager.isProtected(attacker)) {

                    ProtectionManager.removePlayerProtection(attacker);
                }

            }
            return ActionResult.PASS;
        });

    }
}