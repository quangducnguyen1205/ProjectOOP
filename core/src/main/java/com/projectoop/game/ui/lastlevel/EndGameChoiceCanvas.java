package com.projectoop.game.ui.lastlevel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import com.projectoop.game.tools.AudioManager;
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

        // Đặt Background
        Image backgroundImage = new Image(new Texture("assets/UI/BrosCrush_BG.png"));
        backgroundImage.setFillParent(true);
        this.addActor(backgroundImage);

        // Buttons
        TextButton brosButton = new TextButton("Bros", skin);
        brosButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
                handleBrosChoice();
            }
        });
        brosButton.getLabel().setFontScale(2); // Phóng to chữ của nút

        TextButton crushButton = new TextButton("Crush", skin);
        crushButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
                handleCrushChoice();
            }
        });
        crushButton.getLabel().setFontScale(2); // Phóng to chữ của nút

        // Layout
        Table layout = new Table();
        layout.setFillParent(true);
        layout.top(); // Đặt layout ở phía trên
        layout.top().padTop(160);

        // Cột đầu tiên cho "Bros" và cột thứ hai cho "Crush"
        layout.add(brosButton).width(500).height(140).padRight(80).padTop(150); // Nút "Bros"
        layout.add(crushButton).width(500).height(140).padLeft(80).padTop(150); // Nút "Crush"

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
    }
}
