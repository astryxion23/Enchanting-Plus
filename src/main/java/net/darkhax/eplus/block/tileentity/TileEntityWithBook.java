package net.darkhax.eplus.block.tileentity;

import java.util.Random;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.MathHelper;

public abstract class TileEntityWithBook extends TileEntity implements ITickableTileEntity {

    private static final Random rand = new Random();

    public int tickCount;
    public float pageFlip;
    public float pageFlipPrev;
    public float flipRandom;
    public float flipTurn;
    public float bookSpread;
    public float bookSpreadPrev;
    public float bookRotation;
    public float bookRotationPrev;
    public float offset;

    public TileEntityWithBook(TileEntityType<?> type) {
        super(type);
    }

    public boolean isOpen() {
        return this.bookSpread >= 1;
    }

    @Override
    public void tick() {
        if (level == null) return;
        if (this.bookSpreadPrev != this.bookSpread && (this.bookSpread == 0f || this.bookSpread == 1f))
            level.updateNeighborsAt(worldPosition, level.getBlockState(worldPosition).getBlock());
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        PlayerEntity entityplayer = level.getNearestPlayer(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 3.0D, false);
        if (entityplayer != null) {
            double d0 = entityplayer.getX() - (worldPosition.getX() + 0.5);
            double d1 = entityplayer.getZ() - (worldPosition.getZ() + 0.5);
            this.offset = (float) MathHelper.atan2(d0, d1); // Minecraft yaw convention: atan2(dx, dz)
            this.bookSpread += 0.1F;
            if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
                float f1 = this.flipRandom;
                while (true) {
                    this.flipRandom += rand.nextInt(4) - rand.nextInt(4);
                    if (f1 != this.flipRandom) break;
                }
            }
        } else {
            this.offset += 0.02F;
            this.bookSpread -= 0.1F;
        }
        while (this.bookRotation >= (float) Math.PI) this.bookRotation -= (float) Math.PI * 2F;
        while (this.bookRotation < -(float) Math.PI) this.bookRotation += (float) Math.PI * 2F;
        while (this.offset >= (float) Math.PI) this.offset -= (float) Math.PI * 2F;
        while (this.offset < -(float) Math.PI) this.offset += (float) Math.PI * 2F;
        float f2 = this.offset - this.bookRotation;
        while (f2 >= (float) Math.PI) f2 -= (float) Math.PI * 2F;
        while (f2 < -(float) Math.PI) f2 += (float) Math.PI * 2F;
        this.bookRotation += f2 * 0.4F;
        this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f = (this.flipRandom - this.pageFlip) * 0.4F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.flipTurn += (f - this.flipTurn) * 0.9F;
        this.pageFlip += this.flipTurn;
    }
}
