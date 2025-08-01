package lych.polonium.entity.monster.ai;

import lych.polonium.entity.monster.Necromancer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

public abstract class NecromancerGoal extends Goal {
    private static final int DELAY_TICKS = 20;
    protected final Necromancer necromancer;
    @Nullable
    protected LivingEntity target;
    protected int ticksRemaining;

    public NecromancerGoal(Necromancer necromancer) {
        this.necromancer = necromancer;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!necromancer.isAlive() || necromancer.getTarget() == null) {
            return false;
        }
        target = necromancer.getTarget();
        return meetExtraUseConditions();
    }

    protected abstract boolean meetExtraUseConditions();

    @Override
    public void start() {
        super.start();
        necromancer.setSpellColor(getSpellColor());
        ticksRemaining = DELAY_TICKS;
    }

    protected abstract int getSpellColor();

    @Override
    public void tick() {
        super.tick();
        if (target != null && target.isAlive()) {
            necromancer.getLookControl().setLookAt(target);
        }
        if (ticksRemaining > 0) {
            ticksRemaining--;
            if (ticksRemaining == 0) {
                Objects.requireNonNull(target, "Unexpected null target");
                performAttack(target);
            }
        }
    }

    protected abstract void performAttack(LivingEntity target);

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && ticksRemaining > 0;
    }

    @Override
    public void stop() {
        super.stop();
        necromancer.setSpellColor(-1);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
