package com.solegendary.reignofnether.player;

import com.solegendary.reignofnether.building.Building;
import com.solegendary.reignofnether.building.BuildingClientEvents;
import com.solegendary.reignofnether.fogofwar.FogOfWarClientEvents;
import com.solegendary.reignofnether.hud.HudClientEvents;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.orthoview.OrthoviewClientEvents;
import com.solegendary.reignofnether.registrars.SoundRegistrar;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.resources.ResourcesClientEvents;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerClientEvents {

    public static boolean isRTSPlayer = false;

    public static long rtsGameTicks = 0;

    private static final Minecraft MC = Minecraft.getInstance();

    public static boolean rtsLocked = false;

    public static boolean canStartRTS = true;

    @SubscribeEvent
    public static void onRegisterCommand(RegisterClientCommandsEvent evt) {
        evt.getDispatcher().register(Commands.literal("rts-surrender").executes((command) -> {
            PlayerServerboundPacket.surrender();
            return 1;
        }));
        evt.getDispatcher().register(Commands.literal("rts-reset").executes((command) -> {
            if (MC.player != null && MC.player.hasPermissions(4)) {
                PlayerServerboundPacket.resetRTS();
                return 1;
            }
            return 0;
        }));
        evt.getDispatcher()
            .register(Commands.literal("rts-lock").then(Commands.literal("enable").executes((command) -> {
                if (MC.player != null && MC.player.hasPermissions(4)) {
                    PlayerServerboundPacket.lockRTS();
                    return 1;
                }
                return 0;
            })));
        evt.getDispatcher().register(Commands.literal("rts-reset").executes((command) -> {
            if (MC.player != null && MC.player.hasPermissions(4)) {
                PlayerServerboundPacket.resetRTS();
                return 1;
            }
            return 0;
        }));
        evt.getDispatcher()
            .register(Commands.literal("rts-lock").then(Commands.literal("enable").executes((command) -> {
                if (MC.player != null && MC.player.hasPermissions(4)) {
                    PlayerServerboundPacket.lockRTS();
                    return 1;
                }
                return 0;
            })));
        evt.getDispatcher()
            .register(Commands.literal("rts-lock").then(Commands.literal("disable").executes((command) -> {
                if (MC.player != null && MC.player.hasPermissions(4)) {
                    PlayerServerboundPacket.unlockRTS();
                    return 1;
                }
                return 0;
            })));
        evt.getDispatcher()
            .register(Commands.literal("rts-syncing").then(Commands.literal("enable").executes((command) -> {
                if (MC.player != null && MC.player.hasPermissions(4)) {
                    PlayerServerboundPacket.enableRTSSyncing();
                    return 1;
                }
                return 0;
            })));
        evt.getDispatcher()
            .register(Commands.literal("rts-syncing").then(Commands.literal("disable").executes((command) -> {
                if (MC.player != null && MC.player.hasPermissions(4)) {
                    PlayerServerboundPacket.disableRTSSyncing();
                    return 1;
                }
                return 0;
            })));
        evt.getDispatcher().register(Commands.literal("rts-help").executes((command) -> {
            if (MC.player != null) {
                MC.player.sendSystemMessage(Component.literal(" "));
                MC.player.sendSystemMessage(Component.literal(
                    "/rts-fog enable/disable - Toggle fog of war for all players"));
                MC.player.sendSystemMessage(Component.literal("/rts-surrender - Concede the match"));
                MC.player.sendSystemMessage(Component.literal(
                    "/rts-reset - Delete all units/buildings, set all to spectator"));
                MC.player.sendSystemMessage(Component.literal(
                    "/rts-lock enable/disable - Prevent all players from joining the RTS match"));
                MC.player.sendSystemMessage(Component.literal(
                    "/gamerule doLogFalling - Set whether tree logs fall when cut"));
                MC.player.sendSystemMessage(Component.literal(
                    "/gamerule neutralAggro - Set whether you auto-attack neutral units/buildings"));
            }
            return 1;
        }));
        evt.getDispatcher()
            .register(Commands.literal("rts-syncing").then(Commands.literal("enable").executes((command) -> {
                if (MC.player != null && MC.player.hasPermissions(4)) {
                    PlayerServerboundPacket.enableRTSSyncing();
                    return 1;
                }
                return 0;
            })));
        evt.getDispatcher()
            .register(Commands.literal("rts-syncing").then(Commands.literal("disable").executes((command) -> {
                if (MC.player != null && MC.player.hasPermissions(4)) {
                    PlayerServerboundPacket.disableRTSSyncing();
                    return 1;
                }
                return 0;
            })));
        evt.getDispatcher().register(Commands.literal("rts-help").executes((command) -> {
            if (MC.player != null) {
                MC.player.sendSystemMessage(Component.literal(" "));
                MC.player.sendSystemMessage(Component.translatable("commands.reignofnether.toggle_fow",
                    "/rts-fog enable/disable"
                ));
                MC.player.sendSystemMessage(Component.translatable("commands.reignofnether.surrender",
                    "/rts-surrender"
                ));
                MC.player.sendSystemMessage(Component.translatable("commands.reignofnether.reset", "/rts-reset"));
                MC.player.sendSystemMessage(Component.translatable("commands.reignofnether.lock",
                    "/rts-lock enable/disable"
                ));
            }
            return 1;
        }));
        evt.getDispatcher().register(Commands.literal("rts-controls").executes((command) -> {
            if (MC.player != null) {
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.toggle_cam"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.refresh_chunks"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.toggle_fps_tps"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.toggle_leaves"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.deselect"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.command"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.create_group"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.recenter_map"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.select_same"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.destroy"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.rotate_cam"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.zoom"));
                MC.player.sendSystemMessage(Component.translatable("controls.reignofnether.rotate"));
            }
            return 1;
        }));
    }

    public static void defeat(String playerName) {
        if (MC.player == null) {
            return;
        }

        // remove control of this player's buildings for all players' clients
        for (Building building : BuildingClientEvents.getBuildings())
            if (building.ownerName.equals(playerName)) {
                building.ownerName = "";
            }

        if (!MC.player.getName().getString().equals(playerName)) {
            return;
        }

        disableRTS(playerName);
        MC.gui.setTitle(Component.translatable("titles.reignofnether.defeated"));
        MC.player.playSound(SoundRegistrar.DEFEAT.get(), 0.5f, 1.0f);

        ResourcesClientEvents.resourcesList.removeIf(r -> r.ownerName.equals(MC.player.getName().getString()));
    }

    public static void victory(String playerName) {
        if (MC.player == null || !MC.player.getName().getString().equals(playerName)) {
            return;
        }

        MC.gui.setTitle(Component.translatable("titles.reignofnether.victorious"));
        MC.player.playSound(SoundRegistrar.VICTORY.get(), 0.5f, 1.0f);
    }

    public static void enableRTS(String playerName) {
        if (MC.player != null && MC.player.getName().getString().equals(playerName)) {
            isRTSPlayer = true;
        }
    }

    public static void disableRTS(String playerName) {
        if (MC.player != null && MC.player.getName().getString().equals(playerName)) {
            isRTSPlayer = false;
        }
    }

    @SubscribeEvent
    public static void onPlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent evt) {
        // LOG OUT FROM SINGLEPLAYER WORLD ONLY
        if (MC.player != null && evt.getEntity().getId() == MC.player.getId()) {
            resetRTS();
            FogOfWarClientEvents.movedToCapitol.set(false);
            FogOfWarClientEvents.frozenChunks.clear();
            FogOfWarClientEvents.semiFrozenChunks.clear();
            OrthoviewClientEvents.unlockCam();
        }
    }

    @SubscribeEvent
    public static void onPlayerLoginEvent(PlayerEvent.PlayerLoggedInEvent evt) {
        // LOG IN TO SINGLEPLAYER WORLD ONLY
        if (MC.player != null && evt.getEntity().getId() == MC.player.getId()) {
            FogOfWarClientEvents.updateFogChunks();
        }
    }

    @SubscribeEvent
    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut evt) {
        // LOG OUT FROM SERVER WORLD ONLY
        if (MC.player != null && evt.getPlayer() != null && evt.getPlayer().getId() == MC.player.getId()) {
            resetRTS();
            FogOfWarClientEvents.movedToCapitol.set(false);
            FogOfWarClientEvents.frozenChunks.clear();
            FogOfWarClientEvents.semiFrozenChunks.clear();
        }
    }

    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn evt) {
        // LOG IN TO SERVER WORLD ONLY
        if (MC.player != null && evt.getPlayer().getId() == MC.player.getId()) {
            FogOfWarClientEvents.updateFogChunks();
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END) {
            rtsGameTicks += 1;
        }
    }

    // disallow opening the creative menu while orthoview is enabled
    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening evt) {
        if (OrthoviewClientEvents.isEnabled() && evt.getScreen() instanceof CreativeModeInventoryScreen) {
            evt.setCanceled(true);
        }
    }

    // allow tab player list menu on the orthoview screen
    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Render evt) {
        if (OrthoviewClientEvents.isEnabled() && Keybindings.tab.isDown() && MC.level != null) {
            if (!MC.isLocalServer()) {
                MC.gui.getTabList().setVisible(true);
                MC.gui.getTabList()
                    .render(evt.getPoseStack(), MC.getWindow().getGuiScaledWidth(), MC.level.getScoreboard(), null);
            } else {
                MC.gui.getTabList().setVisible(false);
            }
        }
    }

    public static void syncRtsGameTime(Long gameTicks) {
        rtsGameTicks = gameTicks;
    }

    public static void resetRTS() {
        isRTSPlayer = false;

        HudClientEvents.controlGroups.clear();
        UnitClientEvents.getSelectedUnits().clear();
        UnitClientEvents.getPreselectedUnits().clear();
        UnitClientEvents.getAllUnits().clear();
        UnitClientEvents.idleWorkerIds.clear();
        ResearchClient.removeAllResearch();
        ResearchClient.removeAllCheats();
        BuildingClientEvents.getSelectedBuildings().clear();
        BuildingClientEvents.getBuildings().clear();
        ResourcesClientEvents.resourcesList.clear();
    }

    public static void setRTSLock(boolean lock) {
        rtsLocked = lock;
    }

    public static void setCanStartRTS(boolean canStart) {
        canStartRTS = canStart;
    }
}
