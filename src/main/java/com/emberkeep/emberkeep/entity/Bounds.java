package com.emberkeep.emberkeep.entity;

/** Kotak tabrakan sederhana. */
public record Bounds(double x, double y, double width, double height) {
    public boolean intersects(Bounds o) {
        return x < o.x + o.width && x + width > o.x
                && y < o.y + o.height && y + height > o.y;
    }
}
