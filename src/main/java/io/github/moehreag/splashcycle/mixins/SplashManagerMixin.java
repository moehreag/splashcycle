package io.github.moehreag.splashcycle.mixins;

import java.util.List;

import io.github.moehreag.splashcycle.SplashCycle;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SplashManager.class)
public abstract class SplashManagerMixin {

	@Shadow
	private List<Component> splashes;

	@Shadow
	@Final
	private static RandomSource RANDOM;

	@Inject(method = "getSplash", at = @At("HEAD"), cancellable = true)
	private void ensureCycle(CallbackInfoReturnable<SplashRenderer> cir) {
		if (SplashCycle.isCycled) {
			cir.setReturnValue(new SplashRenderer(this.splashes.get(RANDOM.nextInt(this.splashes.size()))));
		}
	}
}
