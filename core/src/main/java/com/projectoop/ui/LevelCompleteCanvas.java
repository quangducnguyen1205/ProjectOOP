package com.projectoop.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.projectoop.game.PlayerData;
import com.projectoop.manager.GameManager;
import com.projectoop.ui.story.*;

import java.util.HashMap;
import java.util.Map;

public class LevelCompleteCanvas extends UICanvas {

    private Skin skin;
    // Bản đồ để ánh xạ màn chơi với UI cốt truyện
    private final Map<String, Class<? extends UICanvas>> storyCanvasMap = new HashMap<>();

    public LevelCompleteCanvas() {
        super();
        initCanvas();
        initStoryCanvasMap();
    }
    private void initStoryCanvasMap() {
        storyCanvasMap.put("level1", StoryLevel1.class);
        storyCanvasMap.put("level2", StoryLevel2.class);
        storyCanvasMap.put("level3", StoryLevel3.class);
        storyCanvasMap.put("level4", StoryLevel4.class);
    }

    private void initCanvas() {
        // Load skin
        try {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        } catch (Exception e) {
            throw new RuntimeException("Skin file not found or invalid: skin/uiskin.json", e);
        }

        // Đặt Background
        Image backgroundImage = new Image(new Texture("assets/UI/LevelComplete_BG.png"));
        backgroundImage.setFillParent(true);
        this.addActor(backgroundImage);

        // Buttons
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleContinue();
            }
        });

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
        layout.bottom(); // Đặt nút ở phía dưới
        layout.add(continueButton).width(200).height(50).padRight(20);
        layout.add(exitButton).width(200).height(50);

        this.addActor(layout);
    }

    // Xử lý khi bấm Continue
    private void handleContinue() {
        PlayerData playerData = GameManager.getInstance(GameManager.class).getPlayerData();
        String currentLevel = playerData.getCurrentLevel();

        // Đánh dấu màn hiện tại là đã hoàn thành
        playerData.addCompletedLevel(currentLevel);

        // Xác định màn tiếp theo
        int currentLevelIndex = Integer.parseInt(currentLevel.replace("level", ""));
        String nextLevel = "level" + (currentLevelIndex + 1);

        // Chuyển sang màn tiếp theo
        closeDirectly();
        UIManager.getInstance(UIManager.class).openUI(storyCanvasMap.get(nextLevel));
    }

    // Xử lý khi bấm Exit
    private void handleExit() {
        closeDirectly();
        UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class);
    }
}
