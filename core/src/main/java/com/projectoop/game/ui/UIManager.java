package com.projectoop.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.projectoop.game.manager.Singleton;
import com.projectoop.game.ui.story.StoryCanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UIManager extends Singleton<UIManager> {

    private Stage stage;
    private HashMap<Class<?>, UICanvas> uiCanvasMap = new HashMap<>();
    private List<UICanvas> backStack = new ArrayList<>();

    public UIManager() {
        // Khởi tạo Stage
        stage = new Stage(new ScreenViewport());
    }
    public Stage getStage() {
        if (stage == null) {
            stage = new Stage(new ScreenViewport());
        }
        return stage;
    }
    // Mở UI
    public <T extends UICanvas> T openUI(Class<T> clazz) {
        T canvas = getUI(clazz);
        canvas.setup();
        canvas.open();
        return canvas;
    }
    // Đóng UI
    public <T extends UICanvas> void closeUI(Class<T> clazz) {
        T canvas = getUI(clazz);
        if (canvas != null) {
            canvas.closeDirectly();
        }
    }
    // Lấy UI hiện tại hoặc tạo mới
    public <T extends UICanvas> T getUI(Class<T> clazz) {
        if (!uiCanvasMap.containsKey(clazz)) {
            try {
                // Tạo mới instance của UICanvas
                T canvas = clazz.getDeclaredConstructor().newInstance();
                uiCanvasMap.put(clazz, canvas);

                // Đảm bảo thêm vào Stage
                if (stage != null) {
                    stage.addActor(canvas);
                } else {
                    throw new IllegalStateException("Stage is not initialized in UIManager!");
                }
            } catch (Exception e) {
                throw new RuntimeException("Error creating UI: " + clazz.getName(), e);
            }
        }
        return clazz.cast(uiCanvasMap.get(clazz));
    }
    // Thêm vào BackStack
    public void addToBackStack(UICanvas canvas) {
        if (!backStack.contains(canvas)) {
            backStack.add(canvas);
        }
    }
    // Xóa khỏi BackStack
    public void removeFromBackStack(UICanvas canvas) {
        backStack.remove(canvas);
    }
    // Xử lý nút Back
    public void handleBackKey() {
        if (!backStack.isEmpty()) {
            UICanvas topCanvas = backStack.get(backStack.size() - 1);
            if (topCanvas != null) {
                topCanvas.backKey();
            }
        }
    }
    public void openStory(String levelName) {
        StoryCanvas storyCanvas = new StoryCanvas(levelName);
        getStage().addActor(storyCanvas);
    }

}
