package net.darkhax.eplus.gui;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.network.payload.SliderUpdatePayload;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.resources.Identifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.tags.EnchantmentTags;

public class GuiEnchantmentLabel {

    protected static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("eplus", "textures/gui/enchant.png");
    private static final int HEIGHT = 18;
    private static final int WIDTH = 143;
    private static final int COLOR_BACKGROUND_LOCKED = 0x44d10841;
    private static final int COLOR_BACKGROUND_AVAILABLE = 0x445aaeae;
    private static final int COLOR_TEXT_ENCHANT = 0xA0E060;
    private static final int COLOR_TEXT_LOCKED = 0xFF6060;

    private final EnchantmentLogicController logic;
    private final Enchantment enchantment;
    private final int initialLevel;
    private int currentLevel;
    private final int startingXPos;
    private final int startingYPos;
    private int xPos;
    private int yPos;
    private int sliderX;
    private boolean dragging = false;
    private boolean visible = true;
    private boolean locked = false;
    public final GuiAdvancedTable parent;

    public GuiEnchantmentLabel(GuiAdvancedTable parent, EnchantmentLogicController logic, Enchantment enchant, int level, int x, int y) {
        this.logic = logic;
        this.parent = parent;
        this.enchantment = enchant;
        this.currentLevel = level;
        this.initialLevel = level;
        this.xPos = this.startingXPos = x;
        this.yPos = this.startingYPos = y;
        this.sliderX = this.xPos + 1;
        if (this.currentLevel > this.enchantment.getMaxLevel()) this.locked = true;
    }

    public void draw(GuiGraphicsExtractor graphics, Font font) {
        if (!this.visible) return;
        int indexX = this.dragging ? this.sliderX : (this.currentLevel <= this.enchantment.getMaxLevel() ? (int) (this.xPos + 1 + (WIDTH - 6) * (this.currentLevel / (double) this.enchantment.getMaxLevel())) : this.xPos + 1 + WIDTH - 6);
        graphics.fill(this.xPos + 1, this.yPos + 2, this.xPos + WIDTH, this.yPos + HEIGHT, this.locked ? COLOR_BACKGROUND_LOCKED : COLOR_BACKGROUND_AVAILABLE);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, indexX, this.yPos + 2, this.isSelected() ? 5 : 0, 197, 5, 16, 256, 256);
        int textColor = this.locked ? COLOR_TEXT_LOCKED : COLOR_TEXT_ENCHANT;
        Registry<Enchantment> reg = this.logic.getWorld().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> holder = reg.wrapAsHolder(this.enchantment);
        int displayLevel = Math.max(1, this.currentLevel);
        Component base = Enchantment.getFullname(holder, displayLevel);
        int colorForStyle = isCurse(reg, this.enchantment) ? COLOR_TEXT_LOCKED : textColor;
        Component styled = base.copy().withStyle(style -> style.withColor(TextColor.fromRgb(colorForStyle)));
        // GuiGraphicsExtractor skips text when ARGB alpha is 0; plain 0xRRGGBB literals have alpha 0 in Java.
        int textArgb = 0xFF000000 | (colorForStyle & 0xFFFFFF);
        graphics.text(font, styled, this.xPos + 7, this.yPos + 6, textArgb, true);
    }

    public boolean isSelected() {
        return this.parent.selected != null && this.parent.selected.enchantment == this.enchantment;
    }

    public String getDisplayName() {
        Registry<Enchantment> reg = this.logic.getWorld().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        String descId = "enchantment." + reg.getKey(this.enchantment).toShortLanguageKey();
        String s = I18n.get(descId);
        if (isCurse(reg, this.enchantment)) s = ChatFormatting.RED + s;
        return this.currentLevel <= 0 ? s : s + " " + I18n.get("enchantment.level." + this.currentLevel);
    }

    private static boolean isCurse(Registry<Enchantment> reg, Enchantment enchantment) {
        return reg.wrapAsHolder(enchantment).is(EnchantmentTags.CURSE);
    }

    public void updateSlider(int xPos) {
        if (this.locked) return;
        int min = this.xPos + 1;
        int max = min + WIDTH - 6;
        this.sliderX = min + xPos - 2;
        if (this.sliderX < min) this.sliderX = min;
        else if (this.sliderX > max) this.sliderX = max;
        float index = xPos / (float) (WIDTH - 10);
        int updatedLevel = Math.round(this.initialLevel > this.enchantment.getMaxLevel() ? this.initialLevel * index : this.enchantment.getMaxLevel() * index);
        if (updatedLevel > this.initialLevel || !this.logic.getInventory().getEnchantingStack().isDamaged())
            this.currentLevel = updatedLevel;
        if (this.currentLevel < 0) this.currentLevel = 0;
        else if (this.currentLevel > this.enchantment.getMaxLevel()) this.currentLevel = this.enchantment.getMaxLevel();
        Registry<Enchantment> reg = this.logic.getWorld().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        Identifier id = reg.getKey(this.enchantment);
        ClientPacketDistributor.sendToServer(new SliderUpdatePayload(id, this.currentLevel));
        this.logic.updateEnchantment(this.enchantment, this.currentLevel);
    }

    public int getCurrentLevel() { return this.currentLevel; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
    public int getStartingXPos() { return this.startingXPos; }
    public int getStartingYPos() { return this.startingYPos; }
    public int getxPos() { return this.xPos; }
    public void setxPos(int xPos) { this.xPos = xPos; }
    public int getyPos() { return this.yPos; }
    public void setyPos(int yPos) { this.yPos = yPos; }
    public int getSliderX() { return this.sliderX; }
    public void setSliderX(int sliderX) { this.sliderX = sliderX; }
    public boolean isDragging() { return this.dragging; }
    public void setDragging(boolean dragging) { this.dragging = dragging; }
    public boolean isLocked() { return this.locked; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public int getWidth() { return WIDTH; }
    public boolean isVisible() { return this.visible; }
    public void setVisible(boolean isVisible) { this.visible = isVisible; }
    public int getHeight() { return HEIGHT; }
    public Enchantment getEnchantment() { return this.enchantment; }
    public int getInitialLevel() { return this.initialLevel; }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return this.xPos <= mouseX && this.xPos + this.getWidth() >= mouseX && this.yPos <= mouseY && this.yPos + this.getHeight() >= mouseY;
    }

    public String getDescription() {
        String key = getTranslationKey(this.enchantment);
        String description = I18n.get(key);
        if (description.startsWith("enchantment.")) {
            Registry<Enchantment> reg = this.logic.getWorld().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Identifier rl = reg.getKey(this.enchantment);
            description = I18n.get("tooltip.eplus.missing", rl.getNamespace(), key);
        }
        return description;
    }

    private String getTranslationKey(Enchantment enchant) {
        if (enchant != null) {
            Registry<Enchantment> reg = this.logic.getWorld().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Identifier rl = reg.getKey(enchant);
            if (rl != null)
                return String.format("enchantment.%s.%s.desc", rl.getNamespace(), rl.getPath());
        }
        return "NULL";
    }
}
