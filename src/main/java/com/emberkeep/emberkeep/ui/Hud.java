package com.emberkeep.emberkeep.ui;

import com.emberkeep.emberkeep.core.EmberkeepApp;
import com.emberkeep.emberkeep.core.GameManager;
import com.emberkeep.emberkeep.entity.Player;
import com.emberkeep.emberkeep.graphics.Sprites;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/** Tampilan hati, skor, waktu, dan pesan. Datanya didapat lewat event. */
public class Hud implements Observer {
    private static final Font LABEL = Font.font("Monospaced", FontWeight.BOLD, 13);
    private static final Font VALUE = Font.font("Monospaced", FontWeight.BOLD, 22);
    private static final Font BIG = Font.font("Monospaced", FontWeight.BOLD, 60);
    private static final Font MID = Font.font("Monospaced", FontWeight.BOLD, 20);

    private static final Color PANEL = Color.rgb(20, 14, 26, 0.78);
    private static final Color BORDER = Color.web("#F2B33D", 0.55);
    private static final Color GOLD = Color.web("#F2B33D");
    private static final Color CREAM = Color.web("#FBF6EF");
    private static final Color MUTED = Color.web("#B8A9C9");
    private static final Color SHADOW = Color.web("#0B0910");

    private int lives = GameManager.MAX_LIVES;
    private int score;
    private int shownScore;
    private String message = "";
    private double messageTimer;
    private double heartPulse;

    @Override
    public void onEvent(GameEvent event) {
        switch (event.type()) {
            case LIVES_CHANGED -> {
                if (event.value() < lives) heartPulse = 0.5;
                lives = event.value();
            }
            case SCORE_CHANGED -> {
                score = event.value();
                if (score == 0) shownScore = 0;
            }
            case PLAYER_FELL -> showMessage("Julia jatuh ke lubang!", 1.5);
            case ROOM_CLEARED -> showMessage("Ruangan bersih! Kunci dan pintu menyusul di versi berikutnya", 4);
        }
    }

    public void showMessage(String text, double seconds) {
        message = text;
        messageTimer = seconds;
    }

    public void update(double dt) {
        messageTimer = Math.max(0, messageTimer - dt);
        heartPulse = Math.max(0, heartPulse - dt);
        if (shownScore < score) {
            shownScore = Math.min(score, shownScore + (int) Math.ceil(600 * dt));
        }
    }

    private static void panel(GraphicsContext gc, double x, double y, double w, double h) {
        gc.setFill(PANEL);
        gc.fillRoundRect(x, y, w, h, 14, 14);
        gc.setStroke(BORDER);
        gc.setLineWidth(2);
        gc.strokeRoundRect(x + 1, y + 1, w - 2, h - 2, 12, 12);
    }

    private static void text(GraphicsContext gc, String s, double x, double y, Color color) {
        gc.setFill(SHADOW);
        gc.fillText(s, x + 2, y + 2);
        gc.setFill(color);
        gc.fillText(s, x, y);
    }

    public void render(GraphicsContext gc, double seconds, int monstersLeft, Player player) {
        Sprites sp = Sprites.get();
        gc.setTextAlign(TextAlignment.LEFT);

        // Panel kiri: nyawa dan skor
        panel(gc, 12, 10, 330, 52);
        for (int i = 0; i < GameManager.MAX_LIVES; i++) {
            double pulse = (i == lives && heartPulse > 0) ? Math.sin(heartPulse * 30) * 3 : 0;
            (i < lives ? sp.heartFull : sp.heartEmpty).draw(gc, 24 + i * 32 + pulse, 22, 3);
        }
        gc.setFont(LABEL);
        text(gc, "SKOR", 132, 30, MUTED);
        gc.setFont(VALUE);
        text(gc, String.format("%06d", shownScore), 132, 53, GOLD);

        // Panel tengah: waktu dan sisa monster
        panel(gc, 360, 10, 240, 52);
        int t = (int) seconds;
        gc.setFont(LABEL);
        text(gc, "WAKTU", 376, 30, MUTED);
        text(gc, "MONSTER", 498, 30, MUTED);
        gc.setFont(VALUE);
        text(gc, String.format("%02d:%02d", t / 60, t % 60), 376, 53, CREAM);
        text(gc, String.valueOf(monstersLeft), 498, 53, CREAM);

        // Panel kanan: dash
        panel(gc, 618, 10, 330, 52);
        gc.setFont(LABEL);
        double ready = player.getDashReadiness();
        text(gc, ready >= 1 ? "DASH  [K]  SIAP" : "DASH  [K]", 634, 30, ready >= 1 ? Color.web("#3BC9DB") : MUTED);
        gc.setFill(Color.web("#2A2233"));
        gc.fillRoundRect(634, 38, 298, 14, 8, 8);
        gc.setFill(ready >= 1 ? Color.web("#3BC9DB") : Color.web("#1098AD"));
        gc.fillRoundRect(634, 38, 298 * ready, 14, 8, 8);
        gc.setFill(Color.rgb(255, 255, 255, 0.25));
        gc.fillRect(638, 40, Math.max(0, 298 * ready - 8), 3);

        // Pesan di bawah layar
        if (messageTimer > 0) {
            double alpha = Math.min(1, messageTimer * 2);
            gc.setGlobalAlpha(alpha);
            gc.setFont(MID);
            double w = Math.max(320, message.length() * 12.5 + 48);
            panel(gc, (EmberkeepApp.WIDTH - w) / 2, EmberkeepApp.HEIGHT - 62, w, 44);
            gc.setTextAlign(TextAlignment.CENTER);
            text(gc, message, EmberkeepApp.WIDTH / 2.0, EmberkeepApp.HEIGHT - 33, GOLD);
            gc.setGlobalAlpha(1);
        }
    }

    /** Lapisan gelap dengan judul, dipakai layar pause dan game over. */
    public static void drawOverlay(GraphicsContext gc, String title, String subtitle) {
        gc.setFill(Color.rgb(8, 5, 12, 0.72));
        gc.fillRect(0, 0, EmberkeepApp.WIDTH, EmberkeepApp.HEIGHT);
        double w = 620;
        double h = 190;
        double x = (EmberkeepApp.WIDTH - w) / 2;
        double y = (EmberkeepApp.HEIGHT - h) / 2;
        panel(gc, x, y, w, h);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(BIG);
        text(gc, title, EmberkeepApp.WIDTH / 2.0, y + 92, GOLD);
        gc.setFont(MID);
        text(gc, subtitle, EmberkeepApp.WIDTH / 2.0, y + 145, CREAM);
    }
}
