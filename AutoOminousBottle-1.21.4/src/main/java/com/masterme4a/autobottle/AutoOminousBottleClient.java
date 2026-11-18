package com.masterme4a.autobottle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffects;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class AutoOminousBottleClient implements ClientModInitializer {
    private static boolean enabled = false;
    private static int useCooldown = 0;
    private static int previousSlot = -1;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(literal("autobottle")
                .then(literal("on").executes(context -> {
                    enabled = true;
                    context.getSource().sendFeedback(Text.literal("Auto Ominous Bottle: включено"));
                    return 1;
                }))
                .then(literal("off").executes(context -> {
                    enabled = false;
                    previousSlot = -1;
                    context.getSource().sendFeedback(Text.literal("Auto Ominous Bottle: выключено"));
                    return 1;
                }))
                .then(literal("status").executes(context -> {
                    context.getSource().sendFeedback(Text.literal("Auto Ominous Bottle: " + (enabled ? "включено" : "выключено")));
                    return 1;
                }))
                .executes(context -> {
                    context.getSource().sendFeedback(Text.literal("Auto Ominous Bottle: " + (enabled ? "включено" : "выключено") + ". Используй /autobottle on или /autobottle off"));
                    return 1;
                })
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> tick(client));
    }

    private static void tick(MinecraftClient client) {
        if (!enabled || client.player == null || client.interactionManager == null || client.world == null) {
            previousSlot = -1;
            return;
        }
        if (useCooldown > 0) {
            useCooldown--;
            return;
        }

        // Don't waste bottles while Bad Omen is already active.
        if (client.player.hasStatusEffect(StatusEffects.BAD_OMEN)) {
            restoreSelectedSlot(client);
            return;
        }

        int bottleSlot = findBottleInHotbar(client);
        if (bottleSlot < 0) {
            restoreSelectedSlot(client);
            return;
        }

        if (client.player.isUsingItem()) return;

        if (client.player.getInventory().selectedSlot != bottleSlot) {
            previousSlot = client.player.getInventory().selectedSlot;
            client.player.getInventory().setSelectedSlot(bottleSlot);
            useCooldown = 2;
            return;
        }

        client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
        useCooldown = 20;
    }

    private static int findBottleInHotbar(MinecraftClient client) {
        for (int i = 0; i < 9; i++) {
            Item item = client.player.getInventory().getStack(i).getItem();
            if (item == Items.OMINOUS_BOTTLE) return i;
        }
        return -1;
    }

    private static void restoreSelectedSlot(MinecraftClient client) {
        if (previousSlot >= 0 && previousSlot < 9 && client.player != null) {
            client.player.getInventory().setSelectedSlot(previousSlot);
            previousSlot = -1;
        }
    }
}
