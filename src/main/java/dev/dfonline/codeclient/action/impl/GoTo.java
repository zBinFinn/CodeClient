package dev.dfonline.codeclient.action.impl;

import dev.dfonline.codeclient.CodeClient;
import dev.dfonline.codeclient.Utility;
import dev.dfonline.codeclient.action.Action;
import dev.dfonline.codeclient.location.Dev;
import dev.dfonline.codeclient.hypercube.item.Location;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class GoTo extends Action {
    public final Vec3d target;
    /**
     * If not null, we are using a location item to teleport.
     */
    private ItemStack locationItem;
    private boolean active;
    private int buffer = 0;

    public GoTo(Vec3d target, Callback callback) {
        super(callback);
        this.target = target;
    }

    @Override
    public void init() {
        active = true;
        if(CodeClient.MC.player == null) return;
        if(CodeClient.location instanceof Dev dev && dev.isInArea(target)) {
            locationItem = new Location(dev.getPos().relativize(target)).setRotation(CodeClient.MC.player.getPitch(), CodeClient.MC.player.getYaw()).toItemStack();
            locationItemTeleport();
        }
    }

    @Override
    public boolean onReceivePacket(Packet<?> packet) {
        if(locationItem == null) return super.onReceivePacket(packet);
        if(packet instanceof PlayerPositionLookS2CPacket tp) {
            if(CodeClient.MC.getNetworkHandler() == null || CodeClient.MC.player == null) return false;
            CodeClient.MC.getNetworkHandler().sendPacket(new TeleportConfirmC2SPacket(tp.getTeleportId()));
            CodeClient.MC.player.setPosition(target);
            active = false;
            callback();
            return true;
        }
        return false;
    }

    @Override
    public void onTick() {
        if(CodeClient.MC.player == null || CodeClient.MC.getNetworkHandler() == null) {
            callback();
            return;
        }

        CodeClient.MC.player.setVelocity(0,0,0);
        if(CodeClient.MC.player.getPos().equals(target)) {
            active = false;
            callback();
            return;
        }
        if(locationItem != null) return;
        hackTowards();
    }

    private void locationItemTeleport() {
        ClientPlayerEntity player = CodeClient.MC.player;
        ClientPlayNetworkHandler net = CodeClient.MC.getNetworkHandler();
        if(player == null || net == null) return;
        ItemStack handItem = player.getMainHandStack();
        Utility.sendHandItem(locationItem);
        boolean sneaky = !player.isSneaking();
        if(sneaky) net.sendPacket(new ClientCommandC2SPacket(player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));
        net.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, player.getBlockPos(), Direction.UP));
        net.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, player.getBlockPos(), Direction.UP));
        if(sneaky) net.sendPacket(new ClientCommandC2SPacket(player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));
        Utility.sendHandItem(handItem);
    }

    /**
     * Use hacky movement packets.
     */
    private void hackTowards() {
        if(CodeClient.MC.player == null || CodeClient.MC.getNetworkHandler() == null) return;

        int bufferStop = 1;
        int bufferReset = 0;

        buffer++;
        if(buffer > bufferStop) {
            if(buffer >= bufferReset) {
                buffer = 0;
            }
            return;
        }
        if(!active) return;
        if(CodeClient.MC.player == null) return;
        Vec3d pos = CodeClient.MC.player.getPos();

        Vec3d offset = pos.relativize(target);
        double maxLength = 50;
        double distance = offset.length();
        Vec3d jump = distance > maxLength ? pos.add(offset.normalize().multiply(maxLength)) : target;
        if(distance > 10) {
            CodeClient.MC.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
            CodeClient.MC.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
            CodeClient.MC.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
            CodeClient.MC.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
            CodeClient.MC.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
            CodeClient.MC.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(false));
        }
        CodeClient.MC.player.setPos(jump.x, jump.y, jump.z);
    }
}