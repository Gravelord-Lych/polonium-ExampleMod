package lych.polonium.entity;

import lych.polonium.Polonium;
import lych.polonium.entity.monster.ReinforcedZombie;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Polonium.MOD_ID)
public final class EntityEventListener {
    @SubscribeEvent
    public static void onZombieSummonAid(ZombieEvent.SummonAidEvent event) {
        if (event.getEntity() instanceof ReinforcedZombie reinforcedZombie) {
            if (reinforcedZombie.getAidsRemaining() <= 0) {
                event.setResult(Event.Result.DENY);
                return;
            }
            ReinforcedZombie aid = ModEntities.REINFORCED_ZOMBIE.get().create(event.getLevel());
            if (aid != null) {
                aid.setAidsRemaining(0);
                event.setResult(Event.Result.ALLOW);
                event.setCustomSummonedAid(aid);
                reinforcedZombie.setAidsRemaining(reinforcedZombie.getAidsRemaining() - 1);
            }
        }
    }
}
