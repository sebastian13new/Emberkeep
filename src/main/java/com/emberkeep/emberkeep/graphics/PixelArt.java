package com.emberkeep.emberkeep.graphics;

import java.util.HashMap;
import java.util.Map;

/** Fungsi bantu untuk mengubah grid huruf menjadi data piksel ARGB. */
public final class PixelArt {
    private PixelArt() { }

    public static Map<Character, Integer> palette(Object... pairs) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            map.put((Character) pairs[i], (Integer) pairs[i + 1]);
        }
        return map;
    }

    public static int width(String[] rows) {
        return rows[0].length();
    }

    public static int[] fromGrid(String[] rows, Map<Character, Integer> palette) {
        int w = width(rows);
        int h = rows.length;
        int[] px = new int[w * h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                char ch = rows[y].charAt(x);
                Integer color = palette.get(ch);
                px[y * w + x] = (ch == '.' || color == null) ? 0 : color;
            }
        }
        return px;
    }

    /** Versi putih dari sprite, dipakai untuk efek kedip saat kena serang. */
    public static int[] whiten(int[] px) {
        int[] out = new int[px.length];
        for (int i = 0; i < px.length; i++) {
            out[i] = (px[i] >>> 24) == 0 ? 0 : 0xFFFFF8F0;
        }
        return out;
    }

    public static String[] flip(String[] rows) {
        String[] out = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            out[i] = new StringBuilder(rows[i]).reverse().toString();
        }
        return out;
    }

    public static String[] concat(String[]... parts) {
        int total = 0;
        for (String[] p : parts) total += p.length;
        String[] out = new String[total];
        int i = 0;
        for (String[] p : parts) {
            for (String row : p) out[i++] = row;
        }
        return out;
    }
}
