/*
 * Copyright (c) 2024 kybe236
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.kybe.baseModules;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.kybe.baseSettings.Setting;
import de.kybe.gui.CategoryEnum;

import java.util.ArrayList;
import java.util.List;

public class Module {
	private final String name;
	private final ArrayList<Setting> settings;
	private final CategoryEnum category;

	public Module(String name, CategoryEnum category) {
		this.name = name;
		this.settings = new ArrayList<>();
		this.category = category;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	public void addSetting(Setting setting) {
		settings.add(setting);
		setting.setParent(this);
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public Setting getSettingByName(String settingName) {
		return settings.stream()
				.filter(setting -> setting.getName().equalsIgnoreCase(settingName))
				.findFirst()
				.orElse(null);
	}

	public CategoryEnum getCategory() {
		return category;
	}

	/*
	 * Returns true if the key was handled
	 */
	public boolean handleKeyPress(int key) {
		return false;
	}

	public JsonObject serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("category", this.getCategory().name());
		obj.addProperty("name", this.getName());

		if (!this.getSettings().isEmpty()) {
			JsonArray settings = new JsonArray();
			for (Setting setting : this.getSettings()) {
				settings.add(setting.serialize());
			}
			obj.add("settings", settings);
		}
		return obj;
	}

	public void deserialize(JsonObject obj) {
		if (!obj.get("category").getAsString().equals(this.category.name())) return;
		if (!obj.has("name") || !obj.get("name").getAsString().equals(this.getName())) return;

		//noinspection DuplicatedCode
		if (obj.has("settings")) {
			JsonArray settings = obj.getAsJsonArray("settings");
			for (JsonElement settingObj : settings.asList()) {
				if (settingObj.isJsonObject()) {
					JsonObject settingJson = settingObj.getAsJsonObject();
					Setting setting = this.getSettingByName(settingJson.get("name").getAsString());
					if (settingJson.has("name") && settingJson.get("name").getAsString().equals(setting.getName())) {
						setting.deserialize(settingJson);
					}
				}
			}
		}

	}
}