package com.projectoop.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.PlayScreen;
import com.projectoop.manager.GameManager;

public class MainMenuCanvas extends UICanvas {

    private Skin skin;

    public MainMenuCanvas() {
        super();
        initMainMenu();
    }

    private void initMainMenu() {
        // Load skin
        try {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        } catch (Exception e) {
            throw new RuntimeException("Skin file not found or invalid: skin/uiskin.json", e);
        }

        // Thêm background
        initBackground();

        // Tiêu đề game
        Label title = new Label("Bros||Crush", skin);
        title.setFontScale(1f);

        // SelectBox để chọn màn chơi
        Array<String> levelItems = new Array<>();
        levelItems.add("Level 1");
        levelItems.add("Level 2");
        levelItems.add("Level 3");
        levelItems.add("Level 4");

        SelectBox<String> levelSelector = new SelectBox<>(skin);
        levelSelector.setItems(levelItems);
        levelSelector.setWidth(120);
        levelSelector.getStyle().font.getData().setScale(0.6f);

        // Nút Play
        TextButton playButton = new TextButton("Play", skin);
        playButton.getLabel().setFontScale(0.6f);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selectedLevel = levelSelector.getSelected();
                int levelIndex = Integer.parseInt(selectedLevel.replace("Level ", "")) - 1;

                // Kiểm tra nếu cấp độ đã được mở khóa
                String levelName = "level" + (levelIndex + 1);
                if (GameManager.getInstance(GameManager.class).getPlayerData().isLevelCompleted(levelName) || levelIndex == 0) {
                    GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(levelName);
                    closeDirectly();
                    GameManager.getInstance(GameManager.class).setScreen(new PlayScreen());
                } else {
                    System.out.println("This level is locked!");
                }
            }
        });

        // Nút Description
        TextButton descriptionButton = new TextButton("Description", skin);
        descriptionButton.getLabel().setFontScale(0.6f);
        descriptionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Description button clicked!");
            }
        });

        // Nút Settings
        ImageButton settingsButton = new ImageButton(skin, "settings-icon");
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Settings button clicked!");
            }
        });

        // Bố cục UI
        Table layout = new Table();
        layout.setFillParent(true);
        layout.top();
        layout.add(title).expandX().center().padTop(40).padBottom(50).row();

        Table leftLayout = new Table();
        leftLayout.add(levelSelector).width(120).height(35).padBottom(15).left().row();
        leftLayout.add(playButton).width(120).height(35).left();

        Table rightLayout = new Table();
        rightLayout.add(descriptionButton).width(120).height(35).right();

        Table buttonContainer = new Table();
        buttonContainer.add(leftLayout).expandX().left().padLeft(50);
        buttonContainer.add(rightLayout).expandX().right().padRight(50);

        layout.add(buttonContainer).expand().fill().row();

        Table topRightLayout = new Table();
        topRightLayout.setFillParent(true);
        topRightLayout.top().right();
        topRightLayout.add(settingsButton).size(25, 25).pad(10);

        this.addActor(layout);
        this.addActor(topRightLayout);
    }

    private void initBackground() {
        Window backgroundWindow = new Window("", skin, "default");
        backgroundWindow.setFillParent(true);
        backgroundWindow.setMovable(false);
        this.addActor(backgroundWindow);
    }
}
