package com.projectgame.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.MathUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Utilities {

    private static final float GRAVITY = 10f;  // Constant gravity value

    // Tính giá trị (value) dựa trên khoảng (floor, ceiling) -> (min, max)
    public static float getValue(float value, float floor, float ceiling, float min, float max) {
        return (ceiling == floor) ? 0 : (value - floor) * (max - min) / (ceiling - floor) + min;
    }

    // Linear interpolation (lerp)
    public static double lerp(double a, double b, float t) {
        return a + (b - a) * MathUtils.clamp(t, 0f, 1f);
    }

    public static float lerp(float a, float b, float t) {
        return a + (b - a) * MathUtils.clamp(t, 0f, 1f);
    }

    // Tính toán đường parabol theo thời gian (dùng để ném vật thể)
    public static Vector2 calculateLinePoint(Vector2 throwPoint, Vector2 force, float wind, float t) {
        float x = (force.x * t) + (wind * t * t * 0.5f);
        float y = (force.y * t) - (GRAVITY * t * t * 0.5f);
        return new Vector2(x + throwPoint.x, y + throwPoint.y);
    }

    // Tính toán tỷ lệ vận tốc để bắn tới một điểm cho trước
    public static float calculateRatioVelocity(Vector2 throwPoint, Vector2 force, Vector2 targetPoint, float wind) {
        float dx = targetPoint.x - throwPoint.x;
        float dy = targetPoint.y - throwPoint.y;

        float t = (float) Math.sqrt(2 * (dx * force.y - dy * force.x) / (wind * force.y + GRAVITY * force.x));

        float vx = (dx - wind * t * t * 0.5f) / t;

        return vx / force.x;
    }

    // Tính thời gian để đến mục tiêu
    public static float timeToTarget(Vector2 throwPoint, Vector2 force, Vector2 targetPoint, float wind) {
        float dx = targetPoint.x - throwPoint.x;
        float dy = targetPoint.y - throwPoint.y;

        return (float) Math.sqrt(2 * (dx * force.y - dy * force.x) / (wind * force.y + GRAVITY * force.x));
    }

    // Ép kiểu chuỗi thành kiểu Enum
    public static <T extends Enum<T>> T parseEnum(Class<T> enumType, String value) {
        return Enum.valueOf(enumType, value.toUpperCase());
    }

    // Random sắp xếp thứ tự trong một list
    public static <T> List<T> sortOrder(List<T> list, int amount) {
        Collections.shuffle(list);
        return list.stream().limit(amount).collect(Collectors.toList());
    }

    // Kiểm tra tỉ lệ phần trăm
    public static boolean chance(float rand, float max) {
        return MathUtils.random(0f, max) < rand;
    }

    // Random giá trị từ một enum
    private static Random random = new Random();
    public static <T extends Enum<T>> T randomEnumValue(Class<T> enumType) {
        T[] enumValues = enumType.getEnumConstants();
        return enumValues[random.nextInt(enumValues.length)];
    }

    // Lấy số lượng phần tử trong một enum
    public static <T extends Enum<T>> int getEnumCount(Class<T> enumType) {
        return enumType.getEnumConstants().length;
    }

    // Lấy phần tử ngẫu nhiên từ một mảng hoặc list
    public static <T> T takeRandom(T[] ts) {
        return ts[MathUtils.random(0, ts.length - 1)];
    }

    public static <T> T takeRandom(List<T> ts) {
        return ts.get(MathUtils.random(0, ts.size() - 1));
    }

    // Lấy phần tử cuối cùng từ mảng hoặc list
    public static <T> T last(T[] ts) {
        return ts[ts.length - 1];
    }

    public static <T> T last(List<T> ts) {
        return ts.get(ts.size() - 1);
    }

    // Tính toán vị trí trong hàng
    public static Vector3 getPositionInRow(int index, int max, Vector3 midPoint, Vector3 space) {
        float firstIndex = ((float) max - 1) * 0.5f;
        return midPoint.add(space.scl(index - firstIndex));
    }

}
