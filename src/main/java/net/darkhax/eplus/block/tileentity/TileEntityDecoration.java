package net.darkhax.eplus.block.tileentity;

import java.awt.Color;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityDecoration extends TileEntityWithBook {

    public float height = 0f;
    public int color = Color.WHITE.getRGB();
    public int variant;

    public TileEntityDecoration(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void decreaseHeight() {
        this.height -= 0.05f;
        if (this.height < -0.35f) this.height = -0.35f;
    }

    public void increaseHeight() {
        this.height += 0.05f;
        if (this.height > 0.35f) this.height = 0.35f;
    }

    @Override
    protected void saveAdditional(CompoundTag dataTag) {
        super.saveAdditional(dataTag);
        dataTag.putFloat("Height", this.height);
        dataTag.putInt("Color", this.color);
        dataTag.putInt("Variant", this.variant);
    }

    @Override
    public void load(CompoundTag dataTag) {
        super.load(dataTag);
        this.height = dataTag.getFloat("Height");
        this.color = dataTag.getInt("Color");
        this.variant = dataTag.getInt("Variant");
    }
}
