package com.emberkeep.emberkeep.core.state;

import com.emberkeep.emberkeep.core.GameEngine;
import javafx.scene.canvas.GraphicsContext;

/**
 * State pattern: perilaku game tiap frame tergantung state yang aktif
 * (main, pause, atau game over).
 */
public interface GameState {
    void update(GameEngine engine, double dt);

    void render(GameEngine engine, GraphicsContext gc);
}
