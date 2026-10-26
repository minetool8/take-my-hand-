package dev.compat.handhelpemf;

import dev.alvar.handhelp.client.FirstPersonAnimation;
import dev.alvar.handhelp.client.PlayerAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import traben.entity_model_features.EMFAnimationApi;
import traben.entity_model_features.models.IEMFModel;

/**
 * Runs right after EMF finishes its CEM animation of a model. If that model is the player
 * model we just captured in setupAnim, re-run Take My Hand's own pose code on top of
 * EMF's result (Take My Hand blends from the parts' current values, so this layers the
 * hand-help pose over whatever the resource pack produced).
 */
public final class TmhEmfHook extends EMFAnimationApi.EMFAnimationHook {
    private boolean loggedFirstApply;
    private boolean loggedFailure;

    @Override
    public void onAnimationEnd(AnimationContext context, boolean wasCancelledByHook) {
        PlayerModel<?> model = PoseCapture.model();
        LivingEntity entity = PoseCapture.entity();
        if (model == null || entity == null) return;

        // Only react when EMF just animated *this* player model.
        if (!(model instanceof IEMFModel emfModel) || !emfModel.emf$isEMFModel()) return;
        if (emfModel.emf$getEMFRootModel() != context.animatingModelRoot()) return;

        PoseCapture.clear();

        // First-person arm rendering is handled by Take My Hand itself.
        if (FirstPersonAnimation.rendering()) return;

        try {
            boolean applied = PlayerAnimation.apply(model, entity, Minecraft.getInstance().getFrameTime());
            if (applied && !loggedFirstApply) {
                loggedFirstApply = true;
                HandHelpEmfCompat.LOGGER.info("Re-applied Take My Hand pose over an EMF-animated player model.");
            }
        } catch (Throwable t) {
            if (!loggedFailure) {
                loggedFailure = true;
                HandHelpEmfCompat.LOGGER.error("Failed to re-apply Take My Hand pose after EMF animation", t);
            }
        }
    }
}
