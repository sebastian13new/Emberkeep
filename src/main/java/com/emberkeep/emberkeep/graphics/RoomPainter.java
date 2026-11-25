package com.emberkeep.emberkeep.graphics;

import com.emberkeep.emberkeep.level.Room;
import com.emberkeep.emberkeep.level.Tile;

/**
 * Menggambar lantai batu, dinding bata, lubang, dan pintu piksel demi piksel.
 * Hasilnya dibuat sekali saat ruangan dimuat, lalu dipakai ulang tiap frame.
 */
public final class RoomPainter {
    private static final int T = Room.TILE;

    private RoomPainter() { }

    public static int[] paint(Room room) {
        int cols = room.getCols();
        int rows = room.getRows();
        int w = cols * T;
        int[] px = new int[w * rows * T];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                switch (room.getTile(r, c)) {
                    case FLOOR -> paintFloor(px, w, room, r, c);
                    case WALL -> paintWall(px, w, room, r, c);
                    case PIT -> paintPit(px, w, room, r, c);
                }
            }
        }
        paintDoor(px, w, room.getSpawnRow(), room.getSpawnCol() - 1);
        return px;
    }

    // ---------- lantai ----------
    private static void paintFloor(int[] px, int w, Room room, int r, int c) {
        boolean wallAbove = isWall(room, r - 1, c);
        boolean wallLeft = isWall(room, r, c - 1);
        boolean wallRight = isWall(room, r, c + 1);

        for (int ly = 0; ly < T; ly++) {
            for (int lx = 0; lx < T; lx++) {
                int gx = c * T + lx;
                int gy = r * T + ly;
                // Tiap ubin dipecah menjadi batu dengan pola acak supaya lantai tidak seragam
                int pattern = (int) (rnd(c, r, 47) * 4);
                int cellW = (pattern == 1 || pattern == 3) ? 16 : 32;
                int cellH = (pattern == 2 || pattern == 3) ? 16 : 32;
                int inX = lx % cellW;
                int inY = ly % cellH;
                int sx = c * 4 + lx / cellW;
                int sy = r * 4 + ly / cellH;

                double v = rnd(sx, sy, 7);
                int base = v < 0.25 ? 0x2E2837 : v < 0.5 ? 0x352D3F : v < 0.75 ? 0x2A2433 : 0x312A35;
                int col = shade(base, (int) ((rnd(gx, gy, 3) - 0.5) * 9));
                // Sedikit gradasi di dalam batu supaya terlihat menonjol
                col = shade(col, (int) (4 - (inX + inY) * 8.0 / (cellW + cellH)));

                if (inX == cellW - 1 || inY == cellH - 1) {
                    col = 0x19141F;                        // nat antar batu
                    if (rnd(gx, gy, 11) < 0.12) col = 0x27332A; // lumut
                } else if (inX == 0 || inY == 0) {
                    col = shade(col, 12);                  // tepi batu lebih terang
                } else if (inX == cellW - 2 || inY == cellH - 2) {
                    col = shade(col, -8);
                }
                if (cellW == 32 && rnd(sx, sy, 21) < 0.3 && isCrack(inX % 16, inY % 16, sx, sy)) {
                    col = shade(col, -16);
                }

                // Bayangan dari dinding di atas dan samping
                if (wallAbove && ly < 10) col = mix(col, 0x0E0B12, (10 - ly) / 14.0);
                if (wallLeft && lx < 6) col = mix(col, 0x0E0B12, (6 - lx) / 12.0);
                if (wallRight && lx > T - 7) col = mix(col, 0x0E0B12, (lx - (T - 7)) / 12.0);

                set(px, w, gx, gy, col);
            }
        }
    }

    private static boolean isCrack(int x, int y, int sx, int sy) {
        int start = (int) (rnd(sx, sy, 31) * 10) + 2;
        int cy = start + (x / 3) * (rnd(sx, sy, 41) < 0.5 ? 1 : -1);
        return x > 2 && x < 13 && Math.abs(y - cy) == 0;
    }

    // ---------- dinding ----------
    private static void paintWall(int[] px, int w, Room room, int r, int c) {
        boolean front = !isWall(room, r + 1, c) && r + 1 < room.getRows();
        boolean openLeft = !isWall(room, r, c - 1) && c - 1 >= 0;
        boolean openRight = !isWall(room, r, c + 1) && c + 1 < room.getCols();
        boolean openUp = !isWall(room, r - 1, c) && r - 1 >= 0;

        for (int ly = 0; ly < T; ly++) {
            for (int lx = 0; lx < T; lx++) {
                int gx = c * T + lx;
                int gy = r * T + ly;
                int col;
                if (front) {
                    // Muka dinding: susunan bata
                    int row = gy / 8;
                    int shift = (row % 2) * 8;
                    int bx = (gx + shift) / 16;
                    int inX = (gx + shift) % 16;
                    int inY = gy % 8;
                    int brick = shade(0x4E4259, (int) ((rnd(bx, row, 5) - 0.5) * 18));
                    col = shade(brick, (int) ((rnd(gx, gy, 9) - 0.5) * 8));
                    if (inY == 7 || inX == 15) col = 0x1B1620;
                    else if (inY == 0) col = shade(col, 14);
                    else if (inY == 6) col = shade(col, -10);
                    if (ly > T - 4) col = mix(col, 0x0E0B12, (ly - (T - 4)) / 4.0);
                } else {
                    // Bagian atas dinding
                    col = shade(0x241E2B, (int) ((rnd(gx, gy, 13) - 0.5) * 8));
                    if ((gx / 32 + gy / 16) % 2 == 0 && (gy % 16 == 0 || gx % 32 == 0)) col = 0x1D1823;
                    int rim = 0x5C4F69;
                    if (openLeft && lx < 3) col = lx == 2 ? 0x1B1620 : rim;
                    if (openRight && lx > T - 4) col = lx == T - 3 ? 0x1B1620 : rim;
                    if (openUp && ly < 3) col = ly == 2 ? 0x1B1620 : rim;
                }
                set(px, w, gx, gy, col);
            }
        }
    }

    // ---------- lubang ----------
    private static void paintPit(int[] px, int w, Room room, int r, int c) {
        boolean edgeTop = !isPit(room, r - 1, c);
        boolean edgeLeft = !isPit(room, r, c - 1);
        boolean edgeRight = !isPit(room, r, c + 1);
        boolean edgeBottom = !isPit(room, r + 1, c);
        int[] strata = {0x5A4E66, 0x4A3F55, 0x3C3247, 0x453A50, 0x342B3E, 0x2A2333, 0x221C2A};

        for (int ly = 0; ly < T; ly++) {
            for (int lx = 0; lx < T; lx++) {
                int gx = c * T + lx;
                int gy = r * T + ly;
                int col;
                if (edgeTop && ly < 14) {
                    // Tebing batu yang terlihat di sisi atas lubang
                    int band = Math.min(strata.length - 1, ly / 2 + (int) (rnd(gx / 3, ly, 17) * 1.5));
                    col = shade(strata[band], (int) ((rnd(gx, gy, 19) - 0.5) * 10));
                    col = mix(col, 0x07060A, ly / 26.0);
                    if (ly < 2) col = 0x6A5D78; // bibir lantai
                } else {
                    double depth = edgeTop ? (ly - 14) / 40.0 : 0.6;
                    col = mix(0x0E0B14, 0x040305, Math.min(1, depth + 0.3));
                    if (rnd(gx, gy, 23) < 0.006) col = 0x2A1F40;
                }
                if (edgeLeft && lx < 3) col = mix(col, 0x000000, 0.5);
                if (edgeRight && lx > T - 4) col = mix(col, 0x000000, 0.5);
                if (edgeBottom && ly > T - 3) col = ly == T - 1 ? 0x4A4053 : 0x3A3044;
                set(px, w, gx, gy, col);
            }
        }
    }

    // ---------- pintu ----------
    private static void paintDoor(int[] px, int w, int r, int c) {
        if (c < 0) return;
        for (int ly = 0; ly < T; ly++) {
            for (int lx = 0; lx < T; lx++) {
                int gx = c * T + lx;
                int gy = r * T + ly;
                int col;
                if (lx < 8) {
                    col = 0x241E2B;
                } else if (lx < 11 || ly < 2 || ly > T - 3) {
                    col = 0x5C4F69; // kusen batu
                } else {
                    col = shade(0x7A4A26, (int) ((rnd(gx, gy, 29) - 0.5) * 14));
                    if ((ly - 2) % 7 == 6) col = 0x4E2E16; // sambungan papan
                    if (lx >= 15 && lx <= 17 && (ly == 8 || ly == 23)) col = 0x9A98A8; // paku
                    if ((ly >= 7 && ly <= 9) || (ly >= 22 && ly <= 24)) {
                        col = lx % 6 == 0 ? 0x9A98A8 : 0x3C3A46; // besi
                    }
                    if (lx == 13 && ly >= 14 && ly <= 17) col = 0xF2B33D; // gagang
                }
                set(px, w, gx, gy, col);
            }
        }
    }

    // ---------- bantuan ----------
    private static boolean isWall(Room room, int r, int c) {
        if (r < 0 || c < 0 || r >= room.getRows() || c >= room.getCols()) return true;
        return room.getTile(r, c) == Tile.WALL;
    }

    private static boolean isPit(Room room, int r, int c) {
        if (r < 0 || c < 0 || r >= room.getRows() || c >= room.getCols()) return false;
        return room.getTile(r, c) == Tile.PIT;
    }

    private static void set(int[] px, int w, int x, int y, int rgb) {
        px[y * w + x] = 0xFF000000 | rgb;
    }

    private static double rnd(int x, int y, int seed) {
        int h = x * 374761393 + y * 668265263 + seed * 982451653;
        h = (h ^ (h >>> 13)) * 1274126177;
        h ^= h >>> 16;
        return (h & 0x7FFFFFFF) / (double) 0x7FFFFFFF;
    }

    private static int shade(int rgb, int d) {
        int r = clamp(((rgb >> 16) & 0xFF) + d);
        int g = clamp(((rgb >> 8) & 0xFF) + d);
        int b = clamp((rgb & 0xFF) + d);
        return (r << 16) | (g << 8) | b;
    }

    private static int mix(int a, int b, double t) {
        t = Math.max(0, Math.min(1, t));
        int r = (int) (((a >> 16) & 0xFF) * (1 - t) + ((b >> 16) & 0xFF) * t);
        int g = (int) (((a >> 8) & 0xFF) * (1 - t) + ((b >> 8) & 0xFF) * t);
        int bl = (int) ((a & 0xFF) * (1 - t) + (b & 0xFF) * t);
        return (r << 16) | (g << 8) | bl;
    }

    private static int clamp(int v) {
        return Math.max(0, Math.min(255, v));
    }
}
