package com.emberkeep.emberkeep.level;

import com.emberkeep.emberkeep.entity.Entity;
import com.emberkeep.emberkeep.entity.MonsterType;
import com.emberkeep.emberkeep.graphics.PixelSprite;
import com.emberkeep.emberkeep.graphics.RoomPainter;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Satu ruangan berbentuk grid. Peta ditulis sebagai teks:
 * '#' dinding, '.' lantai, 'O' lubang, 'S' titik masuk pemain,
 * 'p' monster patroli, 'c' monster pengejar.
 */
public class Room {
    public static final int TILE = 32;

    public record MonsterSpawn(MonsterType type, double x, double y) { }

    private final String name;
    private final Tile[][] tiles;
    private final int cols;
    private final int rows;
    private final List<MonsterSpawn> monsterSpawns = new ArrayList<>();
    private double spawnX;
    private double spawnY;
    private int spawnCol;
    private int spawnRow;
    private Image background;

    public Room(String name, String[] map) {
        this.name = name;
        this.rows = map.length;
        this.cols = map[0].length();
        this.tiles = new Tile[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = map[r].charAt(c);
                double px = c * TILE + 3;
                double py = r * TILE + 3;
                switch (ch) {
                    case '#' -> tiles[r][c] = Tile.WALL;
                    case 'O' -> tiles[r][c] = Tile.PIT;
                    case 'S' -> {
                        tiles[r][c] = Tile.FLOOR;
                        spawnX = px;
                        spawnY = py;
                        spawnCol = c;
                        spawnRow = r;
                    }
                    case 'p' -> {
                        tiles[r][c] = Tile.FLOOR;
                        monsterSpawns.add(new MonsterSpawn(MonsterType.PATROL, px, py));
                    }
                    case 'c' -> {
                        tiles[r][c] = Tile.FLOOR;
                        monsterSpawns.add(new MonsterSpawn(MonsterType.CHASE, px, py));
                    }
                    default -> tiles[r][c] = Tile.FLOOR;
                }
            }
        }
    }

    public Tile tileAt(double px, double py) {
        int c = (int) Math.floor(px / TILE);
        int r = (int) Math.floor(py / TILE);
        if (r < 0 || r >= rows || c < 0 || c >= cols) return Tile.WALL;
        return tiles[r][c];
    }

    public boolean isPitAt(double px, double py) {
        return tileAt(px, py) == Tile.PIT;
    }

    private boolean isBlocked(double x, double y, double w, double h, boolean pitsSolid) {
        // Cek sudut dan titik tengah sisi kotak entity
        double[] xs = {x, x + w / 2, x + w - 0.01};
        double[] ys = {y, y + h / 2, y + h - 0.01};
        for (double cx : xs) {
            for (double cy : ys) {
                Tile t = tileAt(cx, cy);
                if (t == Tile.WALL || (pitsSolid && t == Tile.PIT)) return true;
            }
        }
        return false;
    }

    /**
     * Gerakkan entity sambil mengecek dinding. Sumbu X dan Y dicek terpisah
     * supaya entity bisa "meluncur" di sepanjang dinding.
     * @return true kalau gerakan terhalang.
     */
    public boolean moveWithCollision(Entity e, double dx, double dy, boolean pitsSolid) {
        boolean blocked = false;
        double nx = e.getX() + dx;
        if (!isBlocked(nx, e.getY(), e.getWidth(), e.getHeight(), pitsSolid)) {
            e.setPosition(nx, e.getY());
        } else if (dx != 0) {
            blocked = true;
        }
        double ny = e.getY() + dy;
        if (!isBlocked(e.getX(), ny, e.getWidth(), e.getHeight(), pitsSolid)) {
            e.setPosition(e.getX(), ny);
        } else if (dy != 0) {
            blocked = true;
        }
        return blocked;
    }

    /** Latar ruangan digambar sekali oleh RoomPainter, lalu dipakai ulang. */
    public void render(GraphicsContext gc) {
        if (background == null) {
            background = PixelSprite.toImage(RoomPainter.paint(this), cols * TILE, rows * TILE);
        }
        gc.drawImage(background, 0, 0);
    }

    public Tile getTile(int r, int c) { return tiles[r][c]; }
    public int getCols() { return cols; }
    public int getRows() { return rows; }
    public int getSpawnCol() { return spawnCol; }
    public int getSpawnRow() { return spawnRow; }
    public String getName() { return name; }
    public double getSpawnX() { return spawnX; }
    public double getSpawnY() { return spawnY; }
    public List<MonsterSpawn> getMonsterSpawns() { return monsterSpawns; }
}
