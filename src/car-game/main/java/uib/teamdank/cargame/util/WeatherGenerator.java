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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uib.teamdank.common.GameObject;
import uib.teamdank.common.util.Generator;
import uib.teamdank.common.util.TextureAtlas;
import uib.teamdank.common.util.WeatherData.WeatherType;

public class WeatherGenerator implements Generator<GameObject> {
	private final List<Generator<GameObject>> entityGenerators = new ArrayList<>();
	
	public WeatherGenerator(WeatherType wType) {
		
		if(wType == WeatherType.CLOUD) 
			addClouds();
		else if (wType == WeatherType.SNOW)
			this.entityGenerators.add(rnd -> {
				GameObject wObj = new GameObject(new TextureRegion(new Texture(Gdx.files.internal("Images/snow.png"))));
				wObj.getVelocity().set(-10, -20);
				return wObj;
			});
		else if (wType == WeatherType.RAIN)
			this.entityGenerators.add(rnd -> {
				GameObject wObj = new GameObject(new TextureRegion(new Texture(Gdx.files.internal("Images/rain.png"))));
				wObj.getVelocity().set(-20, -40);
				return wObj;
			});
		else
			this.entityGenerators.add(rnd -> new GameObject(new TextureRegion(new Texture(Gdx.files.internal("Images/empty.png")))));
	}

	private void addClouds() {
		TextureAtlas atlas = TextureAtlas.createFromJson(Gdx.files.internal("Images/cloud_sheet.json"));
		this.entityGenerators.add(rnd -> {
			GameObject wObj = new GameObject(atlas.getRegion("cloud_1"));
			wObj.getVelocity().set(-10, -20);
			return wObj;
		});
		this.entityGenerators.add(rnd -> {
			GameObject wObj = new GameObject(atlas.getRegion("cloud_2"));
			wObj.getVelocity().set(-10, -20);
			return wObj;
		});
		this.entityGenerators.add(rnd -> {
			GameObject wObj = new GameObject(atlas.getRegion("cloud_3"));
			wObj.getVelocity().set(-10, -20);
			return wObj;
		});
	}
	
	@Override
	public GameObject generate(Random random) {
		int index = random.nextInt(entityGenerators.size());
		return entityGenerators.get(index).generate(random);
	}

}
