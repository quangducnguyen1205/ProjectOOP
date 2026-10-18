package com.projectoop.game;

import com.projectoop.manager.GameManager;
import com.projectoop.ui.MainMenuCanvas;
import com.projectoop.ui.UIManager;

public class FirstScreen extends BaseScreen {

    @Override
    protected void onLevelCompleted() {
        String completedLevel = levelManager.getCurrentLevel().getLevelName();
        GameManager.getInstance(GameManager.class).getPlayerData().addCompletedLevel(completedLevel);

        int nextLevelIndex = levelManager.getNextLevelIndex(completedLevel);
        if (nextLevelIndex != -1) {
            String nextLevelName = levelManager.getCurrentLevel().getLevelName();
            GameManager.getInstance(GameManager.class).getPlayerData().setCurrentLevel(nextLevelName);
            updateLevel();
        } else {
            System.out.println("All levels completed!");
            UIManager.getInstance(UIManager.class).openUI(MainMenuCanvas.class);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    protected void updateGame(float delta) {
        super.updateGame(delta);
    }

    @Override
    protected void renderGame() {
        super.renderGame();
    }
}
