package net.darkhax.eplus.inventory;

import java.util.ArrayList;
import java.util.List;

import net.darkhax.eplus.block.tileentity.EnchantmentLogicController;
import net.darkhax.eplus.util.EntityUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
public class ContainerAdvancedTable extends AbstractContainerMenu {

    public final EnchantmentLogicController logic;
    private final Inventory playerInv;

    public ContainerAdvancedTable(int id, Inventory invPlayer, EnchantmentLogicController logic) {
        super(ModContainers.ADVANCED_TABLE, id);
        this.logic = logic;
        this.playerInv = invPlayer;

        this.addSlot(new SlotEnchant(logic, 0, 37, 17));

        for (int x = 0; x < 9; x++)
            this.addSlot(new Slot(invPlayer, x, 43 + 18 * x, 149));

        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 9; x++)
                this.addSlot(new Slot(invPlayer, x + y * 9 + 9, 43 + 18 * x, 91 + y * 18));

        // Armor slots top to bottom: helmet, chestplate, leggings, boots
        for (int y = 0; y < 4; y++) {
            int invIndex = 39 - y;  // 39=head, 38=chest, 37=legs, 36=feet
            this.addSlot(new SlotArmor(invPlayer, EntityUtils.getEquipmentSlot(3 - y), invIndex, 7, 24 + y * 19));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player entityPlayer, int idx) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot clickSlot = this.slots.get(idx);
        if (clickSlot != null && clickSlot.hasItem()) {
            itemStack = clickSlot.getItem().copy();
            if (itemStack.isEmpty()) return ItemStack.EMPTY;
            List<Slot> selectedSlots = new ArrayList<>();
            boolean fromPlayer = (idx >= 1 && idx <= 40); // slot 0 = enchant, 1-40 = player (hotbar, main, armor)
            if (fromPlayer) {
                for (Slot advSlot : this.slots)
                    if (advSlot != clickSlot && advSlot.mayPlace(itemStack)) selectedSlots.add(advSlot);
            } else {
                for (int i = 1; i <= 40; i++) {
                    Slot advSlot = this.slots.get(i);
                    if (advSlot.mayPlace(itemStack)) selectedSlots.add(advSlot);
                }
            }
            for (Slot slot : selectedSlots) {
                if (!slot.mayPlace(itemStack) || itemStack.isEmpty()) continue;
                if (slot.hasItem()) {
                    ItemStack stack = slot.getItem();
                    if (ItemStack.isSameItemSameComponents(itemStack, stack)) {
                        int maxSize = Math.min(stack.getMaxStackSize(), slot.getMaxStackSize());
                        int placeAble = maxSize - stack.getCount();
                        if (itemStack.getCount() < placeAble) placeAble = itemStack.getCount();
                        stack.grow(placeAble);
                        itemStack.shrink(placeAble);
                        if (itemStack.getCount() <= 0) {
                            clickSlot.set(ItemStack.EMPTY);
                            slot.setChanged();
                            this.broadcastChanges();
                            return ItemStack.EMPTY;
                        }
                        this.broadcastChanges();
                    }
                } else {
                    int maxSize = Math.min(itemStack.getMaxStackSize(), slot.getMaxStackSize());
                    ItemStack tmp = itemStack.copy();
                    if (tmp.getCount() > maxSize) tmp.setCount(maxSize);
                    itemStack.shrink(tmp.getCount());
                    slot.set(tmp);
                    if (itemStack.getCount() <= 0) {
                        clickSlot.set(ItemStack.EMPTY);
                        slot.setChanged();
                        this.broadcastChanges();
                        return ItemStack.EMPTY;
                    }
                    this.broadcastChanges();
                }
            }
            clickSlot.set(itemStack.isEmpty() ? ItemStack.EMPTY : itemStack.copy());
        }
        this.broadcastChanges();
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return playerIn.distanceToSqr(this.logic.getPos().getX() + 0.5, this.logic.getPos().getY() + 0.5, this.logic.getPos().getZ() + 0.5) <= 64.0D && playerIn.isAlive();
    }
}
