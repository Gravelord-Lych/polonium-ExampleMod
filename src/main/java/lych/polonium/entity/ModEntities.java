package lych.polonium.entity;

import lych.polonium.Polonium;
import lych.polonium.entity.monster.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Polonium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Polonium.MOD_ID);
    public static final RegistryObject<EntityType<DispenserZombie>> DISPENSER_ZOMBIE = ENTITIES.register(ModEntityNames.DISPENSER_ZOMBIE,
            () -> EntityType.Builder.of(DispenserZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(ModEntityNames.DISPENSER_ZOMBIE));
    public static final RegistryObject<EntityType<Ghoul>> GHOUL = ENTITIES.register(ModEntityNames.GHOUL,
            () -> EntityType.Builder.of(Ghoul::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(ModEntityNames.GHOUL));
    public static final RegistryObject<EntityType<Minion>> MINION = ENTITIES.register(ModEntityNames.MINION,
            () -> EntityType.Builder.of(Minion::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(ModEntityNames.MINION));
    public static final RegistryObject<EntityType<Necromancer>> NECROMANCER = ENTITIES.register(ModEntityNames.NECROMANCER,
            () -> EntityType.Builder.of(Necromancer::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(ModEntityNames.NECROMANCER));
    public static final RegistryObject<EntityType<ReinforcedZombie>> REINFORCED_ZOMBIE = ENTITIES.register(ModEntityNames.REINFORCED_ZOMBIE,
            () -> EntityType.Builder.of(ReinforcedZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(ModEntityNames.REINFORCED_ZOMBIE));
    public static final RegistryObject<EntityType<SkeletonWizard>> SKELETON_WIZARD = ENTITIES.register(ModEntityNames.SKELETON_WIZARD,
            () -> EntityType.Builder.of(SkeletonWizard::new, MobCategory.MONSTER).sized(0.6F, 1.99F).clientTrackingRange(8).build(ModEntityNames.SKELETON_WIZARD));

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(DISPENSER_ZOMBIE.get(), DispenserZombie.createAttributes().build());
        event.put(GHOUL.get(), Ghoul.createAttributes().build());
        event.put(MINION.get(), Zombie.createAttributes().build());
        event.put(NECROMANCER.get(), Necromancer.createAttributes().build());
        event.put(REINFORCED_ZOMBIE.get(), ReinforcedZombie.createAttributes().build());
        event.put(SKELETON_WIZARD.get(), SkeletonWizard.createAttributes().build());
    }

    private ModEntities() {}
}
