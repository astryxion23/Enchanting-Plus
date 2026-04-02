
package net.darkhax.eplus.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.darkhax.eplus.EnchLogic;
import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.api.event.InfoBoxEvent;
import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.network.messages.MessageEnchant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;

public class GuiAdvancedTable extends ContainerScreen<ContainerAdvancedTable> {

    private static final ItemStack SPOOKY_BONE = new ItemStack(Items.BONE);
    static {
        SPOOKY_BONE.enchant(Enchantments.PROJECTILE_PROTECTION, 1);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation("eplus", "textures/gui/enchant.png");
    private static final Random RAND = new Random();

    private Button enchantButton;
    public final List<GuiEnchantmentLabel> enchantmentListAll = new ArrayList<>();
    public final List<GuiEnchantmentLabel> enchantmentList = new ArrayList<>();
    public GuiEnchantmentLabel selected;
    public int listOffset = 0;
    public boolean isSliding;
    public GuiButtonScroller scrollbar;

    private final String[] tips = { "description", "books", "treasure", "curse", "storage", "inventory", "armor" };
    private final int currentTip;
    private final EnchantmentLogicController logic;
    private int totalCost = 0;

    public GuiAdvancedTable(ContainerAdvancedTable container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        this.logic = container.logic;
        this.imageWidth = 235;
        this.imageHeight = 182;
        this.currentTip = RAND.nextInt(this.tips.length);
    }

    @Override
    protected void init() {
        super.init();
        this.isSliding = false;
        this.scrollbar = new GuiButtonScroller(this, this.leftPos + 206, this.topPos + 16, 12, 15);
        ItemStack icon = EnchLogic.isWikedNight(this.logic.getWorld()) ? SPOOKY_BONE : new ItemStack(Items.ENCHANTED_BOOK);
        this.enchantButton = new Button(this.leftPos + 35, this.topPos + 38, 26, 20, StringTextComponent.EMPTY, btn -> {
            if (this.canClientAfford()) {
                EnchantingPlus.NETWORK.sendToServer(new MessageEnchant());
                this.logic.enchantItem();
            }
        }) {
            @Override
            public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
                if (this.visible && GuiAdvancedTable.this.getMinecraft() != null) {
                    GuiAdvancedTable.this.getMinecraft().getItemRenderer().renderAndDecorateItem(icon, this.x + 5, this.y + 2);
                }
            }
        };
        this.addButton(this.enchantButton);
        this.addButton(this.scrollbar);
    }

    /** Linear 1–5 XP cost; treasure enchantments always 4. */
    public static int xpCost(Enchantment ench, int level) {
        if (ench.isTreasureOnly()) {
            return 4;
        }
        return Math.max(1, Math.min(level, 5));
    }

    public int calculateTotalCost() {
        int total = 0;
        ItemStack stack = this.menu.getSlot(0).getItem();
        Map<Enchantment, Integer> existing = EnchantmentHelper.getEnchantments(stack);

        for (Entry<Enchantment, Integer> e : this.logic.getCurrentEnchantments().entrySet()) {
            Enchantment enchant = e.getKey();
            int selectedLevel = e.getValue();
            int existingLevel = existing.getOrDefault(enchant, 0);

            if (selectedLevel > existingLevel) {
                int levelDifference = selectedLevel - existingLevel;
                total += xpCost(enchant, levelDifference);
            }
        }
        return Math.max(0, total);
    }

    @Override
    public void tick() {
        super.tick();
        this.totalCost = calculateTotalCost();
        if (this.enchantButton != null && this.minecraft != null && this.minecraft.player != null)
            this.enchantButton.active = this.minecraft.player.isCreative() || this.minecraft.player.experienceLevel >= this.totalCost;
        this.updateLabels();
        this.populateEnchantmentSliders();
    }

    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.font.draw(matrixStack, this.title, 32, 5, 0x404040);
        int cost = calculateTotalCost();
        int playerXP = this.minecraft != null && this.minecraft.player != null ? this.minecraft.player.experienceLevel : 0;
        boolean creative = this.minecraft != null && this.minecraft.player != null && this.minecraft.player.isCreative();
        int color;
        if (cost == 0) {
            color = 0xAAAAAA;
        } else if (creative) {
            color = 0x80FF20;
        } else if (playerXP >= cost) {
            color = 0x80FF20;
        } else {
            color = 0xFF4040;
        }
        ITextComponent xpText = new StringTextComponent("XP: " + cost);
        int textWidth = this.font.width(xpText.getString());
        int bookCenterX = 44;
        int x = bookCenterX - (textWidth / 2);
        int y = 62;
        this.font.draw(matrixStack, xpText.getString(), x, y, color);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    public void populateEnchantmentSliders() {
        this.enchantmentListAll.clear();
        int labelCount = 0;
        for (Enchantment enchant : this.logic.getValidEnchantments()) {
            GuiEnchantmentLabel label = new GuiEnchantmentLabel(this, this.logic, enchant, this.logic.getCurrentLevel(enchant), 35 + 26 + this.leftPos, 15 + this.topPos + labelCount++ * 18);
            label.setCurrentLevel(this.logic.getCurrentLevel(enchant));
            this.enchantmentListAll.add(label);
        }
    }

    public void updateLabels() {
        this.enchantmentList.clear();
        int count = 0;
        for (int i = 0; i < this.enchantmentListAll.size(); i++) {
            if (i < this.listOffset) continue;
            GuiEnchantmentLabel label = this.enchantmentListAll.get(i);
            label.setyPos(15 + this.topPos + count++ * 18);
            label.setVisible(label.getyPos() >= this.topPos + 15 && label.getyPos() < this.topPos + 87);
            this.enchantmentList.add(label);
        }
        this.lockLabels();
    }

    public void lockLabels() {
        for (GuiEnchantmentLabel label : this.enchantmentListAll) {
            label.setLocked(false);
            Enchantment enchantment = label.getEnchantment();
            for (Entry<Enchantment, Integer> data : this.logic.getCurrentEnchantments().entrySet()) {
                boolean isIncompatable = enchantment != data.getKey() && data.getValue() > 0 && !data.getKey().isCompatibleWith(enchantment);
                boolean isOverLeveled = enchantment == data.getKey() && data.getValue() > enchantment.getMaxLevel();
                if (isOverLeveled || isIncompatable) {
                    label.setLocked(true);
                    break;
                }
            }
        }
    }

    public GuiEnchantmentLabel getLabelUnderMouse(int mx, int my) {
        for (GuiEnchantmentLabel label : this.enchantmentList)
            if (label.isMouseOver(mx, my)) return label;
        return null;
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1, 1, 1, 1);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        for (GuiEnchantmentLabel label : this.enchantmentList)
            label.draw(matrixStack, this.font);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double delta) {
        int total = this.enchantmentListAll.size();
        int visible = 4;
        int maxOffset = Math.max(0, total - visible);

        if (delta > 0) {
            this.listOffset--;
        } else if (delta < 0) {
            this.listOffset++;
        }

        this.listOffset = MathHelper.clamp(this.listOffset, 0, maxOffset);

        if (maxOffset > 0) {
            float percent = (float) this.listOffset / maxOffset;
            this.scrollbar.sliderY = 1 + Math.round(percent * 55f);
        }

        this.updateLabels();
        return true;
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int mouseX = (int) mx;
        int mouseY = (int) my;
        this.selected = this.getLabelUnderMouse(mouseX, mouseY);
        if (this.selected != null && !this.selected.isLocked() && this.selected.isVisible())
            this.selected.setDragging(true);
        else this.selected = null;
        if (this.enchantmentListAll.size() > 4 && mouseX > this.leftPos + 206 && mouseX < this.leftPos + 218
                && mouseY > this.topPos + 16 + this.scrollbar.sliderY && mouseY < this.topPos + 31 + this.scrollbar.sliderY)
            this.isSliding = true;
        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean mouseReleased(double mx, double my, int button) {
        if (this.selected != null) {
            this.selected.setDragging(false);
            this.selected = null;
            this.lockLabels();
        }
        this.isSliding = false;
        return super.mouseReleased(mx, my, button);
    }

    @Override
    public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
        if (this.selected != null) {
            int mouseX = (int) mx;
            if (mouseX < this.selected.getxPos() || mouseX > this.selected.getxPos() + this.selected.getWidth()) {
                this.selected.setDragging(false);
                this.selected = null;
                return true;
            }
            int trackRelativeX = mouseX - (this.selected.getxPos() + 1);
            this.selected.updateSlider(trackRelativeX);
            this.lockLabels();
        }
        return super.mouseDragged(mx, my, button, dx, dy);
    }

    private boolean isMouseOverInfoRegion(int mouseX, int mouseY) {
        return mouseX >= this.leftPos && mouseX <= this.leftPos + 18 && mouseY >= this.topPos && mouseY <= this.topPos + 18;
    }

    @Override
    protected void renderTooltip(MatrixStack matrixStack, int x, int y) {
        super.renderTooltip(matrixStack, x, y);
        if (this.isMouseOverInfoRegion(x, y)) {
            List<String> info = this.getInfoBox();
            if (!info.isEmpty()) {
                List<ITextComponent> comp = new ArrayList<>();
                for (String s : info) comp.add(new StringTextComponent(s));
                this.renderComponentTooltip(matrixStack, comp, x, y);
            }
        }

        if (this.enchantButton.isHovered()) {
            List<ITextComponent> text = new ArrayList<>();
            if (!this.canClientAfford()) text.add(new TranslationTextComponent("gui.eplus.tooltip.tooexpensive"));
            else if (this.logic.getCost() == 0) text.add(new TranslationTextComponent("gui.eplus.tooltip.nochange"));
            else text.add(new TranslationTextComponent("gui.eplus.tooltip.enchant"));
            this.renderComponentTooltip(matrixStack, text, x, y);
        }

        if (hasShiftDown()) {
            GuiEnchantmentLabel label = this.getLabelUnderMouse(x, y);
            if (label != null && label.isVisible())
                this.renderComponentTooltip(matrixStack, Collections.singletonList(new StringTextComponent(label.getDescription())), x, y);
        }
    }

    private List<String> getInfoBox() {
        List<String> info = new ArrayList<>();
        if (this.logic.getInventory().getEnchantingStack().isEmpty())
            info.add(I18n.get("gui.eplus.info.noitem"));
        else if (this.enchantmentListAll.isEmpty())
            info.add(I18n.get("gui.eplus.info.noench"));
        else {
            boolean isCreative = minecraft.player != null && minecraft.player.isCreative();
            int playerXP = isCreative ? Integer.MAX_VALUE : EnchLogic.getExperience(this.logic.getPlayer());
            int cost = this.logic.getCost();
            info.add(isCreative ? I18n.get("eplus.info.infinity") : I18n.get("eplus.info.playerxp", playerXP));
            info.add(I18n.get("eplus.info.costxp", cost));
            info.add(I18n.get("eplus.info.power", this.logic.getEnchantmentPower()) + "%");
            if (cost > playerXP) {
                info.add(" ");
                info.add(TextFormatting.RED + I18n.get("gui.eplus.info.tooexpensive"));
            }
        }
        info.add(" ");
        String shiftKey = I18n.get("key.sneak");
        info.add(TextFormatting.YELLOW + I18n.get("eplus.info.tip.prefix") + TextFormatting.RESET + I18n.get("eplus.info.tip." + this.tips[this.currentTip], shiftKey));
        MinecraftForge.EVENT_BUS.post(new InfoBoxEvent(this, info));
        return info;
    }

    public boolean canClientAfford() {
        return this.logic.getCost() <= EnchLogic.getExperience(this.logic.getPlayer()) || (minecraft.player != null && minecraft.player.isCreative());
    }
}
