package com.github.may2beez.farmhelperv2.mixin.render;

import com.github.may2beez.farmhelperv2.feature.impl.Freelock;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = EntityRenderer.class, priority = Integer.MAX_VALUE)
public class MixinEntityRenderer {
    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationPitch:F"))
    private float modifyPitch(Entity entity) {
        return Freelock.getInstance().getPitch(entity.rotationPitch);
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;rotationYaw:F"))
    private float modifyYaw(Entity entity) {
        return Freelock.getInstance().getYaw(entity.rotationYaw);
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationPitch:F"))
    private float modifyPrevPitch(Entity entity) {
        return Freelock.getInstance().getPrevPitch(entity.prevRotationPitch);
    }

    @Redirect(method = "orientCamera", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;prevRotationYaw:F"))
    private float modifyPrevYaw(Entity entity) {
        return Freelock.getInstance().getPrevYaw(entity.prevRotationYaw);
    }
}
