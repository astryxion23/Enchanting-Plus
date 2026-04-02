package net.darkhax.eplus.util;

import java.util.Random;

public final class MathsUtils {

    private static final Random RAND = new Random();

    public static boolean tryPercentage(double percentage) {
        return RAND.nextDouble() < percentage;
    }
}
