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

import com.badlogic.gdx.math.Vector2;

import uib.teamdank.common.Item;
import uib.teamdank.common.util.TextureAtlas;

/**
 * A hole in the road. Has negative effects if driven over by the
 * {@link Player}.
 */
public class Hole extends Item implements RoadEntity {

	private static final String TEXTURE_REGION = "hole";

	public Hole(TextureAtlas atlas) {
		super("Hole", "A man hole that suspiciously is uncovered.");
		setTexture(atlas.getRegion(TEXTURE_REGION));
		setScale(.4f);
	}

	@Override
	public void drivenOverBy(Player player) {
		player.setHealth(0);
		player.getVelocity().set(Vector2.Zero);
	}

}
