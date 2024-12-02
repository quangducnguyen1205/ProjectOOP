package com.projectoop.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.projectoop.game.manager.GameManager;
import com.projectoop.game.screens.FirstMapScreen;
import com.projectoop.game.screens.SecondMapScreen;
import com.projectoop.game.screens.ThirdMapScreen;
import com.projectoop.game.screens.FourthMapScreen;
import com.projectoop.game.sprites.PlayerData;
import com.projectoop.game.tools.AudioManager;

public class GameOverCanvas extends UICanvas {

    private Skin skin;

    public GameOverCanvas() {
        super();
        initCanvas();
    }

    private void initCanvas() {
        // Load skin (if any) or use default styles
        try {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        } catch (Exception e) {
            throw new RuntimeException("Skin file not found or invalid: skin/uiskin.json", e);
        }

        // Set background image
        Image backgroundImage = new Image(new Texture("assets/UI/GameOver_BG.png"));
        backgroundImage.setFillParent(true);
        this.addActor(backgroundImage);

        // Buttons
        TextButton playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
                handlePlayAgain();
            }
        });

        TextButton exitButton = new TextButton("Exit to Main Menu", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
                handleExit();
            }
        });

        // Layout for buttons (centered and with appropriate padding)
        Table layout = new Table();
        layout.setFillParent(true);
        layout.top().padTop(150);  // Đặt khoảng cách từ đỉnh để không quá sát với đỉnh của màn hình

        // Tạo một bảng con chứa các nút và căn giữa
        Table buttonsTable = new Table();
        buttonsTable.center();  // Căn giữa bảng con

        // Đặt các nút và thêm khoảng cách
        buttonsTable.row().padBottom(20); // Đặt khoảng cách giữa các nút
        buttonsTable.add(playAgainButton).width(200).height(50).padBottom(10).row();
        buttonsTable.add(exitButton).width(200).height(50).padTop(10);

        // Thêm bảng chứa các nút vào layout chính
        layout.add(buttonsTable).expandY(); // Expand để bảng chiếm không gian còn lại

        // Thêm vào Actor
        this.addActor(layout);
    }


    // Handle Play Again action (destroy current screen and load the current level again)
    private void handlePlayAgain() {
        PlayerData playerData = GameManager.getInstance(GameManager.class).getPlayerData();
        String currentLevel = playerData.getCurrentLevel();

        // Reload the level based on current level
        if (currentLevel.equals("level1")) {
            GameManager.getInstance(GameManager.class).setScreen(new FirstMapScreen(GameManager.getInstance(GameManager.class).getGameWorld()));
        } else if (currentLevel.equals("level2")) {
            GameManager.getInstance(GameManager.class).setScreen(new SecondMapScreen(GameManager.getInstance(GameManager.class).getGameWorld()));
        } else if (currentLevel.equals("level3")) {
            GameManager.getInstance(GameManager.class).setScreen(new ThirdMapScreen(GameManager.getInstance(GameManager.class).getGameWorld()));
        } else if (currentLevel.equals("level4")) {
            GameManager.getInstance(GameManager.class).setScreen(new FourthMapScreen(GameManager.getInstance(GameManager.class).getGameWorld()));
        }

        closeDirectly();
    }

    // Handle Exit to Main Menu action
    private void handleExit() {
        handlePlayAgain();
        UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class);
    }
}
