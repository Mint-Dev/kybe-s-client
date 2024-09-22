package de.kybe.commands;

import de.kybe.Kybe;
import de.kybe.baseCommands.Command;
import de.kybe.baseModules.Module;
import de.kybe.baseSettings.BooleanSetting;
import de.kybe.baseSettings.EnumSetting;
import de.kybe.baseSettings.NumberSetting;
import de.kybe.baseSettings.Setting;
import de.kybe.gui.Gui;
import de.kybe.utils.ChatUtils;

import java.util.Comparator;
import java.util.List;

public class Modules extends Command {

	public Modules() {
		super("Modules", "Lists all modules.", "modules", "mods");
	}

	@Override
	public void execute(String name, String[] args) {
		List<Module> modules = Gui.getModules();

		if (args.length < 1) {
			ChatUtils.FAT_clientMessage("Modules:");
			modules.stream()
					.sorted(Comparator.comparing(Module::getName))
					.forEach(module -> ChatUtils.clientMessage(module.getName()));
		} else if (args.length == 1) {
			String moduleName = args[0];
			Module module = modules.stream()
					.filter(mod -> mod.getName().equalsIgnoreCase(moduleName))
					.findFirst()
					.orElse(null);
			if (module != null) {
				List<Setting> settings = module.getSettings();
				ChatUtils.FAT_clientMessage(module.getName() + " has the following settings: ");
				settings.stream()
						.sorted(Comparator.comparing(Setting::getName))
						.forEach(setting -> ChatUtils.clientMessage(setting.getName() + " [" + getType(setting) + " | " + getSettingValue(setting) + "]"));
			} else {
				ChatUtils.clientWarningMessage("Module with name " + moduleName + " not found.");
			}
		} else {
			ChatUtils.clientWarningMessage("Usage: " + Kybe.PREFIX + " " + name + "<module>");
		}
	}

	private String getType(Setting setting) {
		if (setting instanceof NumberSetting) {
			return "Number";
		} else if (setting instanceof EnumSetting) {
			return "Enum";
		} else if (setting instanceof BooleanSetting) {
			return "Boolean";
		}
		return "Undefined";
	}

	private String getSettingValue(Setting setting) {
		if (setting instanceof NumberSetting) {
			return (String.valueOf(((NumberSetting<?>) setting).getValue()));
		} else if (setting instanceof EnumSetting) {
			return (String.valueOf(((EnumSetting<?>) setting).getValue()));
		} else if (setting instanceof BooleanSetting) {
			return (String.valueOf(((BooleanSetting) setting).isToggled()));
		}
		return "Undefined";
	}
}