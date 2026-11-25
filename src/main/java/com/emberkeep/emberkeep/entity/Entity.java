package com.emberkeep.emberkeep.entity;

import com.emberkeep.emberkeep.level.Room;
import javafx.scene.canvas.GraphicsContext;

/**
 * Induk semua objek yang bergerak di game. Player dan Monster
 * menurunkan kelas ini dan mengisi update() serta render() sendiri.
 */
public abstract class Entity {
    protected double x;
    protected double y;
    protected final double width;
    protected final double height;
    protected boolean alive = true;

    protected Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update(double dt, Room room);

    public abstract void render(GraphicsContext gc);

    public Bounds getBounds() {
        return new Bounds(x, y, width, height);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double centerX() { return x + width / 2; }
    public double centerY() { return y + height / 2; }
    public boolean isAlive() { return alive; }
}
