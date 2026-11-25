package com.emberkeep.emberkeep.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Efek partikel: percikan pedang, debu dash, bara api, dan angka skor. */
public class Particles {
    private static final Font TEXT_FONT = Font.font("Monospaced", FontWeight.BOLD, 18);

    private static final class Particle {
        double x, y, vx, vy, life, maxLife, size, gravity, drag;
        Color color;
        boolean glow;
        String text;
    }

    private final List<Particle> particles = new ArrayList<>();
    private final Random rng = new Random();

    public void clear() {
        particles.clear();
    }

    /** Ledakan kecil ke segala arah. */
    public void burst(double x, double y, int count, Color color, double speed, double size, boolean glow) {
        for (int i = 0; i < count; i++) {
            double angle = rng.nextDouble() * Math.PI * 2;
            double s = speed * (0.4 + rng.nextDouble() * 0.8);
            Particle p = new Particle();
            p.x = x;
            p.y = y;
            p.vx = Math.cos(angle) * s;
            p.vy = Math.sin(angle) * s;
            p.maxLife = p.life = 0.3 + rng.nextDouble() * 0.4;
            p.size = size * (0.6 + rng.nextDouble() * 0.8);
            p.drag = 4;
            p.gravity = 60;
            p.color = color;
            p.glow = glow;
            particles.add(p);
        }
    }

    public void dust(double x, double y) {
        Particle p = new Particle();
        p.x = x + (rng.nextDouble() - 0.5) * 14;
        p.y = y + (rng.nextDouble() - 0.5) * 4;
        p.vx = (rng.nextDouble() - 0.5) * 30;
        p.vy = -10 - rng.nextDouble() * 20;
        p.maxLife = p.life = 0.35 + rng.nextDouble() * 0.2;
        p.size = 4 + rng.nextDouble() * 4;
        p.drag = 2;
        p.color = Color.web("#8C8098");
        particles.add(p);
    }

    /** Bara api yang melayang pelan ke atas. */
    public void ember(double x, double y) {
        Particle p = new Particle();
        p.x = x;
        p.y = y;
        p.vx = (rng.nextDouble() - 0.5) * 16;
        p.vy = -14 - rng.nextDouble() * 22;
        p.maxLife = p.life = 2 + rng.nextDouble() * 2.5;
        p.size = 1.5 + rng.nextDouble() * 2;
        p.color = rng.nextDouble() < 0.7 ? Color.web("#FF8A4C") : Color.web("#FFD43B");
        p.glow = true;
        particles.add(p);
    }

    public void text(double x, double y, String text, Color color) {
        Particle p = new Particle();
        p.x = x;
        p.y = y;
        p.vy = -40;
        p.maxLife = p.life = 1.0;
        p.drag = 1.5;
        p.color = color;
        p.text = text;
        particles.add(p);
    }

    public void update(double dt) {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.life -= dt;
            if (p.life <= 0) {
                it.remove();
                continue;
            }
            p.vx -= p.vx * p.drag * dt;
            p.vy -= p.vy * p.drag * dt;
            p.vy += p.gravity * dt;
            if (p.glow && p.text == null && p.gravity == 0) {
                p.vx += Math.sin(p.life * 3 + p.x) * 6 * dt; // bara bergoyang
            }
            p.x += p.vx * dt;
            p.y += p.vy * dt;
        }
    }

    /** Partikel biasa (debu). */
    public void renderNormal(GraphicsContext gc) {
        for (Particle p : particles) {
            if (p.glow || p.text != null) continue;
            double a = p.life / p.maxLife;
            gc.setGlobalAlpha(a * 0.6);
            gc.setFill(p.color);
            double s = p.size * (1.4 - a * 0.4);
            gc.fillOval(p.x - s / 2, p.y - s / 2, s, s);
        }
        gc.setGlobalAlpha(1);
    }

    /** Partikel bercahaya. Pemanggil mengatur blend mode ADD. */
    public void renderGlow(GraphicsContext gc) {
        for (Particle p : particles) {
            if (!p.glow || p.text != null) continue;
            double a = Math.min(1, p.life / p.maxLife * 1.5);
            gc.setGlobalAlpha(a);
            gc.setFill(p.color);
            double s = p.size;
            gc.fillRect(p.x - s / 2, p.y - s / 2, s, s);
            gc.setGlobalAlpha(a * 0.25);
            gc.fillOval(p.x - s * 2, p.y - s * 2, s * 4, s * 4);
        }
        gc.setGlobalAlpha(1);
    }

    public void renderText(GraphicsContext gc) {
        gc.setFont(TEXT_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        for (Particle p : particles) {
            if (p.text == null) continue;
            gc.setGlobalAlpha(Math.min(1, p.life / p.maxLife * 2));
            gc.setFill(Color.web("#1A1420"));
            gc.fillText(p.text, p.x + 2, p.y + 2);
            gc.setFill(p.color);
            gc.fillText(p.text, p.x, p.y);
        }
        gc.setGlobalAlpha(1);
    }
}
