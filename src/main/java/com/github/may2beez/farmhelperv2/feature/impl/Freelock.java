package com.github.may2beez.farmhelperv2.feature.impl;

import com.github.may2beez.farmhelperv2.feature.IFeature;
import com.github.may2beez.farmhelperv2.handler.MacroHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Freelock implements IFeature {
    private final Minecraft mc = Minecraft.getMinecraft();
    private static Freelock instance;

    public static Freelock getInstance() {
        if (instance == null) {
            instance = new Freelock();
        }
        return instance;
    }

    @Override
    public String getName() {
        return "Freelock";
    }

    @Override
    public boolean isRunning() {
        return enabled;
    }

    @Override
    public boolean shouldPauseMacroExecution() {
        return false;
    }

    @Override
    public boolean shouldStartAtMacroStart() {
        return false;
    }

    public void toggle() {
        if (isRunning()) {
            stop();
        } else {
            start();
        }
    }

    private boolean mouseWasGrabbed = false;

    @Override
    public void start() {
        enabled = true;
//        cameraPrevYaw = mc.thePlayer.prevRotationYaw;
//        cameraPrevPitch = mc.thePlayer.prevRotationPitch;
//        cameraYaw = mc.thePlayer.rotationYaw;
//        cameraPitch = mc.thePlayer.rotationPitch;
//        mc.gameSettings.thirdPersonView = 1;
        if (UngrabMouse.getInstance().isRunning() && MacroHandler.getInstance().isCurrentMacroEnabled()) {
            UngrabMouse.getInstance().regrabMouse();
            mouseWasGrabbed = true;
        }
    }

    @Override
    public void stop() {
        enabled = false;
        mc.gameSettings.thirdPersonView = 0;
        if (UngrabMouse.getInstance().isToggled() && mouseWasGrabbed && MacroHandler.getInstance().isCurrentMacroEnabled()) {
            UngrabMouse.getInstance().ungrabMouse();
        }
        mouseWasGrabbed = false;
    }

    @Override
    public void resetStatesAfterMacroDisabled() {

    }

    @Override
    public boolean isToggled() {
        return false;
    }

    public boolean lastActivated;
    private boolean enabled = false;
    @Getter
    @Setter
    private float cameraYaw = 0;
    @Getter
    @Setter
    private float cameraPitch = 0;
    @Getter
    @Setter
    private float cameraPrevYaw;
    @Getter
    @Setter
    private float cameraPrevPitch;

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (!isRunning()) return;

        event.pitch = cameraPitch;
        event.yaw = cameraYaw;
    }

    public float getPitch(float original) {
        return isRunning() ? cameraPitch : original;
    }

    public float getYaw(float original) {
        return isRunning() ? cameraYaw : original;
    }

    public float getPrevPitch(float original) {
        return isRunning() ? cameraPrevPitch : original;
    }

    public float getPrevYaw(float original) {
        return isRunning() ? cameraPrevYaw : original;
    }

    public void onRender() {
        boolean current = isRunning();
        if (lastActivated == current) return;

        if (current) {
            lastActivated = true;
            cameraPrevYaw = mc.thePlayer.prevRotationYaw;
            cameraPrevPitch = mc.thePlayer.prevRotationPitch;
            cameraYaw = mc.thePlayer.rotationYaw + 180;
            cameraPitch = mc.thePlayer.rotationPitch;
            mc.gameSettings.thirdPersonView = 1;
        } else {
            mc.gameSettings.thirdPersonView = 0;
        }

    }

//    private RotationUtils.Rotation serverRotation;
//
//    private RotationUtils.Rotation prevRotation;
//
//    int ticks = 0;
//
//    @SubscribeEvent
//    public void onTick(TickEvent.ClientTickEvent event) {
//        if (event.phase == TickEvent.Phase.END) return;
//
//        if (ticks > 0) {
//            ticks--;
//            return;
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerUpdate(PlayerUpdateEvent event) {
//
//        switch (event.getState()) {
//            case PRE: {
//                this.prevRotation = new RotationUtils.Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
//                final RotationUtils.Rotation actual = new RotationUtils.Rotation(cameraYaw, cameraPitch);
//                mc.thePlayer.rotationYaw = actual.getYaw();
//                mc.thePlayer.rotationPitch = actual.getPitch();
//                break;
//            }
//            case POST: {
//                if (this.prevRotation != null) {
//                    mc.thePlayer.rotationYaw = prevRotation.getYaw();
//                    mc.thePlayer.rotationPitch = prevRotation.getPitch();
//
//                    this.prevRotation = null;
//                }
//                break;
//            }
//            default: {
//                break;
//            }
//        }
//    }
//
//    @SubscribeEvent
//    public void onSendPacket(SendPacketEvent event) {
//        if (!(event.packet instanceof C03PacketPlayer)) {
//            return;
//        }
//
//        final C03PacketPlayer packet = (C03PacketPlayer) event.packet;
//        if (packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
//            this.serverRotation = new RotationUtils.Rotation(packet.getYaw(), packet.getPitch());
//        }
//    }
//
//    public Optional<RotationUtils.Rotation> getEffectiveRotation() {
//        return Optional.ofNullable(this.serverRotation);
//    }
}
