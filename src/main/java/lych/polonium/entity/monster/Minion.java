package lych.polonium.entity.monster;

import lych.polonium.entity.monster.ai.MinionCopyOwnerTargetGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class Minion extends Zombie {
    private static final String OWNER_UUID_TAG = "OwnerUUID";
    private UUID ownerUUID;

    public Minion(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Override
    protected void randomizeReinforcementsChance() {
        Objects.requireNonNull(getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE)).setBaseValue(0);
    }

    @Override
    protected void addBehaviourGoals() {
        goalSelector.addGoal(2, new ZombieAttackGoal(this, 1, false));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1));
        targetSelector.addGoal(1, new HurtByTargetGoal(this, Necromancer.class));
        targetSelector.addGoal(2, new MinionCopyOwnerTargetGoal(this));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected boolean isSunSensitive() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (ownerUUID != null) {
            tag.putUUID(OWNER_UUID_TAG, ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID(OWNER_UUID_TAG)) {
            ownerUUID = tag.getUUID(OWNER_UUID_TAG);
        }
    }

    @Nullable
    public Necromancer getOwner() {
        if (level().isClientSide()) {
            return null;
        }
        Entity entity = ((ServerLevel) level()).getEntity(ownerUUID);
        if (entity instanceof Necromancer necromancer) {
            return necromancer;
        }
        return null;
    }

    public void setOwner(@Nullable Necromancer owner) {
        if (owner != null) {
            ownerUUID = owner.getUUID();
        } else {
            ownerUUID = null;
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.HUSK_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }
}
