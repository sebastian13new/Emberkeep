package com.emberkeep.emberkeep.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Menyimpan dan membaca high score dari file CSV.
 * Kalau file belum ada atau ada baris yang rusak, program TIDAK crash.
 */
public class HighScoreRepository {
    private final Path filePath;

    // Lokasi file dikirim dari luar (Dependency Injection), supaya gampang dites
    public HighScoreRepository(Path filePath) {
        this.filePath = filePath;
    }

    /** Baca semua skor dari file. Baris rusak dilewati. */
    public List<HighScoreEntry> loadAll() {
        List<HighScoreEntry> result = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return result; // file belum ada -> anggap belum ada skor
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) continue;
                try {
                    result.add(HighScoreEntry.fromCsvLine(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("[HighScore] Baris rusak dilewati: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("[HighScore] Gagal membaca file: " + e.getMessage());
        }
        return result;
    }

    /** Tambah satu skor baru, lalu simpan ke file. */
    public void save(HighScoreEntry entry) {
        List<HighScoreEntry> all = loadAll();
        all.add(entry);

        List<String> lines = new ArrayList<>();
        for (HighScoreEntry e : all) {
            lines.add(e.toCsvLine());
        }

        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("[HighScore] Gagal menyimpan file: " + e.getMessage());
        }
    }

    /** Ambil N skor tertinggi. Kalau skornya sama, yang waktunya lebih cepat menang. */
    public List<HighScoreEntry> getTopScores(int limit) {
        List<HighScoreEntry> all = loadAll();
        all.sort(Comparator.comparingInt(HighScoreEntry::getScore).reversed()
                .thenComparingInt(HighScoreEntry::getTimeSeconds));
        return all.subList(0, Math.min(limit, all.size()));
    }
}