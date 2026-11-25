package com.emberkeep.emberkeep.ai;

import com.emberkeep.emberkeep.entity.Monster;
import com.emberkeep.emberkeep.entity.Player;
import com.emberkeep.emberkeep.level.Room;

/** Diam di tempat, lalu mengejar pemain yang masuk jangkauannya. */
public class ChaseStrategy implements MonsterStrategy {
    private final double sightRange;

    public ChaseStrategy(double sightRange) {
        this.sightRange = sightRange;
    }

    @Override
    public void move(Monster monster, Player target, Room room, double dt) {
        double dx = target.centerX() - monster.centerX();
        double dy = target.centerY() - monster.centerY();
        double dist = Math.hypot(dx, dy);
        if (dist == 0 || dist > sightRange) return;

        double step = monster.getSpeed() * dt;
        room.moveWithCollision(monster, dx / dist * step, dy / dist * step, true);
    }
}
