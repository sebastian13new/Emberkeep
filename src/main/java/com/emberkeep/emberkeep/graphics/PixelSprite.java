package com.emberkeep.emberkeep.graphics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.Map;

/** Satu gambar pixel art yang siap digambar, beserta versi putihnya. */
public final class PixelSprite {
    private final Image normal;
    private final Image flash;
    private final int width;
    private final int height;

    public PixelSprite(String[] rows, Map<Character, Integer> palette) {
        this.width = PixelArt.width(rows);
        this.height = rows.length;
        int[] px = PixelArt.fromGrid(rows, palette);
        this.normal = toImage(px, width, height);
        this.flash = toImage(PixelArt.whiten(px), width, height);
    }

    public static Image toImage(int[] px, int w, int h) {
        WritableImage img = new WritableImage(w, h);
        PixelWriter pw = img.getPixelWriter();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                pw.setArgb(x, y, px[y * w + x]);
            }
        }
        return img;
    }

    public void draw(GraphicsContext gc, double x, double y, double dw, double dh, boolean white) {
        gc.drawImage(white ? flash : normal, x, y, dw, dh);
    }

    public void draw(GraphicsContext gc, double x, double y, double scale) {
        gc.drawImage(normal, x, y, width * scale, height * scale);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
