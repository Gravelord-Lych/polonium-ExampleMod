package lych.polonium.entity.monster;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class DispenserZombie extends Zombie implements RangedAttackMob {
    public DispenserZombie(EntityType<? extends Zombie> type, Level level) {
            super(type, level);
        }

        public static AttributeSupplier.Builder createAttributes() {
            return Zombie.createAttributes()
                    .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0);
        }

        @Override
        protected void randomizeReinforcementsChance() {
            Objects.requireNonNull(getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE)).setBaseValue(0);
        }

        @Override
        protected void registerGoals() {
            goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8));
            goalSelector.addGoal(8, new RandomLookAroundGoal(this));
            addBehaviourGoals();
        }

        @Override
        protected void addBehaviourGoals() {
            goalSelector.addGoal(2, new RangedAttackGoal(this, 0.85, 40, 10));
            goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1, true, 4, this::canBreakDoors));
            goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1));
            targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));
            targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
            targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
            targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        }

        @Override
        public void performRangedAttack(LivingEntity target, float power) {
            Projectile projectile = findProjectileFor(target);
            double targetY = target.getEyeY() - (double) 1.1F;
            double dx = target.getX() - this.getX();
            double dy = targetY - projectile.getY();
            double dz = target.getZ() - this.getZ();
            double yModifier = Math.sqrt(dx * dx + dz * dz) * (double) 0.2F;
            projectile.shoot(dx, yModifier + dy, dz, 1.6F, 12.0F);
            playSound(SoundEvents.DISPENSER_DISPENSE, 1, 0.4F / (getRandom().nextFloat() * 0.4F + 0.8F));
            level().addFreshEntity(projectile);
        }

    @Override
    public void tick() {
        super.tick();
        if (canHealSelf()) {
            if (level().isClientSide()) {
                maybeAddParticles();
            } else {
                healSelf();
            }
        }
    }

    private boolean canHealSelf() {
        BlockPos pos = blockPosition();
        return level().hasNeighborSignal(pos) || level().hasSignal(pos.below(), Direction.DOWN);
    }

    private void maybeAddParticles() {
        if (random.nextInt(10) == 0) {
            level().addParticle(DustParticleOptions.REDSTONE, getRandomX(1), getRandomY() + 0.5, getRandomZ(1), 0, 0, 0);
        }
    }

    private void healSelf() {
        if (tickCount % 20 == 0) {
            heal(1);
        }
    }

    private Projectile findProjectileFor(LivingEntity target) {
        if (target instanceof Enemy) {
            return new Snowball(level(), this);
        }
        return new Arrow(level(), this);
    }

    @Override
    protected ItemStack getSkull() {
        return new ItemStack(Items.DISPENSER);
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean baby) {}
}
