package com.projectoop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.projectoop.manager.GameManager;
import com.projectoop.ui.UIManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    @Override
    public void create() {
        // Khởi tạo GameManager
        GameManager.getInstance(GameManager.class).initialize(this);

        // Đảm bảo Stage nhận sự kiện nhập liệu
        Gdx.input.setInputProcessor(UIManager.getInstance(UIManager.class).getStage());
    }

    @Override
    public void render() {
        super.render();
        // GameManager xử lý logic bổ sung nếu cần
        GameManager.getInstance(GameManager.class).update();
    }

    @Override
    public void dispose() {
        super.dispose();
        // Giải phóng tài nguyên từ GameManager
        GameManager.getInstance(GameManager.class).dispose();
    }
}
