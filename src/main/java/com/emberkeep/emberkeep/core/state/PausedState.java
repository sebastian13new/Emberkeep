package com.emberkeep.emberkeep.core.state;

import com.emberkeep.emberkeep.core.GameEngine;
import com.emberkeep.emberkeep.ui.Hud;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class PausedState implements GameState {
    @Override
    public void update(GameEngine engine, double dt) {
        if (engine.getInput().wasPressed(KeyCode.P, KeyCode.ESCAPE)) {
            engine.setState(new PlayingState());
        }
    }

    @Override
    public void render(GameEngine engine, GraphicsContext gc) {
        engine.renderWorld(gc);
        Hud.drawOverlay(gc, "PAUSE", "Tekan P untuk lanjut");
    }
}
