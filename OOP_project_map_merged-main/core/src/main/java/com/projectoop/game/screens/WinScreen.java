package com.projectoop.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.projectoop.game.GameWorld;

public class WinScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;
    private Texture winImage;

    public WinScreen(Game game){
        this.game = game;
        viewport = new FitViewport(GameWorld.V_WIDTH, GameWorld.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((GameWorld) game).batch);

        winImage = new Texture(Gdx.files.internal("winscreen.png"));

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Image image = new Image(winImage);

        table.add(image).expandX();
        table.row();
        //table.add(new Label("Click to Play Again", new Label.LabelStyle(new BitmapFont(), Color.WHITE))).expandX().padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new FourthMapScreen((GameWorld) game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        winImage.dispose();
    }
}
