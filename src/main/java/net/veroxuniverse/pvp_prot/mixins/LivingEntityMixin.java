package net.veroxuniverse.pvp_prot.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.setup.ProtectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attackerEntity = source.getAttacker();
        Entity targetEntity = (Entity) (Object) this;

        if (attackerEntity instanceof ServerPlayerEntity attacker && targetEntity instanceof ServerPlayerEntity target) {
            if (ProtectionManager.isProtected(attacker)) {
                ProtectionManager.removePlayerProtection(attacker);
                cir.setReturnValue(false);
            } else if (ProtectionManager.isProtected(target)) {
                attacker.sendMessage(net.minecraft.text.Text.literal("You cannot attack a protected player!")
                        .styled(style -> style.withColor(Formatting.RED) ), true);
                cir.setReturnValue(false);
            }
        }
    }
}