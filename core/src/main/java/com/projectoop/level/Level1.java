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
            // Tạo các body tĩnh cho các lớp trong map (mặt đất, vật cản, ...)
            createStaticBodies();

            // Khởi tạo player và thiết lập vị trí ban đầu
            player = new Player(world, new Vector2(32f, 100f), 5f / Constants.PPM, 5f / Constants.PPM);
            player.onInit();
        } else {
            System.err.println("Failed to initialize Level1 due to map loading issues.");
        }
    }

    // Phương thức để tạo các body tĩnh từ các layer của bản đồ
    private void createStaticBodies() {
        System.out.println("Creating static bodies for Level1...");

        mapBuilder.createStaticBodiesFromLayer("Ground");
        System.out.println("Created Ground layer.");

        mapBuilder.createStaticBodiesFromLayer("Pipes");
        System.out.println("Created Pipes layer.");

        mapBuilder.createStaticBodiesFromLayer("Bricks");
        System.out.println("Created Bricks layer.");

        mapBuilder.createStaticBodiesFromLayer("Coins");
        System.out.println("Created Coins layer.");
    }

}
