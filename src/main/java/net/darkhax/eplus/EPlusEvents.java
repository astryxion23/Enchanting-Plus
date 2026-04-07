package net.darkhax.eplus;

import net.darkhax.eplus.api.event.EnchantmentCostEvent;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class EPlusEvents {

    @FunctionalInterface
    public interface EnchantmentCostCallback {
        void onEnchantmentCost(EnchantmentCostEvent event);
    }

    public static final Event<EnchantmentCostCallback> ENCHANTMENT_COST = EventFactory.createArrayBacked(EnchantmentCostCallback.class, callbacks -> event -> {
        for (EnchantmentCostCallback callback : callbacks) {
            callback.onEnchantmentCost(event);
        }
    });

    private EPlusEvents() {
    }
}
