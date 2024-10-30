package com.projectoop.game;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    @Override
    public void create() {
        setScreen(new PlayScreen());  // Thiết lập màn hình đầu tiên là FirstScreen
    }
}
