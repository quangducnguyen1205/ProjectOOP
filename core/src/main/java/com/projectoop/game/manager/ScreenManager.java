package com.projectoop.game.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenManager {

    private Game game;
    private Screen currentScreen;

    public ScreenManager(Game game) {
        this.game = game;
    }

    // Chuyển màn mới, nhưng không dispose ngay lập tức
    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.hide();  // Ẩn màn hiện tại
        }

        currentScreen = screen;  // Lưu màn mới
        game.setScreen(currentScreen);  // Chuyển sang màn mới
        currentScreen.show();  // Hiển thị màn mới
    }

    // Dispose tài nguyên của màn cũ khi không còn cần thiết
    public void dispose() {
        if (currentScreen != null) {
            currentScreen.hide();  // Ẩn màn hiện tại trước khi dispose
            currentScreen.dispose();  // Dispose tài nguyên của màn cũ
            currentScreen = null;  // Set null để tránh truy cập sai
        }
    }

}

