package com.emberkeep.emberkeep.input;

import com.emberkeep.emberkeep.entity.Player;

public class DashCommand implements Command {
    @Override
    public void execute(Player player) {
        player.dash();
    }
}
