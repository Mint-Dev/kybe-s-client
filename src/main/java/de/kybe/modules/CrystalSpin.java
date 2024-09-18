package de.kybe.modules;

import de.kybe.gui.Gui;
import de.kybe.gui.components.CategoryEnum;
import de.kybe.gui.components.modules.ToggleableModule;
import de.kybe.gui.components.settings.NumberSetting;

public class CrystalSpin {
	public static ToggleableModule crystalSpin = new ToggleableModule("CrystalSpin", CategoryEnum.COMBAT);

	public CrystalSpin() {
		crystalSpin.addSetting(new NumberSetting<>("SidewaysSpeed", 5f, 0.1f, 50f, 0.5f));
		Gui.addModule(crystalSpin);
	}
}
