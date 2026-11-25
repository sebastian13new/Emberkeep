package com.emberkeep.emberkeep.input;

import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Mencatat tombol yang ditekan, lalu mengubahnya menjadi daftar Command.
 * Kelas lain tidak perlu tahu tombol apa yang dipakai.
 */
public class InputHandler {
    private final Set<KeyCode> held = EnumSet.noneOf(KeyCode.class);
    private final Set<KeyCode> justPressed = EnumSet.noneOf(KeyCode.class);

    private final Command attack = new AttackCommand();
    private final Command dash = new DashCommand();

    public void press(KeyCode key) {
        if (held.add(key)) {
            justPressed.add(key);
        }
    }

    public void release(KeyCode key) {
        held.remove(key);
    }

    public boolean isHeld(KeyCode... keys) {
        for (KeyCode k : keys) {
            if (held.contains(k)) return true;
        }
        return false;
    }

    /** True hanya di frame saat tombol baru ditekan. */
    public boolean wasPressed(KeyCode... keys) {
        for (KeyCode k : keys) {
            if (justPressed.contains(k)) return true;
        }
        return false;
    }

    public void endFrame() {
        justPressed.clear();
    }

    public List<Command> collectCommands() {
        List<Command> commands = new ArrayList<>();

        double dx = 0;
        double dy = 0;
        if (isHeld(KeyCode.W, KeyCode.UP)) dy -= 1;
        if (isHeld(KeyCode.S, KeyCode.DOWN)) dy += 1;
        if (isHeld(KeyCode.A, KeyCode.LEFT)) dx -= 1;
        if (isHeld(KeyCode.D, KeyCode.RIGHT)) dx += 1;
        if (dx != 0 || dy != 0) {
            commands.add(new MoveCommand(dx, dy));
        }

        if (wasPressed(KeyCode.J, KeyCode.SPACE)) commands.add(attack);
        if (wasPressed(KeyCode.K, KeyCode.SHIFT)) commands.add(dash);
        return commands;
    }
}
