package com.projectoop.ui.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.projectoop.game.FirstScreen;
import com.projectoop.game.FourthScreen;
import com.projectoop.game.SecondScreen;
import com.projectoop.game.ThirdScreen;
import com.projectoop.manager.GameManager;
import com.projectoop.ui.MainMenuCanvas;
import com.projectoop.ui.UICanvas;
import com.projectoop.ui.UIManager;

public class StoryCanvas extends UICanvas {

    private Skin skin;
    private String levelName;

    public StoryCanvas(String levelName) {
        super();
        this.levelName = levelName;
        initStoryCanvas();
    }

    private void initStoryCanvas() {
        // Load skin
        try {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        } catch (Exception e) {
            throw new RuntimeException("Skin file not found or invalid: skin/uiskin.json", e);
        }

        // Background image
        Image backgroundImage = new Image(new Texture("assets/UI/BG_" + levelName + ".png"));
        backgroundImage.setFillParent(true);
        this.addActor(backgroundImage);

        // Continue Button
        TextButton continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Đánh dấu màn này đã được chơi lần đầu
                GameManager.getInstance(GameManager.class)
                    .getPlayerData()
                    .addFirstTimePlayedLevel(levelName);


                closeDirectly();
                GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(levelName);

                // Chuyển đến màn chơi tương ứng dựa trên levelName
                if (levelName.equals("level1")) {
                    GameManager.getInstance(GameManager.class).setScreen(new FirstScreen()); // Chuyển đến FirstScreen
                } else if (levelName.equals("level2")) {
                    GameManager.getInstance(GameManager.class).setScreen(new SecondScreen()); // Chuyển đến SecondScreen
                } else if (levelName.equals("level3")) {
                    GameManager.getInstance(GameManager.class).setScreen(new ThirdScreen()); // Chuyển đến ThirdScreen (nếu có)
                } else if (levelName.equals("level4")) {
                    GameManager.getInstance(GameManager.class).setScreen(new FourthScreen()); // Chuyển đến FourthScreen (nếu có)
                }
            }
        });

        // Exit Button
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeDirectly();
                UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class);
            }
        });

        // Layout Table
        Table layout = new Table();
        layout.setFillParent(true);
        layout.bottom();
        layout.add(continueButton).width(150).height(50).padRight(20);
        layout.add(exitButton).width(150).height(50);

        this.addActor(layout);
    }
}
