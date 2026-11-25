package com.emberkeep.emberkeep.core.state;

import com.emberkeep.emberkeep.core.GameEngine;
import com.emberkeep.emberkeep.core.GameManager;
import com.emberkeep.emberkeep.ui.Hud;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class GameOverState implements GameState {
    @Override
    public void update(GameEngine engine, double dt) {
        if (engine.getInput().wasPressed(KeyCode.ENTER)) {
            engine.newGame();
        }
    }

    @Override
    public void render(GameEngine engine, GraphicsContext gc) {
        engine.renderWorld(gc);
        int score = GameManager.getInstance().getScore();
        Hud.drawOverlay(gc, "GAME OVER", "Skor " + score + "  -  tekan Enter untuk main lagi");
    }
}
