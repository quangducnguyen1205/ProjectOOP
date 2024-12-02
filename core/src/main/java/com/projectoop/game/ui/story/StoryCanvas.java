package com.projectoop.game.ui.story;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.projectoop.game.manager.GameManager;
import com.projectoop.game.screens.FirstMapScreen;
import com.projectoop.game.screens.FourthMapScreen;
import com.projectoop.game.screens.SecondMapScreen;
import com.projectoop.game.screens.ThirdMapScreen;
import com.projectoop.game.tools.AudioManager;
import com.projectoop.game.ui.MainMenuCanvas;
import com.projectoop.game.ui.UICanvas;
import com.projectoop.game.ui.UIManager;

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
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
                // Đánh dấu màn này đã được chơi lần đầu
                GameManager.getInstance(GameManager.class)
                    .getPlayerData()
                    .addFirstTimePlayedLevel(levelName);


                closeDirectly();
                GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(levelName);

                // Chuyển đến màn chơi tương ứng dựa trên levelName
                if (levelName.equals("level1")) {
                    GameManager.getInstance(GameManager.class).setScreen(new FirstMapScreen(GameManager.getInstance(GameManager.class).getGameWorld())); // Chuyển đến FirstScreen
                } else if (levelName.equals("level2")) {
                    GameManager.getInstance(GameManager.class).setScreen(new SecondMapScreen(GameManager.getInstance(GameManager.class).getGameWorld())); // Chuyển đến SecondScreen
                } else if (levelName.equals("level3")) {
                    GameManager.getInstance(GameManager.class).setScreen(new ThirdMapScreen(GameManager.getInstance(GameManager.class).getGameWorld())); // Chuyển đến ThirdScreen (nếu có)
                } else if (levelName.equals("level4")) {
                    GameManager.getInstance(GameManager.class).setScreen(new FourthMapScreen(GameManager.getInstance(GameManager.class).getGameWorld())); // Chuyển đến FourthScreen (nếu có)
                }
            }
        });

        // Exit Button
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
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
