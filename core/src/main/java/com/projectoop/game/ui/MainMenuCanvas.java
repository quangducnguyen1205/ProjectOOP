package com.projectoop.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.manager.GameManager;
import com.projectoop.game.screens.FirstMapScreen;
import com.projectoop.game.screens.FourthMapScreen;
import com.projectoop.game.screens.SecondMapScreen;
import com.projectoop.game.screens.ThirdMapScreen;
import com.projectoop.game.sprites.PlayerData;
import com.projectoop.game.tools.AudioManager;
import com.projectoop.game.ui.description.DescriptionPage1;
import com.projectoop.game.ui.description.DescriptionPage2;
import com.projectoop.game.ui.story.StoryLevel1;
import com.projectoop.game.ui.story.StoryLevel2;
import com.projectoop.game.ui.story.StoryLevel3;
import com.projectoop.game.ui.story.StoryLevel4;

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

        // Nút Hướng dẫn
        TextButton guideButton = createGuideButton();

        // Bố cục UI
        Table layout = new Table();
        layout.setFillParent(true);
        layout.center();  // Căn giữa toàn bộ layout

        // Tạo một khoảng cách phía trên
        layout.row().padTop(200);

        // Tạo hai nhóm nút
        Table leftButtons = new Table();
        leftButtons.add(levelSelector).width(200).height(50).padBottom(10).row();
        leftButtons.add(playButton).width(200).height(50);

        Table rightButtons = new Table();
        rightButtons.add(descriptionButton).width(200).height(50).padBottom(10).row();
        rightButtons.add(guideButton).width(200).height(50).padBottom(10).row();

        // Đặt các nút vào bảng chính (layout), căn giữa theo chiều ngang
        layout.add(leftButtons).align(Align.center).padRight(-60).expandX().fillX(); // Bảng bên trái
        layout.add(rightButtons).align(Align.center).padLeft(-60).expandX().fillX(); // Bảng bên phải

        // Thêm dòng chứa nút Exit vào giữa dưới
        layout.row().padTop(120); // Tạo một khoảng cách phía trên
        layout.add(exitButton).colspan(2).align(Align.center).width(200).height(50); // Nút Exit căn giữa dưới

        this.addActor(layout);
    }


    private TextButton createPlayButton(SelectBox<String> levelSelector) {
        TextButton playButton = new TextButton("Play", skin);
        playButton.getLabel().setFontScale(1f);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
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
                    // Chuyển đến màn tương ứng
                    if (levelName.equals("level1")) {
                        GameManager.getInstance(GameManager.class).setScreen(new FirstMapScreen(GameManager.getInstance(GameManager.class).getGameWorld()));
                    } else if (levelName.equals("level2")) {
                        GameManager.getInstance(GameManager.class).setScreen(new SecondMapScreen(GameManager.getInstance(GameManager.class).getGameWorld()));
                    } else if (levelName.equals("level3")) {
                        GameManager.getInstance(GameManager.class).setScreen(new ThirdMapScreen(GameManager.getInstance(GameManager.class).getGameWorld()));
                    } else if (levelName.equals("level4")) {
                        GameManager.getInstance(GameManager.class).setScreen(new FourthMapScreen(GameManager.getInstance(GameManager.class).getGameWorld()));
                    }
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
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
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
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
                Gdx.app.exit();
            }
        });
        return exitButton;
    }

    private TextButton createGuideButton() {
        TextButton guideButton = new TextButton("Guide", skin);
        guideButton.getLabel().setFontScale(1f);
        guideButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.manager.get(AudioManager.bamUIAudio, Sound.class).play();
                closeDirectly();
                UIManager.getInstance(UIManager.class).openUI(GuideUI.class);  // Mở UI Hướng dẫn
            }
        });
        return guideButton;
    }

    private void initBackground() {
        Image backgroundImage = new Image(new Texture("assets/UI/BG_MainMenu.png"));
        backgroundImage.setFillParent(true);
        this.addActor(backgroundImage);
    }
}
