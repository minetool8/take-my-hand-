package dev.compat.handhelpemf;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import traben.entity_model_features.EMFAnimationApi;

@Mod("handhelpemfcompat")
public final class HandHelpEmfCompat {
    public static final Logger LOGGER = LogUtils.getLogger();

    public HandHelpEmfCompat() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            try {
                EMFAnimationApi.registerAnimationHook(new TmhEmfHook());
                LOGGER.info("Registered EMF animation hook for Take My Hand.");
            } catch (Exception e) {
                LOGGER.error("Could not register EMF animation hook", e);
            }
        });
    }
}
