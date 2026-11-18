package com.masterme4a.itemscroller.client;

import com.masterme4a.itemscroller.Masterme4aItemScroller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Left-click an occupied container slot to perform vanilla QUICK_MOVE immediately.
 * This is the same server-authoritative action as Shift-click, without holding Shift.
 */
@Mod.EventBusSubscriber(
        modid = Masterme4aItemScroller.MOD_ID,
        value = net.minecraftforge.api.distmarker.Dist.CLIENT
)
public final class ScrollerClientEvents {
    private ScrollerClientEvents() {}

    @SubscribeEvent
    public static void onMouseClicked(GuiScreenEvent.MouseClickedEvent.Pre event) {
        if (!(event.getGui() instanceof AbstractContainerScreen<?> screen) || event.getButton() != 0) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.gameMode == null || minecraft.level == null) {
            return;
        }

        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        Slot hovered = null;

        for (Slot slot : screen.getMenu().slots) {
            int x = screen.getGuiLeft() + slot.x;
            int y = screen.getGuiTop() + slot.y;
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                hovered = slot;
                break;
            }
        }

        if (hovered == null || !hovered.hasItem() || !hovered.isActive() || !hovered.mayPickup(minecraft.player)) {
            return;
        }

        // Vanilla QUICK_MOVE: container and server validate the transfer.
        minecraft.gameMode.handleInventoryMouseClick(
                screen.getMenu().containerId,
                hovered.index,
                0,
                ClickType.QUICK_MOVE,
                minecraft.player
        );
        event.setCanceled(true);
    }
}
