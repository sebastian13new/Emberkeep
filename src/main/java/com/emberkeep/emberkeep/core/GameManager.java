package com.emberkeep.emberkeep.core;

import com.emberkeep.emberkeep.ui.EventBus;
import com.emberkeep.emberkeep.ui.GameEvent;
import com.emberkeep.emberkeep.ui.GameEventType;

/**
 * Singleton: menyimpan data permainan yang dipakai banyak kelas
 * (nyawa, skor, jumlah monster yang dikalahkan).
 */
public final class GameManager {
    public static final int MAX_LIVES = 3;
    public static final int MONSTER_POINTS = 150;

    private static GameManager instance;

    private int lives;
    private int score;
    private int monstersKilled;
    private EventBus eventBus;

    private GameManager() { }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void reset() {
        lives = MAX_LIVES;
        score = 0;
        monstersKilled = 0;
        publish(GameEventType.LIVES_CHANGED, lives);
        publish(GameEventType.SCORE_CHANGED, score);
    }

    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
        publish(GameEventType.LIVES_CHANGED, lives);
    }

    public void monsterKilled() {
        monstersKilled++;
        score += MONSTER_POINTS;
        publish(GameEventType.SCORE_CHANGED, score);
    }

    private void publish(GameEventType type, int value) {
        if (eventBus != null) {
            eventBus.publish(new GameEvent(type, value));
        }
    }

    public int getLives() { return lives; }
    public int getScore() { return score; }
    public int getMonstersKilled() { return monstersKilled; }
    public boolean isGameOver() { return lives <= 0; }
}
