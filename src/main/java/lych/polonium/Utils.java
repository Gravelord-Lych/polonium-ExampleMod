package lych.polonium;

import net.minecraft.resources.ResourceLocation;

public final class Utils {
    private Utils() {}

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(Polonium.MOD_ID, name);
    }
}
