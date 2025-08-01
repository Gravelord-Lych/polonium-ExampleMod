package lych.polonium.entity.monster.ai;

import lych.polonium.entity.monster.Minion;
import lych.polonium.entity.monster.Necromancer;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MinionCopyOwnerTargetGoal extends TargetGoal {
    private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    @Nullable
    private Necromancer owner;

    public MinionCopyOwnerTargetGoal(Minion minion) {
        super(minion, false);
    }

    @Override
    public boolean canUse() {
        if (asMinion().getOwner() == null) {
            return false;
        }
        owner = asMinion().getOwner();
        if (owner == null || !owner.isAlive()) {
            return false;
        }
        if (owner.getTarget() == null) {
            return false;
        }
        return canAttack(owner.getTarget(), copyOwnerTargeting);
    }

    @Override
    public void start() {
        Objects.requireNonNull(owner, "Owner must not be null");
        asMinion().setTarget(owner.getTarget());
        super.start();
    }

    private Minion asMinion() {
        return (Minion) mob;
    }
}
