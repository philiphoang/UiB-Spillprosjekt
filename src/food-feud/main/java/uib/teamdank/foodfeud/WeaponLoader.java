/*******************************************************************************
 * Copyright (C) 2017  TeamDank
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package uib.teamdank.foodfeud;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.Gson;

import uib.teamdank.common.util.AssetManager;

public class WeaponLoader {
	
	private static class WeaponModel {
		String name;
		String description;
		String texture;
		int calories;
		float mass;
		int amount;
		Weapon.Type type;
	}
	
	private static WeaponModel[] cachedModels;
	
	public static Weapon[] fromJson(AssetManager assets, FileHandle file) {
		if (cachedModels == null) {
			cachedModels = new Gson().fromJson(file.reader(), WeaponModel[].class);
		}
		Weapon[] weapons = new Weapon[cachedModels.length];
		for (int i = 0; i < cachedModels.length; i++) {
			final WeaponModel w = cachedModels[i];
			final TextureRegion tex = assets.getAtlas("Images/food_sheet.json").getRegion(w.texture);
			weapons[i] = new Weapon(w.name, w.description, tex, (int) (w.calories * w.mass)/8, w.mass,w.type);
		}
		return weapons;
	}
	
	private WeaponLoader() {
		// Hide constructor
	}
	
}
