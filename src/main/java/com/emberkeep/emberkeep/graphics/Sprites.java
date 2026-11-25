package com.emberkeep.emberkeep.graphics;

import static com.emberkeep.emberkeep.graphics.SpriteData.*;

/**
 * Semua gambar dibuat sekali lalu dipakai bersama oleh banyak objek
 * (misalnya semua slime memakai gambar yang sama).
 */
public final class Sprites {
    public static final int SCALE = 2;

    private static Sprites instance;

    /** Index frame: 0 = diam, 1 = langkah A, 2 = langkah B. */
    public final PixelSprite[] juliaDown = new PixelSprite[3];
    public final PixelSprite[] juliaUp = new PixelSprite[3];
    public final PixelSprite[] juliaRight = new PixelSprite[3];
    public final PixelSprite[] juliaLeft = new PixelSprite[3];
    public final PixelSprite sword;
    public final PixelSprite slime;
    public final PixelSprite batUp;
    public final PixelSprite batDown;
    public final PixelSprite heartFull;
    public final PixelSprite heartEmpty;

    private Sprites() {
        String[][] legs = {LEGS_IDLE, LEGS_A, LEGS_B};
        String[][] sideLegs = {SIDE_LEGS_IDLE, SIDE_LEGS_A, SIDE_LEGS_B};
        for (int i = 0; i < 3; i++) {
            juliaDown[i] = new PixelSprite(PixelArt.concat(JULIA_DOWN, legs[i]), JULIA_PALETTE);
            juliaUp[i] = new PixelSprite(PixelArt.concat(JULIA_UP, legs[i]), JULIA_PALETTE);
            String[] side = PixelArt.concat(JULIA_SIDE, sideLegs[i]);
            juliaRight[i] = new PixelSprite(side, JULIA_PALETTE);
            juliaLeft[i] = new PixelSprite(PixelArt.flip(side), JULIA_PALETTE);
        }
        sword = new PixelSprite(SWORD, SWORD_PALETTE);
        slime = new PixelSprite(SLIME, SLIME_PALETTE);
        batUp = new PixelSprite(BAT_UP, BAT_PALETTE);
        batDown = new PixelSprite(BAT_DOWN, BAT_PALETTE);
        heartFull = new PixelSprite(HEART, HEART_PALETTE);
        heartEmpty = new PixelSprite(HEART, HEART_EMPTY_PALETTE);
    }

    public static Sprites get() {
        if (instance == null) {
            instance = new Sprites();
        }
        return instance;
    }
}
