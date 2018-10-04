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
package uib.teamdank.cargame.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uib.teamdank.cargame.Coin;
import uib.teamdank.cargame.Fuel;
import uib.teamdank.cargame.Hole;
import uib.teamdank.cargame.Puddle;
import uib.teamdank.common.GameObject;
import uib.teamdank.common.util.AudioManager;
import uib.teamdank.common.util.Generator;
import uib.teamdank.common.util.TextureAtlas;
import uib.teamdank.common.util.WeatherData.WeatherType;

/**
 * Generates {@link GameObject}s of the correct road entity type. This class
 * does not interact with the game directly—its purely functional. 
 */
public class RoadEntityGenerator implements Generator<GameObject> {
	private final List<Generator<GameObject>> entityGenerators = new ArrayList<>();

	public RoadEntityGenerator(AudioManager audio, TextureAtlas roadEntityAtlas, WeatherType wType) {
		this.entityGenerators.add(rnd -> new Fuel(audio, roadEntityAtlas));
		this.entityGenerators.add(rnd -> new Fuel(audio, roadEntityAtlas));
		this.entityGenerators.add(rnd -> new Hole(roadEntityAtlas));
		this.entityGenerators.add(rnd -> new Coin(audio, roadEntityAtlas));
		
		this.entityGenerators.add(rnd -> new Puddle(audio, roadEntityAtlas, wType));
		if(wType == WeatherType.RAIN) {
			this.entityGenerators.add(rnd -> new Puddle(audio, roadEntityAtlas, wType));
			this.entityGenerators.add(rnd -> new Puddle(audio, roadEntityAtlas, wType));
		}
	}

	@Override
	public GameObject generate(Random random) {
		final int index = random.nextInt(entityGenerators.size());
		return entityGenerators.get(index).generate(random);
	}
}
