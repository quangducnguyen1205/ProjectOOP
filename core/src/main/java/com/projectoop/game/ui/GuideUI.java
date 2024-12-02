package com.projectoop.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.projectoop.game.tools.AudioManager;

public class GuideUI extends UICanvas {

    private Skin skin;

    public GuideUI() {
        super();
        initGuideUI();
    }

    private void initGuideUI() {
        // Load skin
        try {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        } catch (Exception e) {
            throw new RuntimeException("Skin file not found or invalid: skin/uiskin.json", e);
        }

        // Thêm background hướng dẫn
        Image backgroundImage = new Image(new Texture("UI/guide_background.png"));
        backgroundImage.setFillParent(true);
        this.addActor(backgroundImage);

        // Nút Exit quay lại
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(1f);

        // Bố cục UI
        Table layout = new Table();
        layout.setFillParent(true);
        layout.center();  // Căn giữa toàn bộ layout

        // Thêm nút Exit vào dưới cùng, căn giữa
        layout.row().expandY().padBottom(-700); // Đẩy nút xuống phía dưới
        layout.add(exitButton).colspan(2).align(Align.center).width(200).height(50); // Nút Exit căn giữa dưới

        this.addActor(layout);

        // Lắng nghe sự kiện nút Exit
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
                closeDirectly();
                UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class);
            }
        });
    }

}
