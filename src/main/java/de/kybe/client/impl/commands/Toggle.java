package de.kybe.client.impl.commands;

import de.kybe.Kybe;
import de.kybe.client.core.command.Command;
import de.kybe.client.core.module.Module;
import de.kybe.client.core.module.ToggleableModule;
import de.kybe.client.core.gui.gui.Gui;
import de.kybe.client.core.util.ChatUtils;

public class Toggle extends Command {

	public Toggle() {
		super("Toggle", "Toggle a module.", "toggle", "t");
	}

	@Override
	public void execute(String name, String[] args) {

		if (args.length < 1) {
			ChatUtils.clientWarningMessage("Usage: " + Kybe.PREFIX + name + " <module>");
			return;
		}

		Module module = Gui.getModuleByName(args[0]);
		if (module == null) {
			ChatUtils.clientWarningMessage("Module with name " + args[0] + " not found.");
		} else {
			if (module instanceof ToggleableModule toggleableModule) {
				toggleableModule.toggle();
				Gui.saveSettings();
				ChatUtils.FAT_clientMessage("Toggled: " + module.getName() + " | " + toggleableModule.isToggled());
			}
		}

	}
}
