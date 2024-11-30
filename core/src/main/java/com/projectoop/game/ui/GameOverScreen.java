package com.projectoop.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.projectoop.game.GameWorld;
import com.projectoop.game.manager.GameManager;
import com.projectoop.game.screens.SecondMapScreen;
import com.projectoop.game.sprites.PlayerData;

public class GameOverScreen extends UICanvas {

    private Skin skin;

    public GameOverScreen() {
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

        // Create labels
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        Label gameOverLabel = new Label("GAME OVER", font);
        Label playAgainLabel = new Label("Click to Play Again", font);

        // Create table layout
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        // Add table to stage
        this.addActor(table);

        // Add listener for the table to handle click event (Play Again)
        table.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handlePlayAgain();
            }
        });
    }

    // Handle Play Again action
    private void handlePlayAgain() {

    }
}
