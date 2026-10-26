package dev.compat.handhelpemf;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;

/**
 * Remembers which PlayerModel / entity was just run through setupAnim, so the EMF hook
 * (which only knows about EMF's model root) can hand them to Take My Hand afterwards.
 * Render thread only.
 */
public final class PoseCapture {
    private static PlayerModel<?> model;
    private static LivingEntity entity;

    private PoseCapture() {}

    public static void set(PlayerModel<?> m, LivingEntity e) {
        model = m;
        entity = e;
    }

    public static PlayerModel<?> model() { return model; }

    public static LivingEntity entity() { return entity; }

    public static void clear() {
        model = null;
        entity = null;
    }
}
