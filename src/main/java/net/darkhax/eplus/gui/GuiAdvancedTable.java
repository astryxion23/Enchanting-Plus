package net.darkhax.eplus.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.darkhax.eplus.EnchLogic;
import net.darkhax.eplus.api.event.InfoBoxEvent;
import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.inventory.ContainerAdvancedTable;
import net.darkhax.eplus.network.payload.EnchantPayload;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.util.Mth;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;

public class GuiAdvancedTable extends AbstractContainerScreen<ContainerAdvancedTable> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("eplus", "textures/gui/enchant.png");
    private static final Random RAND = new Random();

    private Button enchantButton;
    private ItemStack enchantButtonIcon = ItemStack.EMPTY;
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

    public GuiAdvancedTable(ContainerAdvancedTable container, Inventory inv, Component title) {
        super(container, inv, title, 235, 182);
        this.logic = container.logic;
        this.currentTip = RAND.nextInt(this.tips.length);
    }

    @Override
    protected void init() {
        super.init();
        this.isSliding = false;
        this.scrollbar = new GuiButtonScroller(this, this.leftPos + 206, this.topPos + 16, 12, 15);
        if (EnchLogic.isWikedNight(this.logic.getWorld())) {
            ItemStack bone = new ItemStack(Items.BONE);
            Registry<Enchantment> reg = this.logic.getWorld().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath("minecraft", "projectile_protection"));
            Holder<Enchantment> h = reg.getOrThrow(key);
            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
            mut.set(h, 1);
            EnchantmentHelper.setEnchantments(bone, mut.toImmutable());
            this.enchantButtonIcon = bone;
        } else {
            this.enchantButtonIcon = new ItemStack(Items.ENCHANTED_BOOK);
        }
        this.enchantButton = Button.builder(Component.empty(), btn -> {
            if (this.canClientAfford()) {
                ClientPacketDistributor.sendToServer(new EnchantPayload());
                this.logic.enchantItem();
            }
        }).bounds(this.leftPos + 32, this.topPos + 38, 26, 20).build();
        this.addRenderableWidget(this.enchantButton);
        this.addRenderableWidget(this.scrollbar);
    }

    /** Linear 1–5 XP cost; treasure enchantments always 4. */
    public static int xpCost(Enchantment ench, int level, RegistryAccess registryAccess) {
        if (EnchLogic.isTreasureOnlyEnchantment(registryAccess, ench)) {
            return 4;
        }
        return Math.max(1, Math.min(level, 5));
    }

    public int calculateTotalCost() {
        ItemStack stack = this.menu.getSlot(0).getItem();
        if (stack.isEmpty()) {
            return 0;
        }

        RegistryAccess regAccess = this.logic.getWorld().registryAccess();
        Registry<Enchantment> reg = regAccess.lookupOrThrow(Registries.ENCHANTMENT);
        int total = 0;

        for (Entry<Enchantment, Integer> e : this.logic.getCurrentEnchantments().entrySet()) {
            Enchantment enchant = e.getKey();
            int selectedLevel = e.getValue();

            if (selectedLevel <= 0) continue;

            Holder<Enchantment> holder = reg.wrapAsHolder(enchant);
            int existingLevel = stack.getEnchantmentLevel(holder);

            if (selectedLevel > existingLevel) {
                int levelDifference = selectedLevel - existingLevel;
                total += xpCost(enchant, levelDifference, regAccess);
            }
        }

        return Math.max(total, 0);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        this.totalCost = calculateTotalCost();
        if (this.enchantButton != null && this.minecraft != null && this.minecraft.player != null)
            this.enchantButton.active = this.minecraft.player.isCreative() || this.minecraft.player.experienceLevel >= this.totalCost;
        this.updateLabels();
        this.populateEnchantmentSliders();
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, 32, 5, 0xFF404040, false);
        int cost = calculateTotalCost();
        int playerXP = this.minecraft != null && this.minecraft.player != null ? this.minecraft.player.experienceLevel : 0;
        boolean creative = this.minecraft != null && this.minecraft.player != null && this.minecraft.player.isCreative();
        int color;
        if (cost == 0) {
            color = 0xFFAAAAAA;
        } else if (creative) {
            color = 0xFF80FF20;
        } else if (playerXP >= cost) {
            color = 0xFF80FF20;
        } else {
            color = 0xFFFF4040;
        }
        Component xpText = Component.literal("XP: " + cost);
        int textWidth = this.font.width(xpText);
        int bookCenterX = 44;
        int x = bookCenterX - (textWidth / 2);
        int y = 62;
        graphics.text(this.font, xpText, x, y, color, false);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        if (!this.enchantButtonIcon.isEmpty() && this.enchantButton != null)
            graphics.item(this.enchantButtonIcon, this.enchantButton.getX() + 5, this.enchantButton.getY() + 2);
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
        Registry<Enchantment> reg = this.logic.getWorld().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        for (GuiEnchantmentLabel label : this.enchantmentListAll) {
            label.setLocked(false);
            Enchantment enchantment = label.getEnchantment();
            for (Entry<Enchantment, Integer> data : this.logic.getCurrentEnchantments().entrySet()) {
                boolean incompatible = false;
                if (enchantment != data.getKey() && data.getValue() > 0) {
                    Holder<Enchantment> h1 = reg.wrapAsHolder(enchantment);
                    Holder<Enchantment> h2 = reg.wrapAsHolder(data.getKey());
                    incompatible = !Enchantment.areCompatible(h1, h2);
                }
                boolean isOverLeveled = enchantment == data.getKey() && data.getValue() > enchantment.getMaxLevel();
                if (isOverLeveled || incompatible) {
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
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        for (GuiEnchantmentLabel label : this.enchantmentList)
            label.draw(graphics, this.font);
    }

    @Override
    public boolean mouseScrolled(double mx, double my, double deltaX, double deltaY) {
        int total = this.enchantmentListAll.size();
        int visible = 4;
        int maxOffset = Math.max(0, total - visible);

        if (deltaY > 0) {
            this.listOffset--;
        } else if (deltaY < 0) {
            this.listOffset++;
        }

        this.listOffset = Mth.clamp(this.listOffset, 0, maxOffset);

        if (maxOffset > 0) {
            float percent = (float) this.listOffset / maxOffset;
            this.scrollbar.sliderY = 1 + Math.round(percent * 55f);
        }

        this.updateLabels();
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double mx = event.x();
        double my = event.y();
        int mouseX = (int) mx;
        int mouseY = (int) my;
        this.selected = this.getLabelUnderMouse(mouseX, mouseY);
        if (this.selected != null && !this.selected.isLocked() && this.selected.isVisible())
            this.selected.setDragging(true);
        else this.selected = null;
        if (this.enchantmentListAll.size() > 4 && mouseX > this.leftPos + 206 && mouseX < this.leftPos + 218
                && mouseY > this.topPos + 16 + this.scrollbar.sliderY && mouseY < this.topPos + 31 + this.scrollbar.sliderY)
            this.isSliding = true;
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (this.selected != null) {
            this.selected.setDragging(false);
            this.selected = null;
            this.lockLabels();
        }
        this.isSliding = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        double mx = event.x();
        double my = event.y();
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
        return super.mouseDragged(event, dx, dy);
    }

    private boolean isMouseOverInfoRegion(int mouseX, int mouseY) {
        return mouseX >= this.leftPos && mouseX <= this.leftPos + 18 && mouseY >= this.topPos && mouseY <= this.topPos + 18;
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
        super.extractTooltip(graphics, x, y);
        if (this.isMouseOverInfoRegion(x, y)) {
            List<String> info = this.getInfoBox();
            if (!info.isEmpty()) {
                List<Component> comp = new ArrayList<>();
                for (String s : info) comp.add(Component.literal(s));
                graphics.setComponentTooltipForNextFrame(this.font, comp, x, y);
            }
        }

        if (this.enchantButton != null && this.enchantButton.isHovered()) {
            List<Component> text = new ArrayList<>();
            if (!this.canClientAfford()) text.add(Component.translatable("gui.eplus.tooltip.tooexpensive"));
            else if (this.logic.getCost() == 0) text.add(Component.translatable("gui.eplus.tooltip.nochange"));
            else text.add(Component.translatable("gui.eplus.tooltip.enchant"));
            graphics.setComponentTooltipForNextFrame(this.font, text, x, y);
        }

        if (this.minecraft != null && this.minecraft.hasShiftDown()) {
            GuiEnchantmentLabel label = this.getLabelUnderMouse(x, y);
            if (label != null && label.isVisible())
                graphics.setComponentTooltipForNextFrame(this.font, Collections.singletonList(Component.literal(label.getDescription())), x, y);
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
                info.add(ChatFormatting.RED + I18n.get("gui.eplus.info.tooexpensive"));
            }
        }
        info.add(" ");
        String shiftKey = minecraft.options.keyShift.getKey().getDisplayName().getString();
        info.add(ChatFormatting.YELLOW + I18n.get("eplus.info.tip.prefix") + ChatFormatting.RESET + I18n.get("eplus.info.tip." + this.tips[this.currentTip], shiftKey));
        NeoForge.EVENT_BUS.post(new InfoBoxEvent(this, info));
        return info;
    }

    public boolean canClientAfford() {
        return this.logic.getCost() <= EnchLogic.getExperience(this.logic.getPlayer()) || (minecraft.player != null && minecraft.player.isCreative());
    }
}
