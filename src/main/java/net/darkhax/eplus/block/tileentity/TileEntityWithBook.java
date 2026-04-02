package net.darkhax.eplus.block.tileentity;

import java.util.Random;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;

public abstract class TileEntityWithBook extends BlockEntity implements net.minecraftforge.common.extensions.IForgeBlockEntity {

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

    public TileEntityWithBook(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isOpen() {
        return this.bookSpread >= 1;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityWithBook te) {
        te.tickServerOrClient();
    }

    private void tickServerOrClient() {
        if (level == null) return;
        if (this.bookSpreadPrev != this.bookSpread && (this.bookSpread == 0f || this.bookSpread == 1f))
            level.updateNeighborsAt(worldPosition, level.getBlockState(worldPosition).getBlock());
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        Player entityplayer = level.getNearestPlayer(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 3.0D, false);
        if (entityplayer != null) {
            double d0 = entityplayer.getX() - (worldPosition.getX() + 0.5);
            double d1 = entityplayer.getZ() - (worldPosition.getZ() + 0.5);
            this.offset = (float) Mth.atan2(d0, d1); // Minecraft yaw convention: atan2(dx, dz)
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
        this.bookSpread = Mth.clamp(this.bookSpread, 0.0F, 1.0F);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f = (this.flipRandom - this.pageFlip) * 0.4F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        this.flipTurn += (f - this.flipTurn) * 0.9F;
        this.pageFlip += this.flipTurn;
    }
}

