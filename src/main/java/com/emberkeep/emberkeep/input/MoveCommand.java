package com.emberkeep.emberkeep.input;

import com.emberkeep.emberkeep.entity.Player;

public class MoveCommand implements Command {
    private final double dx;
    private final double dy;

    public MoveCommand(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute(Player player) {
        player.addMove(dx, dy);
    }
}
