package net.darkhax.eplus.util;

import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

public final class ParticleUtils {

    public static void spawnParticleRing(World world, net.minecraft.particles.IParticleData particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        for (double degree = 0.0; degree <= 2 * Math.PI; degree += step) {
            if (world.isClientSide) {
                world.addParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
            }
        }
    }
}
