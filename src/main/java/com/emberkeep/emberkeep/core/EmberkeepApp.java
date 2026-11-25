package com.emberkeep.emberkeep.core;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Titik masuk aplikasi JavaFX. Tugasnya cuma menyiapkan jendela,
 * lalu menyerahkan semuanya ke GameEngine.
 */
public class EmberkeepApp extends Application {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        Scene scene = new Scene(new Group(canvas), WIDTH, HEIGHT, Color.BLACK);

        // Matikan penghalusan supaya pixel art tetap tajam saat diperbesar
        canvas.getGraphicsContext2D().setImageSmoothing(false);

        GameEngine engine = new GameEngine(canvas.getGraphicsContext2D());
        scene.setOnKeyPressed(e -> engine.getInput().press(e.getCode()));
        scene.setOnKeyReleased(e -> engine.getInput().release(e.getCode()));

        stage.setTitle("Emberkeep - Kisah Putri Julia dan Kastel Terkutuk");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        engine.start();
    }
}
