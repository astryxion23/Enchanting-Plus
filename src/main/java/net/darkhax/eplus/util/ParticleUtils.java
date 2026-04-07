package net.darkhax.eplus.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;

public final class ParticleUtils {

    public static void spawnParticleRing(Level world, ParticleOptions particle, double x, double y, double z, double velocityX, double velocityY, double velocityZ, double step) {
        for (double degree = 0.0; degree <= 2 * Math.PI; degree += step) {
            if (world.isClientSide()) {
                world.addParticle(particle, x + Math.cos(degree), y, z + Math.sin(degree), velocityX, velocityY, velocityZ);
            }
        }
    }
}
