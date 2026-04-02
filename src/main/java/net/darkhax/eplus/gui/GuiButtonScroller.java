package net.darkhax.eplus.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;

public class GuiButtonScroller extends Button {

    private static final ResourceLocation TEXTURE = new ResourceLocation("eplus", "textures/gui/enchant.png");
    public int sliderY = 1;
    public GuiAdvancedTable parent;

    public GuiButtonScroller(GuiAdvancedTable parent, int x, int y, int widthIn, int heightIn) {
        super(x, y, widthIn, heightIn, net.minecraft.util.text.StringTextComponent.EMPTY, b -> {});
        this.parent = parent;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.parent.getMinecraft().getTextureManager().bind(TEXTURE);
            this.parent.blit(matrixStack, this.x, this.y + this.sliderY, this.parent.isSliding || this.parent.enchantmentListAll.size() <= 4 ? this.width : 0, 182, this.width, this.height);
            if (this.parent.isSliding) {
                this.sliderY = mouseY - this.y - 7;
                this.sliderY = Math.max(1, this.sliderY);
                this.sliderY = Math.min(56, this.sliderY);
                this.parent.updateLabels();
                int total = this.parent.enchantmentListAll.size();
                int visible = 4;
                int maxOffset = Math.max(0, total - visible);

                float scrollPercent = (float)(this.sliderY - 1) / 55.0f;
                this.parent.listOffset = Math.round(scrollPercent * maxOffset);
            }
        }
    }
}
