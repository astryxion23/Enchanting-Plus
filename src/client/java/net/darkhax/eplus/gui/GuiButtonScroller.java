package net.darkhax.eplus.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class GuiButtonScroller extends Button {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("eplus", "textures/gui/enchant.png");
    public int sliderY = 1;
    public GuiAdvancedTable parent;

    public GuiButtonScroller(GuiAdvancedTable parent, int x, int y, int widthIn, int heightIn) {
        super(x, y, widthIn, heightIn, Component.empty(), b -> {}, DEFAULT_NARRATION);
        this.parent = parent;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.getX(), this.getY() + this.sliderY, this.parent.isSliding || this.parent.enchantmentListAll.size() <= 4 ? this.width : 0, 182, this.width, this.height, 256, 256);
            if (this.parent.isSliding) {
                this.sliderY = mouseY - this.getY() - 7;
                this.sliderY = Math.max(1, this.sliderY);
                this.sliderY = Math.min(56, this.sliderY);
                this.parent.updateLabels();
                int total = this.parent.enchantmentListAll.size();
                int visible = 4;
                int maxOffset = Math.max(0, total - visible);

                float scrollPercent = (float) (this.sliderY - 1) / 55.0f;
                this.parent.listOffset = Math.round(scrollPercent * maxOffset);
            }
        }
    }
}
