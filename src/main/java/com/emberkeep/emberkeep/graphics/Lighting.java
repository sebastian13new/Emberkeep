package com.emberkeep.emberkeep.graphics;

import com.emberkeep.emberkeep.level.Room;
import com.emberkeep.emberkeep.level.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import java.util.ArrayList;
import java.util.List;

/** Obor di dinding, cahaya di sekitar Julia, dan kegelapan di pinggir ruangan. */
public class Lighting {
    private record Torch(double x, double y, double seed) { }

    private final List<Torch> torches = new ArrayList<>();
    private final double width;
    private final double height;

    public Lighting(Room room) {
        this.width = room.getCols() * Room.TILE;
        this.height = room.getRows() * Room.TILE;
        int last = room.getCols() - 1;
        int bottom = room.getRows() - 1;
        int[] sideRows = {4, 14};
        for (int r : sideRows) {
            if (r < room.getRows() && room.getTile(r, 1) == Tile.FLOOR) {
                torches.add(new Torch(Room.TILE - 6, r * Room.TILE + 14, r * 1.7));
            }
            if (r < room.getRows() && room.getTile(r, last - 1) == Tile.FLOOR) {
                torches.add(new Torch(last * Room.TILE + 6, r * Room.TILE + 14, r * 2.3));
            }
        }
        int[] bottomCols = {6, 23};
        for (int c : bottomCols) {
            if (c < room.getCols() && room.getTile(bottom - 1, c) == Tile.FLOOR) {
                torches.add(new Torch(c * Room.TILE + 16, bottom * Room.TILE + 8, c * 0.9));
            }
        }
    }

    public List<double[]> getTorchPositions() {
        List<double[]> out = new ArrayList<>();
        for (Torch t : torches) out.add(new double[] {t.x, t.y});
        return out;
    }

    private static double flicker(double time, double seed) {
        return 0.85 + 0.1 * Math.sin(time * 11 + seed) + 0.05 * Math.sin(time * 27 + seed * 3);
    }

    /** Penyangga besi dan api obor. */
    public void renderTorches(GraphicsContext gc, double time) {
        for (Torch t : torches) {
            double f = flicker(time, t.seed);
            gc.setFill(Color.web("#2A2530"));
            gc.fillRect(t.x - 4, t.y + 2, 8, 10);
            gc.setFill(Color.web("#5A5266"));
            gc.fillRect(t.x - 6, t.y, 12, 3);

            double h = 16 * f;
            gc.setFill(Color.web("#E8590C"));
            gc.fillOval(t.x - 6, t.y - h + 2, 12, h);
            gc.setFill(Color.web("#FF8A4C"));
            gc.fillOval(t.x - 4.5, t.y - h * 0.8 + 2, 9, h * 0.8);
            gc.setFill(Color.web("#FFE9A8"));
            gc.fillOval(t.x - 2.5, t.y - h * 0.5 + 2, 5, h * 0.5);
        }
    }

    /** Lapisan gelap: terang di sekitar Julia, makin gelap ke pinggir. */
    public void renderDarkness(GraphicsContext gc, double px, double py) {
        RadialGradient dark = new RadialGradient(0, 0, px, py, 560, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(6, 4, 10, 0.0)),
                new Stop(0.35, Color.rgb(6, 4, 10, 0.08)),
                new Stop(1, Color.rgb(6, 4, 10, 0.72)));
        gc.setFill(dark);
        gc.fillRect(0, 0, width, height);
    }

    /** Cahaya obor. Pemanggil mengatur blend mode ADD. */
    public void renderGlow(GraphicsContext gc, double time) {
        for (Torch t : torches) {
            double f = flicker(time, t.seed);
            double radius = 130 * f;
            RadialGradient glow = new RadialGradient(0, 0, t.x, t.y - 4, radius, false, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(255, 150, 70, 0.32)),
                    new Stop(0.4, Color.rgb(255, 110, 40, 0.12)),
                    new Stop(1, Color.rgb(255, 90, 30, 0.0)));
            gc.setFill(glow);
            gc.fillOval(t.x - radius, t.y - 4 - radius, radius * 2, radius * 2);
        }
    }
}
