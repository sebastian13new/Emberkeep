package com.emberkeep.emberkeep.entity;

import com.emberkeep.emberkeep.graphics.PixelSprite;
import com.emberkeep.emberkeep.graphics.Sprites;
import com.emberkeep.emberkeep.level.Room;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Putri Julia: bisa jalan, menyerang dengan pedang, dan dash. */
public class Player extends Entity {
    private static final double SPEED = 190;
    private static final double DASH_SPEED = 560;
    private static final double DASH_TIME = 0.16;
    private static final double DASH_COOLDOWN = 0.8;
    private static final double ATTACK_TIME = 0.2;
    private static final double ATTACK_COOLDOWN = 0.35;
    private static final double INVINCIBLE_TIME = 1.0;
    private static final double KNOCKBACK_SPEED = 320;
    private static final double KNOCKBACK_TIME = 0.15;
    private static final double SWORD_REACH = 36;
    private static final double SWORD_WIDTH = 40;
    private static final double SWING_DEGREES = 150;

    /** Bayangan sisa dash. */
    private static final class Afterimage {
        final double x, y;
        final PixelSprite sprite;
        double alpha = 0.55;

        Afterimage(double x, double y, PixelSprite sprite) {
            this.x = x;
            this.y = y;
            this.sprite = sprite;
        }
    }

    private double moveX;
    private double moveY;
    private double facingX = 0;
    private double facingY = 1;
    private boolean moving;
    private double animTime;

    private double dashTimer;
    private double dashCooldown;
    private double dashDirX;
    private double dashDirY;
    private double attackTimer;
    private double attackCooldown;
    private double invincibleTimer;
    private double knockX;
    private double knockY;
    private double knockTimer;
    private double trailTimer;
    private final List<Afterimage> trail = new ArrayList<>();

    public Player(double x, double y) {
        super(x, y, 26, 26);
    }

    public void addMove(double dx, double dy) {
        moveX += dx;
        moveY += dy;
    }

    public void attack() {
        if (attackCooldown <= 0 && dashTimer <= 0) {
            attackTimer = ATTACK_TIME;
            attackCooldown = ATTACK_COOLDOWN;
        }
    }

    public void dash() {
        if (dashCooldown > 0 || dashTimer > 0) return;
        double dx = moveX;
        double dy = moveY;
        if (dx == 0 && dy == 0) {
            dx = facingX;
            dy = facingY;
        }
        double len = Math.hypot(dx, dy);
        dashDirX = dx / len;
        dashDirY = dy / len;
        dashTimer = DASH_TIME;
        dashCooldown = DASH_COOLDOWN;
    }

    /** Kena serang monster: kebal sebentar dan terdorong menjauh. */
    public void hurt(double fromX, double fromY) {
        invincibleTimer = INVINCIBLE_TIME;
        double dx = centerX() - fromX;
        double dy = centerY() - fromY;
        double len = Math.hypot(dx, dy);
        if (len == 0) { dx = 0; dy = 1; len = 1; }
        knockX = dx / len * KNOCKBACK_SPEED;
        knockY = dy / len * KNOCKBACK_SPEED;
        knockTimer = KNOCKBACK_TIME;
    }

    public void respawnAt(double sx, double sy) {
        setPosition(sx, sy);
        dashTimer = 0;
        knockTimer = 0;
        invincibleTimer = INVINCIBLE_TIME;
        trail.clear();
    }

    @Override
    public void update(double dt, Room room) {
        dashTimer = Math.max(0, dashTimer - dt);
        dashCooldown = Math.max(0, dashCooldown - dt);
        attackTimer = Math.max(0, attackTimer - dt);
        attackCooldown = Math.max(0, attackCooldown - dt);
        invincibleTimer = Math.max(0, invincibleTimer - dt);
        knockTimer = Math.max(0, knockTimer - dt);

        double vx;
        double vy;
        moving = false;
        if (knockTimer > 0) {
            vx = knockX;
            vy = knockY;
        } else if (dashTimer > 0) {
            vx = dashDirX * DASH_SPEED;
            vy = dashDirY * DASH_SPEED;
        } else {
            double len = Math.hypot(moveX, moveY);
            if (len > 0) {
                double nx = moveX / len;
                double ny = moveY / len;
                // Saat menyerang arah hadap dikunci supaya ayunan pedang tidak berbelok
                if (attackTimer <= 0) {
                    if (Math.abs(nx) >= Math.abs(ny)) {
                        facingX = Math.signum(nx);
                        facingY = 0;
                    } else {
                        facingX = 0;
                        facingY = Math.signum(ny);
                    }
                }
                vx = nx * SPEED;
                vy = ny * SPEED;
                moving = true;
            } else {
                vx = 0;
                vy = 0;
            }
        }
        room.moveWithCollision(this, vx * dt, vy * dt, false);
        moveX = 0;
        moveY = 0;

        animTime = moving ? animTime + dt : 0;

        // Bayangan sisa dash
        trailTimer -= dt;
        if (dashTimer > 0 && trailTimer <= 0) {
            trail.add(new Afterimage(spriteX(), spriteY(), currentSprite()));
            trailTimer = 0.025;
        }
        Iterator<Afterimage> it = trail.iterator();
        while (it.hasNext()) {
            Afterimage a = it.next();
            a.alpha -= dt * 2.8;
            if (a.alpha <= 0) it.remove();
        }
    }

    public Bounds getSwordBounds() {
        double cx = centerX();
        double cy = centerY();
        if (facingX > 0) return new Bounds(cx, cy - SWORD_WIDTH / 2, width / 2 + SWORD_REACH, SWORD_WIDTH);
        if (facingX < 0) return new Bounds(cx - width / 2 - SWORD_REACH, cy - SWORD_WIDTH / 2, width / 2 + SWORD_REACH, SWORD_WIDTH);
        if (facingY < 0) return new Bounds(cx - SWORD_WIDTH / 2, cy - height / 2 - SWORD_REACH, SWORD_WIDTH, height / 2 + SWORD_REACH);
        return new Bounds(cx - SWORD_WIDTH / 2, cy, SWORD_WIDTH, height / 2 + SWORD_REACH);
    }

    // ---------------- gambar ----------------

    private PixelSprite[] spriteSet() {
        Sprites s = Sprites.get();
        if (facingY > 0) return s.juliaDown;
        if (facingY < 0) return s.juliaUp;
        return facingX > 0 ? s.juliaRight : s.juliaLeft;
    }

    private PixelSprite currentSprite() {
        int frame = 0;
        if (moving) {
            int[] cycle = {1, 0, 2, 0};
            frame = cycle[(int) (animTime * 9) % 4];
        }
        return spriteSet()[frame];
    }

    private double spriteX() {
        return centerX() - 20;
    }

    private double spriteY() {
        double bob = moving && ((int) (animTime * 9) % 2 == 0) ? -2 : 0;
        return y + height + 4 - 52 + bob;
    }

    /** Sudut arah hadap dalam derajat (0 = kanan, 90 = bawah). */
    private double facingAngle() {
        if (facingX > 0) return 0;
        if (facingX < 0) return 180;
        return facingY < 0 ? -90 : 90;
    }

    private double swingAngle() {
        double p = 1 - attackTimer / ATTACK_TIME;
        double eased = 1 - (1 - p) * (1 - p);
        return facingAngle() - SWING_DEGREES / 2 + SWING_DEGREES * eased;
    }

    @Override
    public void render(GraphicsContext gc) {
        for (Afterimage a : trail) {
            gc.setGlobalAlpha(a.alpha * 0.6);
            a.sprite.draw(gc, a.x, a.y, 40, 52, true);
        }
        gc.setGlobalAlpha(1);

        // Bayangan di lantai
        gc.setFill(Color.rgb(0, 0, 0, 0.35));
        gc.fillOval(centerX() - 15, y + height - 3, 30, 9);

        // Berkedip saat kebal
        if (invincibleTimer > 0 && ((int) (invincibleTimer * 14)) % 2 == 0) return;

        boolean swordBehind = facingY < 0 && !isAttacking();
        if (swordBehind) drawSword(gc);
        currentSprite().draw(gc, spriteX(), spriteY(), 40, 52, false);
        if (!swordBehind) drawSword(gc);
    }

    private void drawSword(GraphicsContext gc) {
        PixelSprite sword = Sprites.get().sword;
        if (isAttacking()) {
            gc.save();
            gc.translate(centerX(), centerY() - 6);
            gc.rotate(swingAngle() + 90);
            sword.draw(gc, -5, -14 * 2 - 10, 10, 32, false);
            gc.restore();
            return;
        }
        if (isDashing()) return;
        // Pedang dipegang di tangan saat tidak menyerang
        double sx = spriteX();
        double sy = spriteY();
        double handX;
        double handY = sy + 17 * 2;
        if (facingX < 0) {
            handX = sx + 4 * 2;
        } else if (facingX > 0) {
            handX = sx + 15 * 2;
        } else {
            handX = sx + 16 * 2 + 1;
        }
        sword.draw(gc, handX - 5, handY - 14 * 2, 10, 32, false);
    }

    /** Jejak tebasan pedang, digambar di lapisan cahaya (blend ADD). */
    public void renderEffects(GraphicsContext gc) {
        if (!isAttacking()) return;
        double start = facingAngle() - SWING_DEGREES / 2;
        double end = swingAngle();
        double r = 42;
        double cx = centerX();
        double cy = centerY() - 6;
        double p = attackTimer / ATTACK_TIME;
        gc.setLineWidth(8);
        gc.setStroke(Color.rgb(200, 220, 255, 0.35 * p + 0.15));
        gc.strokeArc(cx - r, cy - r, r * 2, r * 2, -start, -(end - start), ArcType.OPEN);
        gc.setLineWidth(3);
        gc.setStroke(Color.rgb(255, 255, 255, 0.6 * p + 0.2));
        gc.strokeArc(cx - r, cy - r, r * 2, r * 2, -start, -(end - start), ArcType.OPEN);
    }

    public boolean isAttacking() { return attackTimer > 0; }
    public boolean isDashing() { return dashTimer > 0; }
    public boolean isInvincible() { return invincibleTimer > 0; }
    public double getFacingX() { return facingX; }
    public double getFacingY() { return facingY; }

    /** 0 = baru saja dash, 1 = dash siap dipakai lagi. */
    public double getDashReadiness() {
        return 1.0 - dashCooldown / DASH_COOLDOWN;
    }
}
