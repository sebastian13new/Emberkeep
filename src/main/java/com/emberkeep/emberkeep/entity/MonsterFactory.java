package com.emberkeep.emberkeep.entity;

import com.emberkeep.emberkeep.ai.ChaseStrategy;
import com.emberkeep.emberkeep.ai.PatrolStrategy;
import javafx.scene.paint.Color;

/** Factory Method: semua pembuatan monster dipusatkan di sini. */
public class MonsterFactory {
    public Monster create(MonsterType type, double x, double y, Player target) {
        return switch (type) {
            case PATROL -> new Monster(type, x, y, 2, 80, Color.web("#38B26A"),
                    new PatrolStrategy(x, 96), target);
            case CHASE -> new Monster(type, x, y, 3, 110, Color.web("#9775FA"),
                    new ChaseStrategy(230), target);
        };
    }
}
