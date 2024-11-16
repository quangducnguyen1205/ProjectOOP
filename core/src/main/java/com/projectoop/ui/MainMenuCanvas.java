package com.projectoop.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.PlayScreen;
import com.projectoop.game.PlayerData;
import com.projectoop.manager.GameManager;
import com.projectoop.ui.description.DescriptionPage1;
import com.projectoop.ui.description.DescriptionPage2;
import com.projectoop.ui.story.StoryLevel1;
import com.projectoop.ui.story.StoryLevel2;
import com.projectoop.ui.story.StoryLevel3;
import com.projectoop.ui.story.StoryLevel4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenuCanvas extends UICanvas {

    private Skin skin;

    // Danh sách các trang mô tả
    private final List<Class<? extends UICanvas>> descriptionPages = List.of(
        DescriptionPage1.class,
        DescriptionPage2.class
    );

    // Bản đồ để ánh xạ màn chơi với UI cốt truyện
    private final Map<String, Class<? extends UICanvas>> storyCanvasMap = new HashMap<>();

    public MainMenuCanvas() {
        super();
        initStoryCanvasMap(); // Khởi tạo ánh xạ màn chơi
        initMainMenu();
    }

    private void initStoryCanvasMap() {
        storyCanvasMap.put("level1", StoryLevel1.class);
        storyCanvasMap.put("level2", StoryLevel2.class);
        storyCanvasMap.put("level3", StoryLevel3.class);
        storyCanvasMap.put("level4", StoryLevel4.class);
    }

    private void initMainMenu() {
        // Load skin
        try {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        } catch (Exception e) {
            throw new RuntimeException("Skin file not found or invalid: skin/uiskin.json", e);
        }

        // Thêm background
        initBackground();

        // SelectBox để chọn màn chơi
        Array<String> levelItems = new Array<>();
        levelItems.add("Level 1");
        levelItems.add("Level 2");
        levelItems.add("Level 3");
        levelItems.add("Level 4");

        SelectBox<String> levelSelector = new SelectBox<>(skin);
        levelSelector.setItems(levelItems);
        levelSelector.setWidth(200);
        levelSelector.getStyle().font.getData().setScale(1f);
        levelSelector.setAlignment(Align.center);

        // Nút Play
        TextButton playButton = createPlayButton(levelSelector);

        // Nút Description
        TextButton descriptionButton = createDescriptionButton();

        // Nút Exit
        TextButton exitButton = createExitButton();

        // Bố cục UI
        Table layout = new Table();
        layout.setFillParent(true);
        layout.center();

        // Tạo hai nhóm nút
        Table upperButtons = new Table();
        upperButtons.add(levelSelector).width(200).height(50).padBottom(10).row();
        upperButtons.add(playButton).width(200).height(50);

        Table lowerButtons = new Table();
        lowerButtons.add(descriptionButton).width(200).height(50).padBottom(10).row();
        lowerButtons.add(exitButton).width(200).height(50);

        layout.add(upperButtons).padBottom(0).row(); // Đẩy nhóm nút trên lên
        layout.add(new Label("", skin, "default")).padBottom(110).row(); // Tên quán nằm giữa
        layout.add(lowerButtons).padBottom(-50).row(); // Đặt nhóm nút dưới cùng

        this.addActor(layout);
    }

    private TextButton createPlayButton(SelectBox<String> levelSelector) {
        TextButton playButton = new TextButton("Play", skin);
        playButton.getLabel().setFontScale(1f);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selectedLevel = levelSelector.getSelected();
                String levelName = "level" + selectedLevel.split(" ")[1];

                PlayerData playerData = GameManager.getInstance(GameManager.class).getPlayerData();

                // Kiểm tra nếu level khả dụng
                if (!playerData.isLevelAvailable(levelName)) {
                    System.out.println("Level " + levelName + " is not available!");
                    return; // Không làm gì nếu level không khả dụng
                }

                if (playerData.isFirstTimePlayed(levelName)) {
                    // Nếu lần đầu chơi, hiển thị cốt truyện
                    playerData.addFirstTimePlayedLevel(levelName); // Đánh dấu là đã chơi lần đầu
                    closeDirectly();
                    UIManager.getInstance(UIManager.class).openUI(storyCanvasMap.get(levelName));
                } else {
                    // Nếu màn đã hoàn thành, vào thẳng màn chơi
                    closeDirectly();
                    playerData.setCurrentLevel(levelName);
                    GameManager.getInstance(GameManager.class).setScreen(new PlayScreen());
                }
            }
        });
        return playButton;
    }

    private TextButton createDescriptionButton() {
        TextButton descriptionButton = new TextButton("Description", skin);
        descriptionButton.getLabel().setFontScale(1f);
        descriptionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeDirectly();
                UIManager.getInstance(UIManager.class).openUI(descriptionPages.get(0));
            }
        });
        return descriptionButton;
    }

    private TextButton createExitButton() {
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.getLabel().setFontScale(1f);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        return exitButton;
    }

    private void initBackground() {
        Window backgroundWindow = new Window("", skin, "default");
        backgroundWindow.setFillParent(true);
        backgroundWindow.setMovable(false);
        this.addActor(backgroundWindow);
    }
}
