package com.projectoop.pooling;

import com.projectoop.utils.Transform;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import java.util.ArrayList;
import java.util.List;

/**
 * PoolController quản lý các đối tượng Pool và Particle.
 */
public class PoolController {

    // Danh sách các đối tượng không có root
    public List<GameUnit> poolNoneRoot = new ArrayList<>();

    // Danh sách các đối tượng có root
    public List<PoolAmount> poolWithRoot = new ArrayList<>();

    // Mảng chứa các ParticleEffect và thông tin preload của chúng
    public ParticleAmount[] particleArray;

    /**
     * Phương thức khởi tạo (thay thế cho Awake trong Unity).
     * Sử dụng để preload các đối tượng vào pool khi game bắt đầu.
     */
    public void init() {
        // Preload các đối tượng trong PoolNoneRoot
        for (GameUnit unit : poolNoneRoot) {
            SimplePool.preload(unit, 0, null);  // Không có root cho các đối tượng này
        }

        // Preload các đối tượng trong PoolWithRoot
        for (PoolAmount poolAmount : poolWithRoot) {
            SimplePool.preload(poolAmount.prefab, poolAmount.amount, poolAmount.root);  // Có root cho các đối tượng này
        }

        // Preload các ParticleEffect
        for (ParticleAmount particleAmount : particleArray) {
            ParticlePool.preload(particleAmount.prefab, particleAmount.amount, particleAmount.root);
            ParticlePool.addShortcut(particleAmount.particleType, particleAmount.prefab);
        }
    }
}

/**
 * Lớp PoolAmount chứa thông tin về đối tượng trong Pool.
 */
class PoolAmount {
    public Transform root;
    public GameUnit prefab;
    public int amount;

    public PoolAmount(Transform root, GameUnit prefab, int amount) {
        this.root = root;
        this.prefab = prefab;
        this.amount = amount;
    }
}

/**
 * Lớp ParticleAmount chứa thông tin về ParticleEffect trong Pool.
 */
class ParticleAmount {
    public Transform root;
    public ParticleType particleType;
    public ParticleEffect prefab;
    public int amount;

    public ParticleAmount(Transform root, ParticleType particleType, ParticleEffect prefab, int amount) {
        this.root = root;
        this.particleType = particleType;
        this.prefab = prefab;
        this.amount = amount;
    }
}

/**
 * Enum cho loại Particle.
 */
enum ParticleType {
    // Các loại particle (nếu có) sẽ được định nghĩa tại đây
}

/**
 * Enum cho loại PoolType.
 */
enum PoolType {
    // Các loại Pool (nếu có) sẽ được định nghĩa tại đây
}
