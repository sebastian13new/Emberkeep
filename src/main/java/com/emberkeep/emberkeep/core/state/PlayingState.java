package com.emberkeep.emberkeep.core.state;

import com.emberkeep.emberkeep.core.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class PlayingState implements GameState {
    @Override
    public void update(GameEngine engine, double dt) {
        if (engine.getInput().wasPressed(KeyCode.P, KeyCode.ESCAPE)) {
            engine.setState(new PausedState());
            return;
        }
        engine.updateWorld(dt);
    }

    @Override
    public void render(GameEngine engine, GraphicsContext gc) {
        engine.renderWorld(gc);
    }
}
