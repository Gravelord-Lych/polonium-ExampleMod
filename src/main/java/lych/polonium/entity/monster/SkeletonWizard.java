package lych.polonium.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class SkeletonWizard extends AbstractSkeleton {
    private static final EntityDataAccessor<Boolean> REINFORCED = SynchedEntityData.defineId(SkeletonWizard.class, EntityDataSerializers.BOOLEAN);
    private static final String DOES_PHYSICAL_DAMAGE_TAG = "DoesPhysicalDamage";
    private static final String REINFORCED_TAG = "Reinforced";
    private static final UUID REINFORCED_BONUS_UUID = UUID.fromString("0153B2B3-CC49-470F-AD1C-B8D31EFAD17D");
    private static final AttributeModifier REINFORCED_BONUS = new AttributeModifier(REINFORCED_BONUS_UUID, "Reinforced bonus", 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private final List<PotionSelector> potionSelectors = List.of(
            new SlownessPotionSelector(),
            new PoisonPotionSelector(),
            new WeaknessPotionSelector(),
            new HealingPotionSelector(),
            new HarmingPotionSelector()
    );
    private RangedAttackGoal potionAttackGoal;
    private boolean doesPhysicalDamage;

    public SkeletonWizard(EntityType<? extends AbstractSkeleton> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ATTACK_DAMAGE, 3);
    }

    private RangedAttackGoal getPotionAttackGoal() {
        if (potionAttackGoal == null) {
            potionAttackGoal = new RangedAttackGoal(this, 1, 60, 10);
        }
        return potionAttackGoal;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(REINFORCED, false);
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean(DOES_PHYSICAL_DAMAGE_TAG, doesPhysicalDamage());
        tag.putBoolean(REINFORCED_TAG, isReinforced());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        // Must be placed before super.readAdditionalSaveData(tag) otherwise the reinforced Skeleton Wizard will have
        // incorrect health (min(trueHealth, 100)).
        setReinforced(tag.getBoolean(REINFORCED_TAG), false);
        super.readAdditionalSaveData(tag);
        setDoesPhysicalDamage(tag.getBoolean(DOES_PHYSICAL_DAMAGE_TAG));
        // The "re-reassessment" of thw weapon goal is necessary because Skeleton Wizard's way of attacking may have changed.
        reassessWeaponGoal();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficultyInstance) {}

    private void resetItemsInHands() {
        setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        if (doesPhysicalDamage()) {
            super.performRangedAttack(target, power);
        } else {
            preformPotionAttack(target);
        }
    }

    private void preformPotionAttack(LivingEntity target) {
        Vec3 movement = target.getDeltaMovement();
        double x = target.getX() + movement.x - this.getX();
        double y = target.getEyeY() - 1.1 - this.getY();
        double z = target.getZ() + movement.z - this.getZ();
        double distance = Math.sqrt(x * x + z * z);
        ThrownPotion thrownPotion = findPotion(target);
        thrownPotion.setXRot(thrownPotion.getXRot() + 20.0F);
        thrownPotion.shoot(x, y + distance * 0.2, z, 0.75F, 8);
        if (!isSilent()) {
            // Witch's sound is used here for I'm too lazy to create a custom sound.
            level().playSound(null, getX(), getY(), getZ(), SoundEvents.WITCH_THROW, getSoundSource(), 1.0F, 0.8F + random.nextFloat() * 0.4F);
        }
        level().addFreshEntity(thrownPotion);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (getHealth() <= getMaxHealth() * 0.5F && !doesPhysicalDamage()) {
            setDoesPhysicalDamage(true);
            awardBow();
        }
    }

    private void awardBow() {
        ItemStack bow = new ItemStack(Items.BOW);
        bow.enchant(Enchantments.POWER_ARROWS, isReinforced() ? 5 : 2);
        setItemInHand(InteractionHand.MAIN_HAND, bow);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level().isClientSide()) {
            boolean canBeReinforced = stack.is(Items.BONE) && !isReinforced();
            if (canBeReinforced) {
                spawnAnim();
            }
            return canBeReinforced ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (stack.is(Items.BONE)) {
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (!isReinforced()) {
                setReinforced(true, true);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected float getDamageAfterMagicAbsorb(DamageSource source, float amount) {
        amount = super.getDamageAfterMagicAbsorb(source, amount);
        if (source.getEntity() == this) {
            amount = 0;
        }
        if (source.getEntity() instanceof IronGolem) {
            amount *= 0.5F;
        }
        if (source.is(DamageTypeTags.WITCH_RESISTANT_TO)) {
            amount *= 0.15F;
        }
        return amount;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance instance) {
        MobEffect effect = instance.getEffect();
        if (!super.canBeAffected(instance)) {
            return false;
        }
        return effect.isBeneficial() && isReinforced();
    }

    private ThrownPotion findPotion(LivingEntity target) {
        for (PotionSelector potionSelector : potionSelectors) {
            if (potionSelector.test(target)) {
                return potionSelector.getPotionProjectile();
            }
        }
        throw new IllegalStateException("No valid potion found");
    }

    @Override
    public void reassessWeaponGoal() {
        if (!level().isClientSide()) {
            removeMeleeGoalAndBowGoal();
            goalSelector.removeGoal(getPotionAttackGoal());
            if (doesPhysicalDamage()) {
                super.reassessWeaponGoal();
            } else {
                goalSelector.addGoal(4, getPotionAttackGoal());
            }
        }
    }

    /**
     * <code>bowGoal</code> and <code>meleeGoal</code> are private in {@link AbstractSkeleton},
     * so I use this method to remove them.
     */
    private void removeMeleeGoalAndBowGoal() {
        goalSelector.getAvailableGoals().stream()
                .filter(SkeletonWizard::isMeleeGoalOrBowGoal)
                .filter(WrappedGoal::isRunning)
                .forEach(WrappedGoal::stop);
        goalSelector.getAvailableGoals().removeIf(SkeletonWizard::isMeleeGoalOrBowGoal);
    }

    private static boolean isMeleeGoalOrBowGoal(WrappedGoal wrappedGoal) {
        return wrappedGoal.getGoal() instanceof MeleeAttackGoal || wrappedGoal.getGoal() instanceof RangedBowAttackGoal<?>;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    private boolean shouldThrowLingeringPotion() {
        return random.nextDouble() < (isReinforced() ? 0.3 : 0.2);
    }

    public boolean isReinforced() {
        return entityData.get(REINFORCED);
    }

    public void setReinforced(boolean reinforced, boolean update) {
        entityData.set(REINFORCED, reinforced);
        if (!level().isClientSide()) {
            if (reinforced) {
                Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).addTransientModifier(REINFORCED_BONUS);
                Objects.requireNonNull(getAttribute(Attributes.ARMOR)).addTransientModifier(REINFORCED_BONUS);
                Objects.requireNonNull(getAttribute(Attributes.ATTACK_DAMAGE)).addTransientModifier(REINFORCED_BONUS);
                if (update) {
                    resetItemsInHands();
                    setHealth(getMaxHealth());
                    setDoesPhysicalDamage(false);
                    reassessWeaponGoal();
                }
            } else {
                Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).removeModifier(REINFORCED_BONUS);
                Objects.requireNonNull(getAttribute(Attributes.ARMOR)).removeModifier(REINFORCED_BONUS);
                Objects.requireNonNull(getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(REINFORCED_BONUS);
            }
        }
    }

    public boolean doesPhysicalDamage() {
        return doesPhysicalDamage;
    }

    public void setDoesPhysicalDamage(boolean doesPhysicalDamage) {
        this.doesPhysicalDamage = doesPhysicalDamage;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public void setBaby(boolean baby) {}

    public class SlownessPotionSelector extends PotionSelector {
        public SlownessPotionSelector() {
            super(Potions.SLOWNESS, Potions.STRONG_SLOWNESS);
        }

        @Override
        public boolean test(LivingEntity target) {
            if (target.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                return false;
            }
            return distanceToSqr(target) > 8 * 8 && random.nextBoolean();
        }
    }

    public class PoisonPotionSelector extends PotionSelector {
        public PoisonPotionSelector() {
            super(Potions.POISON, Potions.STRONG_POISON);
        }

        @Override
        public boolean test(LivingEntity target) {
            if (target.getMobType() == MobType.UNDEAD) {
                return false;
            }
            if (target.hasEffect(MobEffects.POISON)) {
                return false;
            }
            return target.getHealth() >= target.getMaxHealth() * 0.5F;
        }
    }

    public class WeaknessPotionSelector extends PotionSelector {
        public WeaknessPotionSelector() {
            super(Potions.WEAKNESS, Potions.LONG_WEAKNESS);
        }

        @Override
        public boolean test(LivingEntity target) {
            if (target.hasEffect(MobEffects.WEAKNESS)) {
                return false;
            }
            if (distanceToSqr(target) >= 3 * 3) {
                return false;
            }
            return target instanceof IronGolem || random.nextDouble() < 0.25;
        }
    }

    public class HealingPotionSelector extends PotionSelector {
        public HealingPotionSelector() {
            super(Potions.HEALING, Potions.STRONG_HEALING);
        }

        @Override
        public boolean test(LivingEntity target) {
            return target.getMobType() == MobType.UNDEAD;
        }
    }

    public class HarmingPotionSelector extends PotionSelector {
        public HarmingPotionSelector() {
            super(Potions.HARMING, Potions.STRONG_HARMING);
        }

        @Override
        public boolean test(LivingEntity target) {
            return true;
        }
    }

    public abstract class PotionSelector implements Predicate<LivingEntity> {
        private final Potion potion;
        private final Potion strongerPotion;

        public PotionSelector(Potion potion, Potion strongerPotion) {
            this.potion = potion;
            this.strongerPotion = strongerPotion;
        }

        public ThrownPotion getPotionProjectile() {
            ThrownPotion thrownPotion = new ThrownPotion(level(), SkeletonWizard.this);
            Item potionType = shouldThrowLingeringPotion() ? Items.LINGERING_POTION : Items.SPLASH_POTION;
            thrownPotion.setItem(PotionUtils.setPotion(new ItemStack(potionType), isReinforced() ? strongerPotion : potion));
            return thrownPotion;
        }
    }
}