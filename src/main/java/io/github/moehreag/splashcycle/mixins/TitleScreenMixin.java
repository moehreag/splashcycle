package io.github.moehreag.splashcycle.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.moehreag.splashcycle.SplashCycle;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	@Shadow
	private @Nullable SplashRenderer splash;

	protected TitleScreenMixin(Component component) {
		super(component);
	}

	@WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;createNormalMenuOptions(II)I"))
	private int addSplashCycleButton(TitleScreen instance, int entryY, int rowHeight, Operation<Integer> original) {
		int y = original.call(instance, entryY, rowHeight);
		Button singleplayer = (Button) children().stream()
				.filter(b -> b instanceof Button e &&
						e.getMessage().equals(Component.translatable("menu.singleplayer")))
				.findFirst().orElseThrow();
		var cycle = SpriteIconButton.builder(Component.translatable("cycle_splash"),
						btn -> {
							SplashCycle.isCycled = true;
							splash = this.minecraft.gui.splashManager().getSplash();
						}, true)
				.size(20, 20)
				.sprite(Identifier.fromNamespaceAndPath("splashcycle", "cycle"), 16, 16).build();
		cycle.setPosition(singleplayer.getRight() + 4, singleplayer.getY());
		cycle.setTooltip(Tooltip.create(cycle.getMessage()));
		addRenderableWidget(cycle);
		return y;
	}
}
