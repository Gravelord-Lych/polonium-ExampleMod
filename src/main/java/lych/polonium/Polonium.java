package lych.polonium;

import lych.polonium.entity.ModEntities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Polonium.MOD_ID)
public class Polonium {
    public static final String MOD_ID = "polonium";

    public Polonium() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITIES.register(bus);
    }
}
