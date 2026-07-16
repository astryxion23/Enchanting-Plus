package net.darkhax.eplus.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;

import net.darkhax.eplus.EnchLogic;
import net.darkhax.eplus.EnchantingPlus;
import net.darkhax.eplus.api.event.InfoBoxEvent;
import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.network.messages.MessageEnchant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.minecraftforge.common.MinecraftForge;

public class GuiAdvancedTable extends AbstractContainerScreen<ContainerAdvancedTable> {

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

    public GuiAdvancedTable(ContainerAdvancedTable container, Inventory inv, Component title) {
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
        this.enchantButton = new Button(this.leftPos + 35, this.topPos + 38, 26, 20, Component.empty(), btn -> {
            if (this.canClientAfford()) {
                EnchantingPlus.NETWORK.sendToServer(new MessageEnchant());
                this.logic.enchantItem();
            }
        }, (supplier) -> net.minecraft.network.chat.Component.empty()) {
            @Override
            public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
                guiGraphics.renderItem(icon, this.getX() + 5, this.getY() + 2);
            }
        };
        this.addRenderableWidget(this.enchantButton);
        this.addRenderableWidget(this.scrollbar);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        if (this.enchantButton != null && this.minecraft != null && this.minecraft.player != null)
            this.enchantButton.active = this.minecraft.player.isCreative() || this.minecraft.player.experienceLevel >= this.logic.getCost();
        this.updateLabels();
        this.populateEnchantmentSliders();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, this.title, 32, 5, 0x404040, false);
        int cost = this.logic.getCost();
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
        Component xpText = Component.literal("XP: " + cost);
        // Center under book icon
        int textWidth = this.font.width(xpText);
        // Approximate center of book icon inside GUI
        int bookCenterX = 44; // adjust slightly if needed (32–36 range)
        // Compute centered X
        int x = bookCenterX - (textWidth / 2);
        // Keep existing vertical position
        int y = 62;
        guiGraphics.drawString(this.font, xpText, x, y, color, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
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
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        for (GuiEnchantmentLabel label : this.enchantmentList)
            label.draw(guiGraphics, this.font);
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

        this.listOffset = Mth.clamp(this.listOffset, 0, maxOffset);

        // Sync slider position
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
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        if (this.isMouseOverInfoRegion(x, y)) {
            List<String> info = this.getInfoBox();
            if (!info.isEmpty()) {
                List<Component> comp = new ArrayList<>();
                for (String s : info) comp.add(Component.literal(s));
                guiGraphics.renderComponentTooltip(this.font, comp, x, y);
            }
        }

        if (this.enchantButton.isHovered()) {
            List<Component> text = new ArrayList<>();
            if (!this.canClientAfford()) text.add(Component.translatable("gui.eplus.tooltip.tooexpensive"));
            else if (this.logic.getCost() == 0) text.add(Component.translatable("gui.eplus.tooltip.nochange"));
            else text.add(Component.translatable("gui.eplus.tooltip.enchant"));
            guiGraphics.renderComponentTooltip(this.font, text, x, y);
        }

        if (hasShiftDown()) {
            GuiEnchantmentLabel label = this.getLabelUnderMouse(x, y);
            if (label != null && label.isVisible())
                guiGraphics.renderComponentTooltip(this.font, Collections.singletonList(Component.literal(label.getDescription())), x, y);
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
            int playerLevels = isCreative ? Integer.MAX_VALUE : this.logic.getPlayer().experienceLevel;
            int cost = this.logic.getCost();
            info.add(isCreative ? I18n.get("eplus.info.infinity") : I18n.get("eplus.info.playerxp", playerLevels));
            info.add(I18n.get("eplus.info.costxp", cost));
            info.add(I18n.get("eplus.info.power", this.logic.getEnchantmentPower()) + "%");
            if (cost > playerLevels) {
                info.add(" ");
                info.add(ChatFormatting.RED + I18n.get("gui.eplus.info.tooexpensive"));
            }
        }
        info.add(" ");
        String shiftKey = minecraft.options.keyShift.getKey().getDisplayName().getString();
        info.add(ChatFormatting.YELLOW + I18n.get("eplus.info.tip.prefix") + ChatFormatting.RESET + I18n.get("eplus.info.tip." + this.tips[this.currentTip], shiftKey));
        MinecraftForge.EVENT_BUS.post(new InfoBoxEvent(this, info));
        return info;
    }

    public boolean canClientAfford() {
        return (minecraft.player != null && minecraft.player.isCreative())
                || this.logic.getCost() <= this.logic.getPlayer().experienceLevel;
    }
}
