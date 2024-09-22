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

import java.util.Arrays;

public class Set extends Command {

    public Set() {
        super("Set", "Set the value of a modules setting.", "s", "set");
    }

    @Override
    public void execute(String name, String[] args) {

        if(args.length < 3) {
            sendUsage(name);
            return;
        }

        Module module = Gui.getModuleByName(args[0]);
        if(module == null) {
            ChatUtils.clientWarningMessage("Module with name " + args[0] + " not found.");
            return;
        }

        Setting setting = module.getSettingByName(args[1]);
        if(setting == null) {
            ChatUtils.clientWarningMessage("Setting with name " + args[1] + " in module " + module.getName() + " not found.");
            return;
        }

        if(setting instanceof BooleanSetting booleanSetting) {
            String valueStr = args[2].toLowerCase();
            boolean value;

            if (valueStr.equalsIgnoreCase("toggle") || valueStr.equalsIgnoreCase("t")) {
                booleanSetting.setToggled(!booleanSetting.isToggled());
                Success(module.getName(), setting.getName(), String.valueOf(booleanSetting.isToggled()));
                return;
            }

            if (valueStr.equals("true") || valueStr.equals("on")) {
                value = true;
            } else if (valueStr.equals("false") || valueStr.equals("off")) {
                value = false;
            } else {
                ChatUtils.clientWarningMessage("Invalid value for boolean setting. Use true/false, on/off");
                return;
            }

            booleanSetting.setToggled(value);
            Success(module.getName(), setting.getName(), String.valueOf(booleanSetting.isToggled()));

        } else if (setting instanceof EnumSetting<?> enumSetting) {
            String newValueStr = args[2].toUpperCase();
            Enum<?>[] allModes = enumSetting.getAllModes();

            boolean validValue = false;
            for (Enum<?> mode : allModes) {
                if (mode.name().equalsIgnoreCase(newValueStr)) {
                    enumSetting.setValue(mode);
                    validValue = true;
                    break;
                }
            }

            if (validValue) {
                Success(module.getName(), setting.getName(), enumSetting.getValue().name());
            } else {
                ChatUtils.clientWarningMessage("Invalid value for enum setting. Available values: " +
                        String.join(", ", Arrays.stream(allModes).map(Enum::name).toArray(String[]::new)));
            }

        } else if (setting instanceof NumberSetting<?> numberSetting) {
            if(numberSetting.getIncrement().doubleValue() == 1) {
                int value;
                try {
                    value = Integer.parseInt(args[2]);
                    numberSetting.setValue(value);
                    Success(module.getName(), setting.getName(), String.valueOf(value));
                } catch (NumberFormatException e) {
                    ChatUtils.clientWarningMessage(args[2] + " is not a valid integer.");
                }
            } else {
                double value;
                try {
                    value = Double.parseDouble(args[2]);
                    numberSetting.setValue(value);
                    Success(module.getName(), setting.getName(), String.valueOf(value));
                } catch (NumberFormatException e) {
                    ChatUtils.clientWarningMessage(args[2] + " is not a valid number.");
                }
            }
        }

    }

    private static void sendUsage(String s) {
        ChatUtils.clientWarningMessage("Usage: " + Kybe.PREFIX + s + " <module>" + " <setting>" + " <value>");
    }

    private static void Success(String module, String setting, String value) {
        Gui.saveSettings();
        ChatUtils.FAT_clientMessage("Set " + module + " - " + setting + " to " + value + ".");
    }
}
