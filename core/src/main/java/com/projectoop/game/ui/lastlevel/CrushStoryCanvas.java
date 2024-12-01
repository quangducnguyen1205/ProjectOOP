package com.projectoop.game.ui.lastlevel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.projectoop.game.manager.GameManager;
import com.projectoop.game.screens.NullScreen;
import com.projectoop.game.ui.UICanvas;
import com.projectoop.game.ui.MainMenuCanvas;
import com.projectoop.game.ui.UIManager;

public class CrushStoryCanvas extends UICanvas {

    private Skin skin;

    public CrushStoryCanvas() {
        super();
        initCanvas();
    }

    private void initCanvas() {
        // Load skin
        try {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        } catch (Exception e) {
            throw new RuntimeException("Skin file not found or invalid: skin/uiskin.json", e);
        }

        // Đặt Background cho Crush
        Image backgroundImage = new Image(new Texture("assets/UI/CrushChoice_BG.png")); // Đường dẫn ảnh bạn đã chuẩn bị
        backgroundImage.setFillParent(true);
        this.addActor(backgroundImage);

        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleExit();
            }
        });

        // Layout
        Table layout = new Table();
        layout.setFillParent(true);
        layout.center(); // Đặt nút ở giữa
        layout.row().padTop(10); // Thêm khoảng cách
        layout.add(exitButton).width(200).height(50);

        this.addActor(layout);
    }

    // Xử lý khi bấm Exit
    private void handleExit() {
        closeDirectly();
        UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class); // Quay về MainMenu
    }
}
