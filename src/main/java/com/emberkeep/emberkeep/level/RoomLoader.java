package com.emberkeep.emberkeep.level;

/**
 * Template Method: urutan memuat ruangan selalu sama (bangun peta, buat Room,
 * pasang objek tambahan). Tiap ruangan cukup mengisi langkah yang berbeda.
 */
public abstract class RoomLoader {

    public final Room load() {
        String[] map = buildLayout();
        Room room = new Room(getName(), map);
        placeObjects(room);
        return room;
    }

    protected abstract String getName();

    protected abstract String[] buildLayout();

    /** Hook: ruangan yang punya peti, obor, atau cermin mengisinya di sini. */
    protected void placeObjects(Room room) { }
}
