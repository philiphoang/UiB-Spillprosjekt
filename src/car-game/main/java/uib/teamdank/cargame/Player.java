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

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uib.teamdank.common.Actor;
import uib.teamdank.common.Inventory;
import uib.teamdank.common.Score;
import uib.teamdank.common.Upgrade;
import uib.teamdank.common.Upgradeable;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.WeatherData;
import uib.teamdank.common.util.WeatherData.WeatherType;

/**
 * The player/the car.
 */
public class Player extends Actor implements Upgradeable {

	private static final float ZERO_SPEED_TOLERANCE = 8f;

	private static final float INITIAL_HORIZONTAL_ACCELERATION = 40f;
	private static final float HORIZONTAL_FRICTION = .9f;
	private static final float ADD_TO_HORIZONTAL_SPEED = .0018f;

	private static final float VERTICAL_ACCELERATION = 10f;
	private static final float VERTICAL_FRICTION = .98f;
	
	private static final float INITIAL_TOP_SPEED = 512f;
	private static final float MAX_TOP_SPEED = 2048;
	private static final float ADD_TO_TOP_SPEED = 0.1f;
	
	private float topSpeed = INITIAL_TOP_SPEED;
	private float horisontalSpeed = INITIAL_HORIZONTAL_ACCELERATION;
	
	private final Inventory inventory = new Inventory();
	private final Set<String> unlockedSkins = new HashSet<>();

	private final Score score;

	private uib.teamdank.common.util.TextureAtlas carTextures;
	private AssetManager assets;

	private WeatherType wType;

	private Player() {
		super(100, "Per");
		score = new Score(getName());
	}

	public void accelerate() {
		if (!isOutOfFuel() && getVelocity().y != topSpeed) {
			if (getVelocity().y > topSpeed) {
				getVelocity().y = topSpeed;
			} else {
				getVelocity().y += VERTICAL_ACCELERATION;
			}
		}
	}

	public void applyFriction() {
		final Vector2 velocity = getVelocity();

		// Horizontal
		if(wType == WeatherType.SNOW)
			velocity.x *= HORIZONTAL_FRICTION + 0.02;
		else
			velocity.x *= HORIZONTAL_FRICTION;
		if (velocity.epsilonEquals(0, velocity.y, ZERO_SPEED_TOLERANCE)) {
			velocity.x = 0;
		}

		// Vertical
		if (isOutOfFuel()) {
			if(wType == WeatherType.SNOW)
				velocity.y *= VERTICAL_FRICTION + 0.01;
			else
				velocity.y *= VERTICAL_FRICTION;
			if (velocity.epsilonEquals(velocity.x, 0, ZERO_SPEED_TOLERANCE)) {
				velocity.y = 0;
			}
		}
	}

	@Override
	public void applyUpgrade(Upgrade upgrade) {
		// TODO Auto-generated method stub
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Score getScore() {
		return score;
	}
	
	public boolean hasUnlockedSkin(String name) {
		return unlockedSkins.contains(name);
	}

	public boolean isOutOfFuel() {
		return getHealth() == 0;
	}

	public void reset() {
		getPosition().set(0, 0);
		getVelocity().set(0, 0);
		setHealth(getMaxHealth());
		getScore().setScore(0);
		topSpeed = INITIAL_TOP_SPEED;
		horisontalSpeed = INITIAL_HORIZONTAL_ACCELERATION;
	}

	public void restrictHorizontally(int minX, int maxX) {
		if (getPosition().x < minX) {
			getPosition().x = minX;
			getVelocity().x *= -1;
		} else if (getPosition().x > maxX - getWidth()) {
			getPosition().x = (float) (maxX - getWidth());
			getVelocity().x *= -1;
		}
	}

	public void setImage(String car) {
		this.assets = new AssetManager();
		carTextures = assets.getAtlas("Images/car_sheet.json");
		setTexture(carTextures.getRegion(car));
	}

	private void turn(int direction) {
		final Vector2 velocity = getVelocity();
		float horizontalAcceleration = (velocity.y / topSpeed) * horisontalSpeed;
		if(wType == WeatherType.SNOW)
			horizontalAcceleration *= 0.75;
		velocity.x += (Math.signum(direction) * horizontalAcceleration);
	}

	public void turnLeft() {
		turn(-1);
	}

	public void turnRight() {
		turn(1);
	}

	public void unlockSkin(String name) {
		this.unlockedSkins.add(name);
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		accelerate();
		applyFriction();
		if (topSpeed <= MAX_TOP_SPEED) {
			topSpeed += ADD_TO_TOP_SPEED;
			horisontalSpeed += ADD_TO_HORIZONTAL_SPEED;
		}
	}
	
	public void setWeatherType(WeatherType wType) {
		this.wType = wType;
	}
	
	public void saveAsJson() {
		FileHandle handle = Gdx.files.external("TeamDank/Carl the Crasher/player.json");
		Gson gson = new GsonBuilder().create();
		handle.writeString(gson.toJson(this), false);
	}
	
	public static Player create() {
		try {
			FileHandle handle = Gdx.files.external("TeamDank/Carl the Crasher/player.json");
			if (handle.exists())
				return new GsonBuilder().create().fromJson(handle.readString(), Player.class);
			return new Player();
		} catch(Exception e) {
			Logger logger = LoggerFactory.getLogger(WeatherData.class);
			logger.error(e.getMessage());
			
			return new Player();
		}
	}
}
