package com.emberkeep.emberkeep.core;

import com.emberkeep.emberkeep.core.state.GameOverState;
import com.emberkeep.emberkeep.core.state.GameState;
import com.emberkeep.emberkeep.core.state.PlayingState;
import com.emberkeep.emberkeep.entity.Entity;
import com.emberkeep.emberkeep.entity.Monster;
import com.emberkeep.emberkeep.entity.MonsterFactory;
import com.emberkeep.emberkeep.entity.Player;
import com.emberkeep.emberkeep.graphics.Lighting;
import com.emberkeep.emberkeep.graphics.Particles;
import com.emberkeep.emberkeep.input.Command;
import com.emberkeep.emberkeep.input.InputHandler;
import com.emberkeep.emberkeep.level.JembatanRuntuhLoader;
import com.emberkeep.emberkeep.level.Room;
import com.emberkeep.emberkeep.level.RoomLoader;
import com.emberkeep.emberkeep.ui.EventBus;
import com.emberkeep.emberkeep.ui.GameEvent;
import com.emberkeep.emberkeep.ui.GameEventType;
import com.emberkeep.emberkeep.ui.Hud;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Facade: satu pintu untuk semua subsistem (input, ruangan, entity, efek, HUD).
 * Game loop juga berjalan di sini.
 */
public class GameEngine {
    private final GraphicsContext gc;
    private final InputHandler input = new InputHandler();
    private final EventBus eventBus = new EventBus();
    private final Hud hud = new Hud();
    private final Particles particles = new Particles();
    private final List<Monster> monsters = new ArrayList<>();
    private final Random rng = new Random();

    private Room room;
    private Lighting lighting;
    private Player player;
    private GameState state;
    private double elapsedSeconds;
    private boolean roomCleared;
    private double shakeTimer;
    private double emberTimer;
    private double dustTimer;

    public GameEngine(GraphicsContext gc) {
        this.gc = gc;
        eventBus.subscribe(hud);
        GameManager.getInstance().setEventBus(eventBus);
        newGame();
    }

    /** Mulai (atau ulang) permainan dari awal. */
    public void newGame() {
        GameManager.getInstance().reset();

        RoomLoader loader = new JembatanRuntuhLoader();
        room = loader.load();
        lighting = new Lighting(room);
        player = new Player(room.getSpawnX(), room.getSpawnY());

        monsters.clear();
        MonsterFactory factory = new MonsterFactory();
        for (Room.MonsterSpawn spawn : room.getMonsterSpawns()) {
            monsters.add(factory.create(spawn.type(), spawn.x(), spawn.y(), player));
        }

        particles.clear();
        elapsedSeconds = 0;
        roomCleared = false;
        shakeTimer = 0;
        hud.showMessage(room.getName(), 2.5);
        setState(new PlayingState());
    }

    /** Menjalankan game loop sekitar 60 kali per detik. */
    public void start() {
        AnimationTimer loop = new AnimationTimer() {
            private long last = -1;

            @Override
            public void handle(long now) {
                if (last < 0) {
                    last = now;
                    return;
                }
                double dt = Math.min((now - last) / 1_000_000_000.0, 0.05);
                last = now;

                state.update(GameEngine.this, dt);
                state.render(GameEngine.this, gc);
                input.endFrame();
            }
        };
        loop.start();
    }

    /** Satu langkah dunia game. Dipanggil oleh PlayingState. */
    public void updateWorld(double dt) {
        elapsedSeconds += dt;
        hud.update(dt);
        particles.update(dt);
        shakeTimer = Math.max(0, shakeTimer - dt);
        spawnAmbientEmbers(dt);

        for (Command command : input.collectCommands()) {
            command.execute(player);
        }
        player.update(dt, room);

        if (player.isDashing()) {
            dustTimer -= dt;
            if (dustTimer <= 0) {
                particles.dust(player.centerX(), player.getY() + player.getHeight());
                dustTimer = 0.02;
            }
        }

        // Jatuh ke lubang (tidak berlaku saat dash, jadi lubang bisa dilompati)
        if (!player.isDashing() && room.isPitAt(player.centerX(), player.centerY())) {
            particles.burst(player.centerX(), player.centerY(), 18, Color.web("#4A4053"), 90, 5, false);
            player.respawnAt(room.getSpawnX(), room.getSpawnY());
            GameManager.getInstance().loseLife();
            eventBus.publish(new GameEvent(GameEventType.PLAYER_FELL, 0));
            shakeTimer = 0.3;
        }

        for (Monster monster : monsters) {
            monster.update(dt, room);

            if (player.isAttacking() && monster.canBeHit()
                    && monster.getBounds().intersects(player.getSwordBounds())) {
                monster.takeHit(player.getFacingX(), player.getFacingY());
                particles.burst(monster.centerX(), monster.centerY(), 10, Color.web("#FFE9A8"), 160, 3, true);
                shakeTimer = Math.max(shakeTimer, 0.08);
                if (!monster.isAlive()) {
                    GameManager.getInstance().monsterKilled();
                    particles.burst(monster.centerX(), monster.centerY(), 26, monster.getParticleColor(), 140, 6, false);
                    particles.burst(monster.centerX(), monster.centerY(), 12, monster.getParticleColor(), 90, 3, true);
                    particles.text(monster.centerX(), monster.getY() - 10,
                            "+" + GameManager.MONSTER_POINTS, Color.web("#F2B33D"));
                }
            }

            if (monster.isAlive() && !player.isInvincible()
                    && monster.getBounds().intersects(player.getBounds())) {
                player.hurt(monster.centerX(), monster.centerY());
                GameManager.getInstance().loseLife();
                particles.burst(player.centerX(), player.centerY(), 14, Color.web("#F7A1BE"), 130, 4, true);
                shakeTimer = 0.25;
            }
        }
        monsters.removeIf(m -> !m.isAlive());

        if (GameManager.getInstance().isGameOver()) {
            setState(new GameOverState());
        } else if (monsters.isEmpty() && !roomCleared) {
            roomCleared = true;
            eventBus.publish(new GameEvent(GameEventType.ROOM_CLEARED, 0));
        }
    }

    private void spawnAmbientEmbers(double dt) {
        emberTimer -= dt;
        if (emberTimer > 0) return;
        emberTimer = 0.08;
        double x = Room.TILE + rng.nextDouble() * (room.getCols() - 2) * Room.TILE;
        double y = Room.TILE * 3 + rng.nextDouble() * (room.getRows() - 4) * Room.TILE;
        particles.ember(x, y);
        // Obor juga sesekali mengeluarkan bara
        for (double[] t : lighting.getTorchPositions()) {
            if (rng.nextDouble() < 0.25) particles.ember(t[0] + (rng.nextDouble() - 0.5) * 6, t[1] - 10);
        }
    }

    public void renderWorld(GraphicsContext g) {
        double time = System.nanoTime() / 1_000_000_000.0;
        g.setFill(Color.web("#0B0910"));
        g.fillRect(0, 0, EmberkeepApp.WIDTH, EmberkeepApp.HEIGHT);

        g.save();
        if (shakeTimer > 0) {
            double power = 6 * shakeTimer / 0.3;
            g.translate((rng.nextDouble() - 0.5) * power, (rng.nextDouble() - 0.5) * power);
        }

        room.render(g);
        lighting.renderTorches(g, time);

        // Entity digambar dari yang paling atas ke paling bawah supaya tumpukannya benar
        List<Entity> entities = new ArrayList<>(monsters);
        entities.add(player);
        entities.sort(Comparator.comparingDouble(e -> e.getY() + e.getHeight()));
        for (Entity e : entities) {
            e.render(g);
        }

        particles.renderNormal(g);
        lighting.renderDarkness(g, player.centerX(), player.centerY());

        g.setGlobalBlendMode(BlendMode.ADD);
        lighting.renderGlow(g, time);
        particles.renderGlow(g);
        player.renderEffects(g);
        g.setGlobalBlendMode(BlendMode.SRC_OVER);

        particles.renderText(g);
        g.restore();

        hud.render(g, elapsedSeconds, monsters.size(), player);
    }

    public void setState(GameState next) {
        this.state = next;
    }

    public InputHandler getInput() { return input; }
}
