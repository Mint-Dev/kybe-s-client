/*
 * Copyright (c) 2024 kybe236
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.kybe;

import com.mojang.blaze3d.platform.InputConstants;
import de.kybe.baseCommands.CommandManager;
import de.kybe.commands.*;
import de.kybe.mixin.IKeyMapping;
import de.kybe.mixin.IOptions;
import de.kybe.modules.Test;
import de.kybe.modules.misc.Gui;
import de.kybe.modules.render.CrystalSpin;
import de.kybe.modules.movement.DoubleJump;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kybe implements ModInitializer {
	public static final String MOD_ID = "kybe";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static KeyMapping keyMapping;
	public static final String PREFIX = "?";

	@Override
	public void onInitialize() {
		//noinspection InstantiationOfUtilityClass
		new DoubleJump();
		//noinspection InstantiationOfUtilityClass
		new CrystalSpin();
		new Test();
		new Gui();

		CommandManager.addCommand(new Say());
		CommandManager.addCommand(new de.kybe.commands.Test());
		CommandManager.addCommand(new Help());
		CommandManager.addCommand(new Set());
		CommandManager.addCommand(new Modules());
		CommandManager.addCommand(new Toggle());
	}

	public static void afterConfigInit() {
		keyMapping =  new KeyMapping("key.kybe.open", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "category.kybe");

		Options config = Minecraft.getInstance().options;

		((IOptions) config).setKeyMappings(ArrayUtils.add(config.keyMappings, keyMapping));
		((IKeyMapping) keyMapping).getCategorySortOrder().put("category.kybe", ((IKeyMapping) keyMapping).getCategorySortOrder().size() + 1);
	}
}