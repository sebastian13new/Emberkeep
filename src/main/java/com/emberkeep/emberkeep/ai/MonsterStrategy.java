package com.emberkeep.emberkeep.ai;

import com.emberkeep.emberkeep.entity.Monster;
import com.emberkeep.emberkeep.entity.Player;
import com.emberkeep.emberkeep.level.Room;

/** Strategy pattern: cara bergerak monster bisa ditukar. */
public interface MonsterStrategy {
    void move(Monster monster, Player target, Room room, double dt);
}
