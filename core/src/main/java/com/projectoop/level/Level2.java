package com.projectoop.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.Player;
import com.projectoop.utils.Constants;

public class Level2 extends Level {

    // Constructor cho Level1
    public Level2(String name, World world) {
        super(name, world);
    }

    @Override
    public void onInit() {
        // Thiết lập MapBuilder với đường dẫn tới file map
        setupMap("ProjectOOP/OOP_project_draft-main/assets/" + levelName + ".tmx");

        if (mapBuilder != null && map != null) {
            // Khởi tạo player và thiết lập vị trí ban đầu
            player = new Player(world, new Vector2(1200f, 160f), 1.2f, 5f / Constants.PPM);
            player.onInit();

            createStaticBodies();

            // Log vị trí của player để kiểm tra khởi tạo
            System.out.println("Player initialized at position: " + player.getPosition());
        } else {
            System.err.println("Failed to initialize Level1 due to map loading issues.");
        }
    }

    // Phương thức tạo tất cả các body tĩnh từ RectangleMapObject trong map
    private void createStaticBodies() {
        System.out.println("Creating static bodies for Level...");

        // Sử dụng createStaticBodies để tự động tạo body từ tất cả RectangleMapObject trong map
        mapBuilder.createStaticBodies();

        System.out.println("Static bodies created for all layers in the map.");
    }
}
