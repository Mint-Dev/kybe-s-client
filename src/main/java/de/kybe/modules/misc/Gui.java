package de.kybe.modules.misc;

import de.kybe.baseModules.Module;
import de.kybe.baseSettings.BooleanSetting;
import de.kybe.eventBus.EventBus;
import de.kybe.eventBus.Subscribe;
import de.kybe.eventBus.events.KeyboardEvent.KeyboardEvent;
import de.kybe.eventBus.events.KeyboardEvent.RawKeyboardEvent;
import de.kybe.eventBus.events.typeCharEvent.TypeCharEvent;
import de.kybe.gui.CategoryEnum;

import static de.kybe.Kybe.keyMapping;
import static de.kybe.constants.Globals.mc;

@SuppressWarnings("unused")
public class Gui {
	private static final String openCombo = "kybe";
	static BooleanSetting combo = new BooleanSetting("Open Combo");
	private static String input = "";
	public Module module = new Module("Gui", CategoryEnum.MISC);

	public Gui() {
		combo.setToggled(true);
		module.addSetting(combo);

		de.kybe.gui.Gui.addModule(module);
		EventBus.register(this);
	}

	@Subscribe
	public static void onCharTyped(TypeCharEvent event) {
		if (event.getType() == KeyboardEvent.Type.RELEASE || !combo.isToggled()) {
			return;
		}

		input += event.getKey();

		if (input.length() > openCombo.length()) {
			input = input.substring(1);
		}

		if (input.equals(openCombo)) {
			event.setCancel(true);
			mc.setScreen(new de.kybe.gui.Gui());
			input = "";
		}
	}

	@Subscribe
	public static void onRawKeyboard(RawKeyboardEvent event) {
		if (keyMapping.matches(event.getI(), event.getJ()) && mc.screen == null) {
			event.setCancel(true);
			mc.setScreen(new de.kybe.gui.Gui());
		}
	}
}
