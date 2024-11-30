package com.projectoop.game.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager {
    private Game game;
    private Screen currentScreen;

    public ScreenManager(Game game) {
        this.game = game;
    }

    public void setScreen(Screen screen) {
        dispose();
        currentScreen = screen;  // Lưu màn mới
        game.setScreen(currentScreen);  // Chuyển sang màn mới
        currentScreen.show();  // Hiển thị màn mới
    }

    public void dispose() {
        if (currentScreen != null) {
            currentScreen.dispose();  // Dispose tài nguyên của màn hiện tại
        }
    }
}

