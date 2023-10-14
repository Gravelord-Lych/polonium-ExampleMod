package lych.polonium.entity;

import lych.polonium.Polonium;
import lych.polonium.entity.monster.Ghoul;
import lych.polonium.entity.monster.ReinforcedZombie;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Ghoul) {
            DamageSource source = event.getSource();
            if (source.getEntity() instanceof Player playerKiller) {
                playerKiller.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 10, 1));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Ghoul ghoul && ghoul.isHighHealth()) {
            event.setAmount(event.getAmount() * 1.2f);
        }
        if (event.getEntity() instanceof Ghoul ghoul && !ghoul.isHighHealth()) {
            event.setAmount(event.getAmount() * 0.8f);
        }
    }
}
