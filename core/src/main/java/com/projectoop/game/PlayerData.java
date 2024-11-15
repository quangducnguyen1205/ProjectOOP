package com.projectoop.game;

import com.badlogic.gdx.utils.Array;

public class PlayerData {
    private Array<String> completedLevels; // Lưu danh sách các level đã hoàn thành
    private String currentLevel; // Level hiện tại
    private int score; // Điểm của người chơi

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
}
