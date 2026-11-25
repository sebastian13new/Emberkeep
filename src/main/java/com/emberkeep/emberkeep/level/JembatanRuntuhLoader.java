package com.emberkeep.emberkeep.level;

/** Ruang 2: Jembatan Runtuh, berisi lubang dan monster. */
public class JembatanRuntuhLoader extends RoomLoader {

    @Override
    protected String getName() {
        return "Ruang 2 - Jembatan Runtuh";
    }

    @Override
    protected String[] buildLayout() {
        return new String[] {
                "##############################",
                "##############################",
                "#............OOOO............#",
                "#..p.........OOOO.......c....#",
                "#............OOOO............#",
                "#............................#",
                "#............................#",
                "#....OOO.....OOOO.....OOO....#",
                "#....OOO.....OOOO.....OOO....#",
                "#S.................p.........#",
                "#............................#",
                "#....OOO.....OOOO.....OOO....#",
                "#....OOO.....OOOO.....OOO....#",
                "#............................#",
                "#............................#",
                "#............OOOO............#",
                "#..c.........OOOO.......p....#",
                "#............OOOO............#",
                "#............................#",
                "##############################"
        };
    }
}
