package com.projectoop.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

public abstract class UICanvas extends Table {
    // Các thuộc tính tùy chỉnh
    protected boolean isDestroyOnClose = false;
    protected boolean isHandlingRabbitEars = false;
    protected boolean isWidescreenProcessing = false;
    private float offsetY = 0;
    // Danh sách popup con
    protected List<UICanvas> popups = new ArrayList<>();
    private UICanvas parentsPopup;
    public UICanvas() {
        // Cấu hình cơ bản cho Table (UI container)
        super();
        this.setFillParent(true);
        this.init();
    }
    // Khởi tạo
    protected void init() {
        // Lấy Stage từ UIManager
        Stage stage = UIManager.getInstance(UIManager.class).getStage();
        if (stage != null) {
            // Xử lý màn hình thỏ tai
            float ratio = (float) stage.getViewport().getScreenHeight() / (float) stage.getViewport().getScreenWidth();
            if (isHandlingRabbitEars && ratio > 2.1f) {
                this.padTop(100f);
                offsetY = 100f;
            }

            // Xử lý màn hình rộng
            if (isWidescreenProcessing) {
                ratio = (float) stage.getViewport().getScreenWidth() / (float) stage.getViewport().getScreenHeight();
                if (ratio < 2.1f) {
                    float defaultRatio = 850f / 1920f;
                    float currentRatio = ratio;

                    float scaleFactor = 1 - (currentRatio - defaultRatio);
                    this.setWidth(this.getWidth() * scaleFactor);
                }
            }
        } else {
            throw new IllegalStateException("Stage is not initialized in UIManager!");
        }
    }
    // Setup canvas (thêm vào BackStack)
    public void setup() {

        // Thêm vào BackStack trong UIManager
        UIManager.getInstance(UIManager.class).addToBackStack(this);
    }
    // Xử lý nút Back (Android)z
    public void backKey() {
        // Override nếu cần
    }
    // Mở canvas
    public void open() {
        this.setVisible(true);
    }
    // Đóng canvas ngay lập tức
    public void closeDirectly() {
        // Xóa khỏi BackStack
        UIManager.getInstance(UIManager.class).removeFromBackStack(this);
        this.setVisible(false);
        if (isDestroyOnClose) {
            this.remove();
        }
    }
    // Đóng canvas sau một khoảng thời gian trễ
    public void close(float delayTime) {
        this.addAction(Actions.sequence(
            Actions.delay(delayTime),
            Actions.run(this::closeDirectly)
        ));
    }
    // Popup
    public <T extends UICanvas> T getPopup(Class<T> clazz) {
        for (UICanvas popup : popups) {
            if (clazz.isInstance(popup)) {
                return clazz.cast(popup);
            }
        }
        return null;
    }
    public <T extends UICanvas> T openPopup(Class<T> clazz) {
        T popup = getPopup(clazz);
        if (popup != null) {
            popup.setup();
            popup.open();
        }
        return popup;
    }
    public <T extends UICanvas> boolean isOpenedPopup(Class<T> clazz) {
        T popup = getPopup(clazz);
        return popup != null && popup.isVisible();
    }
    public <T extends UICanvas> void closePopup(Class<T> clazz, float delayTime) {
        T popup = getPopup(clazz);
        if (popup != null) {
            popup.close(delayTime);
        }
    }
    public <T extends UICanvas> void closePopupDirect(Class<T> clazz) {
        T popup = getPopup(clazz);
        if (popup != null) {
            popup.closeDirectly();
        }
    }
    public void closeAllPopup() {
        for (UICanvas popup : popups) {
            popup.closeDirectly();
        }
    }
    // Getter và Setter cho ParentsPopup
    public UICanvas getParentsPopup() {
        return parentsPopup;
    }
    public void setParentsPopup(UICanvas parentsPopup) {
        this.parentsPopup = parentsPopup;
    }
}
