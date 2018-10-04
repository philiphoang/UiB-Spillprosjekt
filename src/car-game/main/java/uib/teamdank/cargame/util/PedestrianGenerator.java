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

import uib.teamdank.cargame.Pedestrian;
import uib.teamdank.common.GameObject;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.AudioManager;
import uib.teamdank.common.util.Generator;
import uib.teamdank.common.util.TextureAtlas;
import uib.teamdank.common.util.WeatherData.WeatherType;

/**
 * Generates {@link GameObject}s of the correct pedestrian type. This class does
 * not interact with the game directly; its purely functional.
 */
public class PedestrianGenerator implements Generator<GameObject> {
	private final List<Generator<GameObject>> entityGenerators = new ArrayList<>();
	public PedestrianGenerator(AssetManager assets, TextureAtlas atlas, WeatherType wType) {
		final AudioManager audio = assets.getAudio();
		this.entityGenerators.add(rnd -> new Pedestrian(audio, 5, 60, 0, rnd.nextBoolean(), assets.getAnimation("Images/walker_kid_animation.json")));
		
		if (wType == WeatherType.CLOUD || wType == WeatherType.SUN)
			this.entityGenerators.add(rnd -> new Pedestrian(audio, 10, 100, 0, rnd.nextBoolean(), atlas.getRegion("skateboard")));
		
		if (wType == WeatherType.RAIN){
			this.entityGenerators.add(rnd -> new Pedestrian(audio, 2, 50, 0, rnd.nextBoolean(), atlas.getRegion("umbrella_blue")));
			this.entityGenerators.add(rnd -> new Pedestrian(audio, 2, 50, 0, rnd.nextBoolean(), atlas.getRegion("umbrella_red")));
			this.entityGenerators.add(rnd -> new Pedestrian(audio, 2, 50, 0, rnd.nextBoolean(), atlas.getRegion("umbrella_yellow")));
			this.entityGenerators.add(rnd -> new Pedestrian(audio, 2, 50, 0, rnd.nextBoolean(), atlas.getRegion("umbrella_green")));
		}
		
		if (wType == WeatherType.SNOW) {
			this.entityGenerators.add(rnd -> new Pedestrian(audio, 15, 0, 0, rnd.nextBoolean(), atlas.getRegion("snowman")));
			this.entityGenerators.add(rnd -> new Pedestrian(audio, 20, 0, 100, rnd.nextBoolean(), atlas.getRegion("skier")));
		} else
			this.entityGenerators.add(rnd -> new Pedestrian(audio, 5, 0, 100, rnd.nextBoolean(), atlas.getRegion("cyclist")));
	}

	@Override
	public GameObject generate(Random random) {
		final int index = random.nextInt(entityGenerators.size());
		return entityGenerators.get(index).generate(random);
	}
}
