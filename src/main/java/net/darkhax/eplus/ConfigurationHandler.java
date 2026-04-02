package net.darkhax.eplus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.darkhax.eplus.api.Blacklist;
import net.darkhax.eplus.util.RegistryUtils;
import net.darkhax.eplus.util.StackUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;

public final class ConfigurationHandler {

    public static File configFile;
    private static Properties props = new Properties();

    public static float costFactor = 1.5f;
    public static float treasureFactor = 4f;
    public static float curseFactor = 3f;
    public static int baseCost = 45;

    public static float floatingBookBonus = 1f;

    public static void initConfig(File file) {
        configFile = file;
        if (configFile.exists()) {
            try (FileInputStream in = new FileInputStream(configFile)) {
                props.load(in);
                baseCost = Integer.parseInt(props.getProperty("baseCost", "45"));
                costFactor = Float.parseFloat(props.getProperty("costFactor", "1.5"));
                treasureFactor = Float.parseFloat(props.getProperty("treasureFactor", "4"));
                curseFactor = Float.parseFloat(props.getProperty("curseFactor", "3"));
                floatingBookBonus = Float.parseFloat(props.getProperty("floatingBookPower", "1"));
            } catch (IOException e) {
                // use defaults
            }
        }
        save();
    }

    private static void save() {
        props.setProperty("baseCost", String.valueOf(baseCost));
        props.setProperty("costFactor", String.valueOf(costFactor));
        props.setProperty("treasureFactor", String.valueOf(treasureFactor));
        props.setProperty("curseFactor", String.valueOf(curseFactor));
        props.setProperty("floatingBookPower", String.valueOf(floatingBookBonus));
        if (!props.containsKey("blacklistedItems")) props.setProperty("blacklistedItems", "");
        if (!props.containsKey("blacklistedEnchantments")) props.setProperty("blacklistedEnchantments", "");
        try (FileOutputStream out = new FileOutputStream(configFile)) {
            props.store(out, "Enchanting Plus config");
        } catch (IOException ignored) {}
    }

    public static void buildBlacklist() {
        String itemList = props.getProperty("blacklistedItems", "");
        if (!itemList.isEmpty()) {
            for (String itemString : itemList.split(",")) {
                itemString = itemString.trim();
                if (itemString.isEmpty()) continue;
                ItemStack stack = StackUtils.createStackFromString(itemString);
                if (!stack.isEmpty()) Blacklist.blacklist(stack);
            }
        }
        String enchList = props.getProperty("blacklistedEnchantments", "");
        if (!enchList.isEmpty()) {
            for (String enchString : enchList.split(",")) {
                enchString = enchString.trim();
                if (enchString.isEmpty()) continue;
                Enchantment ench = RegistryUtils.getEnchantment(enchString);
                if (ench != null) Blacklist.blacklist(ench);
            }
        }
    }
}
