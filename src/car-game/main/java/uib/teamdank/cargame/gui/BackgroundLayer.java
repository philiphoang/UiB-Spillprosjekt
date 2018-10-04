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
package uib.teamdank.cargame.gui;

import java.util.Objects;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uib.teamdank.cargame.Player;
import uib.teamdank.cargame.util.LoopingBackground;
import uib.teamdank.cargame.util.ScrollingSpawner;
import uib.teamdank.common.GameObject;
import uib.teamdank.common.gui.Layer;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.TextureAtlas;
import uib.teamdank.common.util.WeatherData.WeatherType;

public class BackgroundLayer extends Layer {

	private final OrthographicCamera playerCamera;
	private final OrthographicCamera screenCamera;
	private final Player player;
	
	private final Texture backgroundTexture;
	private final LoopingBackground scrollingRoad;
	private final ScrollingSpawner[] structureSpawners;
	
	public BackgroundLayer(AssetManager assets, OrthographicCamera playerCamera, OrthographicCamera screenCamera, Player player, WeatherType wType) {
		super(false);
		
		this.playerCamera = playerCamera;
		this.screenCamera = screenCamera;
		this.player = player;
		
		// fordi backgroundTexture er final så kan ikke denne if setningen flyttes ut i egen metode.
		if (wType.equals(WeatherType.SNOW)){
			this.backgroundTexture = new Texture(Gdx.files.internal("Images/background_snow.png"));
		}
		else {
			this.backgroundTexture = new Texture(Gdx.files.internal("Images/background.png"));
		}
		
		this.scrollingRoad = new LoopingBackground(playerCamera, new Texture(Gdx.files.internal("Images/road.png")), .5f);
		
		TextureAtlas structuresAtlas = assets.getAtlas("Images/structure_sheet.json"); 
		TextureRegion[] structureTextures = new TextureRegion[4];
		
		// Set weather texture
		if(wType != WeatherType.SNOW){
			
			structureTextures[0] = structuresAtlas.getRegion("building_left_1");
			structureTextures[1] = structuresAtlas.getRegion("building_left_2");
			structureTextures[2] = structuresAtlas.getRegion("pond_left_1");
			structureTextures[3] = structuresAtlas.getRegion("trees_1");
		} else {
			
			structureTextures[0] = structuresAtlas.getRegion("building_left_1_snow");
			structureTextures[1] = structuresAtlas.getRegion("building_left_2_snow");
			structureTextures[2] = structuresAtlas.getRegion("pond_left_1");
			structureTextures[3] = structuresAtlas.getRegion("trees_1_snow");
		}
		
		this.structureSpawners = new ScrollingSpawner[] {
			setupStructureSpawner(structureTextures, false),
			setupStructureSpawner(structureTextures, true)
		};
	}
	
	public int getRoadLeftX() {
		return -(scrollingRoad.getWidth() - player.getWidth()) / 2;
	}
	
	public int getRoadRightX() {
		return (scrollingRoad.getWidth() + player.getWidth()) / 2;
	}
	
	@Override
	public void preRender(SpriteBatch batch, float delta) {
		final int screenWidth = Gdx.graphics.getWidth();
		final int screenHeight = Gdx.graphics.getHeight();
		
		batch.setProjectionMatrix(screenCamera.combined);
		batch.draw(backgroundTexture, -screenWidth / 2f, -screenHeight / 2f, screenWidth, screenHeight);
		batch.setProjectionMatrix(playerCamera.combined);
		scrollingRoad.render(batch);
		scrollingRoad.updateHorizontalPosition(getRoadLeftX());
		structureSpawners[0].setHorizontalPositionRange(getRoadLeftX(), getRoadLeftX());
		structureSpawners[1].setHorizontalPositionRange(getRoadRightX(), getRoadRightX());
		for (ScrollingSpawner spawner : structureSpawners) {
			spawner.update(delta);
		}
	}
	
	private ScrollingSpawner setupStructureSpawner(TextureRegion[] textures, boolean leftSide) {
		Objects.requireNonNull(textures, "texture scannot be null");
		if (textures.length == 0) {
			throw new IllegalArgumentException("there must be at least one structure texture");
		}
		ScrollingSpawner spawner = new ScrollingSpawner(this, playerCamera, rand -> {
			GameObject structure = new GameObject();
			structure.setTexture(textures[new Random().nextInt(textures.length)]);
			return structure;
		});
		spawner.setExtraVerticalSpaceBetweenSpawns(40);
		spawner.setTimeBetweenSpawns(0);
		spawner.setChanceOfSpawn(1);
		spawner.setFlipTexture(leftSide);
		return spawner;
	}
	
}
