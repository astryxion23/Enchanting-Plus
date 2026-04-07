package net.darkhax.eplus.block.tileentity;

import java.awt.Color;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putFloat("Height", this.height);
        output.putInt("Color", this.color);
        output.putInt("Variant", this.variant);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.height = input.getFloatOr("Height", 0f);
        this.color = input.getIntOr("Color", 0);
        this.variant = input.getIntOr("Variant", 0);
    }
}
