package com.emberkeep.emberkeep.input;

import com.emberkeep.emberkeep.entity.Player;

/** Command pattern: satu aksi pemain dibungkus menjadi objek. */
public interface Command {
    void execute(Player player);
}
