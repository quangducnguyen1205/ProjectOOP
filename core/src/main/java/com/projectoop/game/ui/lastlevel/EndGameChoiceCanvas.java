package com.projectoop.game.ui.lastlevel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.projectoop.game.manager.GameManager;
import com.projectoop.game.screens.NullScreen;
import com.projectoop.game.ui.UICanvas;
import com.projectoop.game.ui.UIManager;


public class EndGameChoiceCanvas extends UICanvas {

    private Skin skin;

    public EndGameChoiceCanvas() {
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

        // Buttons
        TextButton brosButton = new TextButton("Bros", skin);
        brosButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleBrosChoice();
            }
        });

        TextButton crushButton = new TextButton("Crush", skin);
        crushButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleCrushChoice();
            }
        });

        // Layout
        Table layout = new Table();
        layout.setFillParent(true);
        layout.center(); // Đặt nút ở giữa
        layout.add(brosButton).width(200).height(50).padRight(20);
        layout.row().padTop(10); // Thêm khoảng cách
        layout.add(crushButton).width(200).height(50);

        this.addActor(layout);
    }

    // Xử lý khi chọn Bros
    private void handleBrosChoice() {
        closeDirectly();
        UIManager.getInstance(UIManager.class).openUI(BrosStoryCanvas.class);  // UI hiển thị về bạn bè
        // Khi chọn Bros, sẽ kết thúc màn chơi và chuyển sang UI cho bạn bè
        GameManager.getInstance(GameManager.class).setScreen(new NullScreen());
    }

    // Xử lý khi chọn Crush
    private void handleCrushChoice() {
        closeDirectly();
        GameManager.getInstance(GameManager.class).getLastScreen().moveKnightToSavePrincessPoint();
        //UIManager.getInstance(UIManager.class).removeFromBackStack(this);
    }

}
