package com.emberkeep.emberkeep.ai;

import com.emberkeep.emberkeep.entity.Monster;
import com.emberkeep.emberkeep.entity.Player;
import com.emberkeep.emberkeep.level.Room;

/** Jalan bolak-balik ke kiri dan kanan di sekitar titik awal. */
public class PatrolStrategy implements MonsterStrategy {
    private final double originX;
    private final double range;
    private int direction = 1;

    public PatrolStrategy(double originX, double range) {
        this.originX = originX;
        this.range = range;
    }

    @Override
    public void move(Monster monster, Player target, Room room, double dt) {
        boolean blocked = room.moveWithCollision(monster, direction * monster.getSpeed() * dt, 0, true);
        double offset = monster.getX() - originX;
        if (blocked || (direction > 0 && offset > range) || (direction < 0 && offset < -range)) {
            direction = -direction;
        }
    }
}
