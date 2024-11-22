package com.projectoop.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.Player;
import com.projectoop.utils.Constants;

public class Level1 extends Level {

    // Constructor cho Level1
    public Level1(String name, World world) {
        super(name, world);
    }

    @Override
    public void onInit() {
        // Thiết lập MapBuilder với đường dẫn tới file map
        setupMap("SuperMario/android/assets/" + levelName + ".tmx");

        if (mapBuilder != null && map != null) {
            // Khởi tạo player và thiết lập vị trí ban đầu
            player = new Player(world, new Vector2(60f, 26f), 1.2f, 5f / Constants.PPM);
            player.onInit();

            createStaticBodies();

            // Log vị trí của player để kiểm tra khởi tạo
            System.out.println("Player initialized at position: " + player.getPosition());
        } else {
            System.err.println("Failed to initialize Level1 due to map loading issues.");
        }
    }

}
