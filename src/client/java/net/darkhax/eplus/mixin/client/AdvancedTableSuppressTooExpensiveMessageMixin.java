package net.darkhax.eplus.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.darkhax.eplus.gui.GuiAdvancedTable;
import net.minecraft.client.resources.language.I18n;

/**
 * Hides all "too expensive" style messaging for the advanced table UI without changing cost logic,
 * GUI classes, or translation entries. Tooltip affordance checks in {@link GuiAdvancedTable#extractTooltip}
 * are overridden for display only; the enchant button still uses the real {@link GuiAdvancedTable#canClientAfford()}.
 */
@Mixin(GuiAdvancedTable.class)
public class AdvancedTableSuppressTooExpensiveMessageMixin {

    @Redirect(
        method = "extractTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/darkhax/eplus/gui/GuiAdvancedTable;canClientAfford()Z"
        )
    )
    private boolean eplus_tooltipPretendsPlayerCanAfford(GuiAdvancedTable instance) {
        return true;
    }

    @Inject(method = "getInfoBox", at = @At("RETURN"))
    private void eplus_removeTooExpensiveInfoLines(CallbackInfoReturnable<List<String>> cir) {
        List<String> info = cir.getReturnValue();
        if (info == null || info.isEmpty()) {
            return;
        }
        String suffix = I18n.get("gui.eplus.info.tooexpensive");
        for (int i = info.size() - 1; i >= 0; i--) {
            String line = info.get(i);
            if (line != null && line.endsWith(suffix)) {
                if (i > 0 && " ".equals(info.get(i - 1))) {
                    info.remove(i - 1);
                    i--;
                }
                info.remove(i);
            }
        }
    }
}
