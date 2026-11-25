package com.emberkeep.emberkeep.entity;

import com.emberkeep.emberkeep.ai.MonsterStrategy;
import com.emberkeep.emberkeep.graphics.PixelSprite;
import com.emberkeep.emberkeep.graphics.Sprites;
import com.emberkeep.emberkeep.level.Room;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/** Monster biasa. Cara geraknya diatur oleh MonsterStrategy. */
public class Monster extends Entity {
    private static final double HIT_COOLDOWN = 0.3;
    private static final double KNOCKBACK_SPEED = 260;
    private static final double KNOCKBACK_TIME = 0.12;

    private final MonsterType type;
    private final int maxHp;
    private final double speed;
    private final Color particleColor;
    private final MonsterStrategy strategy;
    private final Player target;

    private int hp;
    private double hitTimer;
    private double knockX;
    private double knockY;
    private double knockTimer;
    private double animTime;

    public Monster(MonsterType type, double x, double y, int hp, double speed,
                   Color particleColor, MonsterStrategy strategy, Player target) {
        super(x, y, 26, 26);
        this.type = type;
        this.maxHp = hp;
        this.hp = hp;
        this.speed = speed;
        this.particleColor = particleColor;
        this.strategy = strategy;
        this.target = target;
        this.animTime = (x * 0.37 + y * 0.11) % 3; // supaya animasi tiap monster tidak serempak
    }

    @Override
    public void update(double dt, Room room) {
        animTime += dt;
        hitTimer = Math.max(0, hitTimer - dt);
        knockTimer = Math.max(0, knockTimer - dt);
        if (knockTimer > 0) {
            room.moveWithCollision(this, knockX * dt, knockY * dt, true);
        } else {
            strategy.move(this, target, room, dt);
        }
    }

    public boolean canBeHit() {
        return hitTimer <= 0;
    }

    public void takeHit(double dirX, double dirY) {
        hp--;
        hitTimer = HIT_COOLDOWN;
        knockX = dirX * KNOCKBACK_SPEED;
        knockY = dirY * KNOCKBACK_SPEED;
        knockTimer = KNOCKBACK_TIME;
        if (hp <= 0) {
            alive = false;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        Sprites s = Sprites.get();
        boolean flash = hitTimer > HIT_COOLDOWN - 0.12;
        double feet = y + height;

        if (type == MonsterType.PATROL) {
            // Slime: memantul dan memipih
            double squash = Math.sin(animTime * 7) * 0.09;
            double dw = 36 * (1 + squash);
            double dh = 28 * (1 - squash);
            gc.setFill(Color.rgb(0, 0, 0, 0.35));
            gc.fillOval(centerX() - 16, feet - 5, 32, 9);
            s.slime.draw(gc, centerX() - dw / 2, feet + 2 - dh, dw, dh, flash);
        } else {
            // Kelelawar bayangan: terbang naik turun sambil mengepakkan sayap
            double bob = Math.sin(animTime * 5) * 4;
            gc.setFill(Color.rgb(0, 0, 0, 0.25));
            gc.fillOval(centerX() - 12, feet - 3, 24, 7);
            PixelSprite frame = ((int) (animTime * 9)) % 2 == 0 ? s.batUp : s.batDown;
            frame.draw(gc, centerX() - 22, feet - 40 + bob, 44, 28, flash);
        }

        if (hp < maxHp) {
            double bw = 30;
            double bx = centerX() - bw / 2;
            double by = feet - (type == MonsterType.PATROL ? 38 : 50);
            gc.setFill(Color.web("#1A1420"));
            gc.fillRect(bx - 1, by - 1, bw + 2, 6);
            gc.setFill(Color.web("#F2B33D"));
            gc.fillRect(bx, by, bw * hp / maxHp, 4);
        }
    }

    public MonsterType getType() { return type; }
    public double getSpeed() { return speed; }
    public Color getParticleColor() { return particleColor; }
}
