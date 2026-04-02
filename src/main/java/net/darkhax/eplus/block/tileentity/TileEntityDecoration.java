package net.darkhax.eplus.block.tileentity;

import java.awt.Color;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityDecoration extends TileEntityWithBook {

    public float height = 0f;
    public int color = Color.WHITE.getRGB();
    public int variant;

    public TileEntityDecoration(TileEntityType<?> type) {
        super(type);
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
    public CompoundNBT save(CompoundNBT dataTag) {
        super.save(dataTag);
        dataTag.putFloat("Height", this.height);
        dataTag.putInt("Color", this.color);
        dataTag.putInt("Variant", this.variant);
        return dataTag;
    }

    @Override
    public void load(net.minecraft.block.BlockState state, CompoundNBT dataTag) {
        super.load(state, dataTag);
        this.height = dataTag.getFloat("Height");
        this.color = dataTag.getInt("Color");
        this.variant = dataTag.getInt("Variant");
    }
}
