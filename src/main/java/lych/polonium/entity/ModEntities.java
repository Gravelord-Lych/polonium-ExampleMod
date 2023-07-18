package lych.polonium.entity;

import lych.polonium.Polonium;
import lych.polonium.entity.monster.ReinforcedZombie;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Polonium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Polonium.MOD_ID);
    public static final RegistryObject<EntityType<ReinforcedZombie>> REINFORCED_ZOMBIE = ENTITIES.register(ModEntityNames.REINFORCED_ZOMBIE,
            () -> EntityType.Builder.of(ReinforcedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(ModEntityNames.REINFORCED_ZOMBIE));

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(REINFORCED_ZOMBIE.get(), ReinforcedZombie.createAttributes().build());
    }

    private ModEntities() {}
}
