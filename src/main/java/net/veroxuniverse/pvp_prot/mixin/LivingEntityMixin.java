package net.veroxuniverse.pvp_prot.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Formatting;
import net.veroxuniverse.pvp_prot.setup.ProtectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void pvp_prot$onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attackerEntity = source.getAttacker();
        Entity targetEntity = (Entity) (Object) this;

        if (attackerEntity instanceof ServerPlayerEntity attacker
                && targetEntity instanceof ServerPlayerEntity target
                && pvp_prot$isProtectedDamageSource(source)) {
            if (ProtectionManager.isProtected(attacker)) {
                ProtectionManager.removePlayerProtection(attacker);
                cir.setReturnValue(false);
            } else if (ProtectionManager.isProtected(target) && pvp_prot$isProtectedDamageSource(source)) {
                attacker.sendMessage(net.minecraft.text.Text.translatable("message.pvp_prot.blocked")
                        .styled(style -> style.withColor(Formatting.RED)), true);
                cir.setReturnValue(false);
            }
        }
    }

    @Unique
    boolean pvp_prot$isProtectedDamageSource(DamageSource source) {
        return (source.isIn(DamageTypeTags.IS_EXPLOSION)
                || source.isIn(DamageTypeTags.IS_FIRE)
                || source.isOf(DamageTypes.LAVA)
                || source.isOf(DamageTypes.FALLING_ANVIL)
                || source.isOf(DamageTypes.IN_WALL)
                || source.isOf(DamageTypes.WIND_CHARGE)
                || source.isIn(DamageTypeTags.IS_PROJECTILE)
                || source.isIn(DamageTypeTags.IS_PLAYER_ATTACK)
        ) && source.getSource() instanceof ServerPlayerEntity;
    }
}