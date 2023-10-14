package lych.polonium.entity.monster.ai;

import lych.polonium.entity.monster.Ghoul;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class GhoulTargetAllMobsGoal extends NearestAttackableTargetGoal<Mob> {
    public GhoulTargetAllMobsGoal(Ghoul ghoul) {
        super(ghoul, Mob.class, true);
    }

    @Override
    public boolean canUse() {
        Ghoul ghoul = (Ghoul) mob;
        return ghoul.isHighHealth() && super.canUse();
    }
}
