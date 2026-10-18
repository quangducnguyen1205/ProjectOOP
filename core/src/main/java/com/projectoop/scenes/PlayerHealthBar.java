package com.projectoop.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectoop.game.BaseScreen;
import com.projectoop.utils.Constants;

public class PlayerHealthBar {
    private Texture HeroHealthBar, HeroHealth;
    private BaseScreen screen;
    private SpriteBatch batch;
    private float divide = 5f;

    public PlayerHealthBar(BaseScreen x) {
        // Load textures for health bar
        HeroHealthBar = new Texture("HealthBar/bg.png");
        HeroHealth = new Texture("HealthBar/red.png");

        // Get batch and screen
        this.screen = x;
        batch = x.batch; // Assuming batch is properly initialized in BaseScreen
    }

    public void draw() {
        // Ensure Player is not null
        if (screen.getPlayer() == null) return;

        // Get position of the camera and calculate HealthBar position
        float x = screen.getCamera().position.x - screen.getGamePort().getWorldWidth() / 2 + 10 / Constants.PPM; // Adjusted position on the left
        float y = screen.getCamera().position.y + screen.getGamePort().getWorldHeight() / 2 - 50 / Constants.PPM; // Adjusted vertical position

        // Draw the background of the health bar
        batch.draw(HeroHealthBar, x, y, HeroHealthBar.getWidth() / Constants.PPM / divide, HeroHealthBar.getHeight() / Constants.PPM / divide);

        // Calculate the ratio for health bar filling
        float ratio = screen.getPlayer().getCurrentHealth() / screen.getPlayer().getMaxHealth();
        float HealthX = x + (HeroHealthBar.getWidth()  - HeroHealth.getWidth()) / 2f / Constants.PPM;
        float HealthY = y + (HeroHealthBar.getHeight() - HeroHealth.getHeight()) / 2f / Constants.PPM;

        // Draw the health portion based on the ratio
        batch.draw(HeroHealth, HealthX, HealthY,
            (HeroHealth.getWidth() / Constants.PPM / divide) * ratio, HeroHealth.getHeight() / Constants.PPM / divide);
    }
}
