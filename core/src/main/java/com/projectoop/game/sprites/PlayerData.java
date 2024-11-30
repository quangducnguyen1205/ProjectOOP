package com.projectoop.game.sprites;

import com.badlogic.gdx.utils.Array;

import java.util.HashSet;
import java.util.Set;

public class PlayerData {
    private Array<String> completedLevels; // Lưu danh sách các level đã hoàn thành
    private String currentLevel; // Level hiện tại
    private int score; // Điểm của người chơi
    private final Set<String> firstTimePlayedLevels = new HashSet<>();

    public PlayerData() {
        completedLevels = new Array<>();
        currentLevel = "level1"; // Mặc định cấp độ đầu tiên
        score = 0;
    }

    // Thêm một level đã hoàn thành
    public void addCompletedLevel(String level) {
        if (!completedLevels.contains(level, false)) {
            completedLevels.add(level);
        }
    }

    // Kiểm tra xem level đã được hoàn thành chưa
    public boolean isLevelCompleted(String level) {
        return completedLevels.contains(level, false);
    }

    // Đánh dấu level đã được chơi lần đầu
    public void addFirstTimePlayedLevel(String level) {
        // Chỉ thêm vào nếu màn chưa hoàn thành
        if (!isLevelCompleted(level)) {
            firstTimePlayedLevels.add(level);
        }
    }

    public boolean isFirstTimePlayed(String level) {
        // Nếu là level1 và chưa hoàn thành, mặc định là lần đầu chơi
        if (level.equals("level1") && !isLevelCompleted(level)) {
            return true;
        }
        return firstTimePlayedLevels.contains(level);
    }


    // Kiểm tra nếu level khả dụng
    public boolean isLevelAvailable(String level) {
        if (level.equals("level1")) {
            return true; // Level 1 luôn khả dụng
        }

        // Lấy số thứ tự màn chơi hiện tại
        int levelIndex = Integer.parseInt(level.replace("level", ""));
        String previousLevel = "level" + (levelIndex - 1);

        // Level khả dụng nếu màn trước đó đã hoàn thành
        return isLevelCompleted(previousLevel);
    }

    // Xóa trạng thái lần đầu chơi (nếu cần thiết, ví dụ khi reset game)
    public void resetFirstTimePlayedLevels() {
        firstTimePlayedLevels.clear();
    }

    // Getters và Setters
    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Set<String> getFirstTimePlayedLevels() {
        return firstTimePlayedLevels;
    }
}
