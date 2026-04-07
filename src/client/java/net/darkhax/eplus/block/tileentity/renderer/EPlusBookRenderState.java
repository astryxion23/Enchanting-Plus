package net.darkhax.eplus.block.tileentity.renderer;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;

public final class EPlusBookRenderState extends BlockEntityRenderState {
    public float heightOffset;
    public float flip;
    public float open;
    public float time;
    public float yRot;
    public Identifier bookTexture;
    public final ItemStackRenderState floatingItemState = new ItemStackRenderState();
    public boolean hasFloatingItem;
    public float floatingItemOpenness;
}
