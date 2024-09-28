package com.projectoop.pooling;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.files.FileHandle;
import com.projectoop.utils.Transform;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Quản lý các ParticleEffect và tái sử dụng chúng thông qua cơ chế Object Pooling.
 */
public class ParticlePool {

    private static final int DEFAULT_POOL_SIZE = 3;
    private static Transform root;

    // Shortcut cho các ParticleEffect
    private static Map<ParticleType, ParticleEffect> shortcuts = new HashMap<>();
    private static Map<Integer, Pool> pools = new HashMap<>();

    // Lấy root của ParticleEffect
    public static Transform getRoot() {
        if (root == null) {
            root = new Transform();  // Tạo root mới nếu cần
        }
        return root;
    }

    // Lớp Pool quản lý việc spawn và despawn các ParticleEffect
    static class Pool {
        private Transform m_sRoot;
        private ParticleEffect prefab;
        private int index;
        private List<ParticleEffect> inactive = new ArrayList<>();

        // Constructor
        public Pool(ParticleEffect prefab, int initialQty, Transform parent) {
            this.m_sRoot = parent;
            this.prefab = prefab;

            // Tạo sẵn số lượng ParticleEffect ban đầu
            for (int i = 0; i < initialQty; i++) {
                ParticleEffect particle = new ParticleEffect();

                // Nạp particle từ file .p và texture
                FileHandle particleFile = new FileHandle("particles/particle.p");  // Đường dẫn tới file particle .p
                FileHandle imagesDir = new FileHandle("particles");  // Thư mục chứa texture

                particle.load(particleFile, imagesDir);  // Sử dụng phương thức load() thay vì loadEmitters()
                inactive.add(particle);
            }
        }

        // Kích hoạt ParticleEffect từ pool
        public void play(Vector3 pos, Quaternion rot) {
            index = (index + 1) < inactive.size() ? index + 1 : 0;

            ParticleEffect particle = inactive.get(index);

            particle.setPosition(pos.x, pos.y);  // Vector3 có x và y
            particle.start();  // Kích hoạt particle
        }

        // Giải phóng ParticleEffect
        public void release() {
            while (inactive.size() > 0) {
                inactive.remove(0);  // Giải phóng tất cả các ParticleEffect
            }
        }
    }

    // Preload ParticleEffect vào pool
    public static void preload(ParticleEffect prefab, int qty, Transform parent) {
        if (!pools.containsKey(prefab.hashCode())) {
            pools.put(prefab.hashCode(), new Pool(prefab, qty, parent));
        }
    }

    // Kích hoạt ParticleEffect từ pool
    public static void play(ParticleEffect prefab, Vector3 pos, Quaternion rot) {
        if (!pools.containsKey(prefab.hashCode())) {
            Transform newRoot = new Transform();
            pools.put(prefab.hashCode(), new Pool(prefab, DEFAULT_POOL_SIZE, newRoot));
        }

        pools.get(prefab.hashCode()).play(pos, rot);
    }

    // Thêm shortcut cho ParticleType
    public static void addShortcut(ParticleType particleType, ParticleEffect particle) {
        shortcuts.put(particleType, particle);
    }

    // Kích hoạt ParticleEffect theo ParticleType
    public static void play(ParticleType particleType, Vector3 pos, Quaternion rot) {
        if (!shortcuts.containsKey(particleType)) {
            throw new IllegalArgumentException("ParticleType not found!");
        }

        play(shortcuts.get(particleType), pos, rot);
    }

    // Giải phóng ParticleEffect khỏi pool
    public static void release(ParticleEffect prefab) {
        if (pools.containsKey(prefab.hashCode())) {
            pools.get(prefab.hashCode()).release();
            pools.remove(prefab.hashCode());
        }
    }
}
