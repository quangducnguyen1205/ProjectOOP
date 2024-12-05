package com.projectoop.game.ui.description;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.projectoop.game.ui.MainMenuCanvas;
import com.projectoop.game.ui.UICanvas;
import com.projectoop.game.ui.UIManager;

public class DescriptionPage2 extends UICanvas {

    private Skin skin;

    public DescriptionPage2() {
        super();
        initPage2();
    }

    private void initPage2() {
        // Load skin
        try {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        } catch (Exception e) {
            throw new RuntimeException("Skin file not found or invalid: skin/uiskin.json", e);
        }

        // Background image
        Image backgroundImage = new Image(new Texture("assets/UI/description2_BG.png"));
        backgroundImage.setFillParent(true);
        this.addActor(backgroundImage);

        // Buttons
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Quay lại trang trước (DescriptionPage1)
                closeDirectly();
                UIManager.getInstance(UIManager.class).openUI(DescriptionPage1.class);
            }
        });

        TextButton nextButton = new TextButton("Next", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Quay lại trang đầu tiên (DescriptionPage1)
                closeDirectly();
                UIManager.getInstance(UIManager.class).openUI(DescriptionPage3.class);
            }
        });

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Quay về MainMenu
                closeDirectly();
                UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class);
            }
        });

        // Layout
        Table layout = new Table();
        layout.setFillParent(true);

        // Đặt các nút vào vị trí hợp lý
        layout.bottom(); // Đặt toàn bộ layout ở dưới cùng của màn hình
        layout.add(backButton).width(150).height(50).padRight(20); // Nút Back ở bên trái
        layout.add(nextButton).width(150).height(50).padRight(20); // Nút Next ở giữa
        layout.add(exitButton).width(150).height(50); // Nút Exit ở bên phải

        this.addActor(layout);
    }
}
