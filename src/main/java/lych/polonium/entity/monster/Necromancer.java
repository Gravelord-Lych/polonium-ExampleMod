package lych.polonium.entity.monster;

import lych.polonium.entity.monster.ai.NecromancerShootGoal;
import lych.polonium.entity.monster.ai.NecromancerSummonGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Necromancer extends Monster {
    public static final int MAX_SUMMON_COOLDOWN = 600;
    private static final String SPELL_COLOR_TAG = "SpellColor";
    private static final EntityDataAccessor<Integer> SPELL_COLOR = SynchedEntityData.defineId(Necromancer.class, EntityDataSerializers.INT);
    private static final String SUMMON_COOLDOWN_TAG = "SummonCooldown";
    private int summonCooldown = 0;

    public Necromancer(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(SPELL_COLOR, -1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.MOVEMENT_SPEED, 0.27)
                .add(Attributes.FOLLOW_RANGE, 24);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new NecromancerSummonGoal(this));
        goalSelector.addGoal(3, new NecromancerShootGoal(this));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (getSummonCooldown() > 0) {
            setSummonCooldown(getSummonCooldown() - 1);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && isCastingSpell()) {
            int spellColor = getSpellColor();
            int r0 = spellColor / 65536;
            int g0 = spellColor / 256 % 256;
            int b0 = spellColor % 256;
            double r = (double) r0 / 255.0D;
            double g = (double) g0 / 255.0D;
            double b = (double) b0 / 255.0D;
            float len = yBodyRot * ((float) Math.PI / 180F) + Mth.cos((float) tickCount * 0.6662F) * 0.25F;
            float dx = Mth.cos(len);
            float dz = Mth.sin(len);
            if (isLeftHanded()) {
                level().addParticle(ParticleTypes.ENTITY_EFFECT, getX() + (double) dx * 0.6D, getY() + 1.8D, getZ() + (double) dz * 0.6D, r, g, b);
            } else {
                level().addParticle(ParticleTypes.ENTITY_EFFECT, getX() - (double) dx * 0.6D, getY() + 1.8D, getZ() - (double) dz * 0.6D, r, g, b);
            }
        }
    }

    public int getSummonCooldown() {
        return summonCooldown;
    }

    public void setSummonCooldown(int summonCooldown) {
        this.summonCooldown = summonCooldown;
    }

    public boolean isCastingSpell() {
        return entityData.get(SPELL_COLOR) > 0;
    }

    public int getSpellColor() {
        return entityData.get(SPELL_COLOR);
    }

    public void setSpellColor(int color) {
        entityData.set(SPELL_COLOR, color);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt(SUMMON_COOLDOWN_TAG, getSummonCooldown());
        tag.putInt(SPELL_COLOR_TAG, getSpellColor());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSummonCooldown(tag.getInt(SUMMON_COOLDOWN_TAG));
        if (tag.contains(SPELL_COLOR_TAG)) {
            setSpellColor(tag.getInt(SPELL_COLOR_TAG));
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return super.getHurtSound(source);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        super.playStepSound(pos, state);
    }
}

