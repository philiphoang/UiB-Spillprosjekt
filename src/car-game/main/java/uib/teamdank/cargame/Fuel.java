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
package uib.teamdank.cargame;

import uib.teamdank.common.Item;
import uib.teamdank.common.util.AudioManager;
import uib.teamdank.common.util.TextureAtlas;

/**
 * A fuel can in the road. Has positive effects if driven over by the
 * {@link Player}.
 */
public class Fuel extends Item implements RoadEntity {

	private static final String SOUND = "Sounds/fuel.wav";
	private static final String TEXTURE_REGION = "gastank";

	private static final int DEFAULT_FUEL_AMOUNT = 25;

	private final AudioManager audioManager;
	private boolean wasDrivenOver = false;

	public Fuel(AudioManager audioManager, TextureAtlas atlas) {
		super("Fuel", "A jerry can full of delicious fuel.");
		this.audioManager = audioManager;
		setTexture(atlas.getRegion(TEXTURE_REGION));
		setScale(.25f);

		audioManager.preloadSounds(SOUND);
	}

	@Override
	public void drivenOverBy(Player player) {
		if (!wasDrivenOver) {
			player.increaseHealth(DEFAULT_FUEL_AMOUNT);
			audioManager.playSound(SOUND);
			wasDrivenOver = true;
		}
		this.markForRemoval();
	}
}