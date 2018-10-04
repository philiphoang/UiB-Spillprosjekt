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

import java.util.Objects;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;

import uib.teamdank.common.GameObject;
import uib.teamdank.common.gui.Layer;
import uib.teamdank.common.util.Generator;

public class ScrollingSpawner implements Generator<GameObject> {

	private static final Random random = new Random();

	private final Layer layer;
	private final OrthographicCamera camera;
	private final Generator<GameObject> generator;

	private boolean flipTexture;
	private int extraVerticalSpaceBetweenSpawns;
	private int minHorizontalPosition;
	private int maxHorizontalPosition;

	private float timeBetweenSpawns;
	private float chanceOfSpawn;

	private final Array<GameObject> spawns = new Array<>();
	private float timeSinceSpawn = 0;

	public ScrollingSpawner(Layer layer, OrthographicCamera camera, Generator<GameObject> generator) {
		this.layer = Objects.requireNonNull(layer, "layer cannot be null");
		this.camera = Objects.requireNonNull(camera, "camera cannot be null");
		this.generator = Objects.requireNonNull(generator, "generator cannot be null");

		setFlipTexture(false);
		setExtraVerticalSpaceBetweenSpawns(0);
		setTimeBetweenSpawns(0f);
		setChanceOfSpawn(1f);
	}

	private GameObject createNewSpawn() {
		GameObject spawn = generator.generate(random);
		spawn.getPosition().y = camera.position.y + camera.viewportHeight / 2 + extraVerticalSpaceBetweenSpawns;
		spawn.getPosition().x = getNewHorizontalPosition(spawn.getWidth());
		spawn.setFlipHorizontally(flipTexture);
		spawns.add(spawn);
		layer.addGameObject(spawn);
		return spawn;
	}


	private void deleteOldStructures() {
		GameObject firstSpawn = spawns.get(0);
		if (firstSpawn.isMarkedForRemoval()) {
			// Object has already been removed elsewhere
			spawns.removeIndex(0);
		} else {
			float firstY = firstSpawn.getPosition().y;
			float firstHeight = firstSpawn.getHeight();
			if (firstY + firstHeight < camera.position.y - camera.viewportHeight / 2) {
				spawns.removeIndex(0);
				firstSpawn.markForRemoval();
			}
		}
	}

	@Override
	public GameObject generate(Random random) {
		return generator.generate(random);
	}

	protected OrthographicCamera getCamera() {
		return camera;
	}

	protected Generator<GameObject> getGenerator() {
		return generator;
	}

	protected Layer getLayer() {
		return layer;
	}

	protected Random getRandom() {
		return random;
	}

	protected Array<GameObject> getSpawns() {
		return spawns;
	}

	private int getNewHorizontalPosition(int width) {
		final int min = minHorizontalPosition;
		final int max = maxHorizontalPosition;

		int position;
		if (min == max) {
			position = min - width;
		} else {
			position = random.nextInt(Math.abs((max - width) - min)) + min;
		}

		if (flipTexture) {
			position += width;
		}
		return position;
	}

	private boolean previousSpawnHasBeenVisible() {
		if (spawns.size == 0) {
			return false;
		}
		final GameObject prevSpawn = spawns.get(spawns.size - 1);
		final float prevY = prevSpawn.getPosition().y;
		final float prevHeight = prevSpawn.getHeight();
		return prevY + prevHeight < camera.position.y + camera.viewportHeight / 2;
	}

	/**
	 * Sets the chance of spawning a game object. A random float is generated
	 * from {@code 0} to {@code 1} when deciding whether to spawn a new game
	 * object, if the result is less than the value given, a new object is
	 * spawned.
	 */
	public void setChanceOfSpawn(float chanceOfSpawn) {
		this.chanceOfSpawn = chanceOfSpawn;
	}

	/**
	 * Sets the extra amount of space between spawned game objects vertically.
	 * The distance between each spawned game object will be at least the value
	 * given.
	 */
	public void setExtraVerticalSpaceBetweenSpawns(int extraVerticalSpaceBetweenSpawns) {
		this.extraVerticalSpaceBetweenSpawns = extraVerticalSpaceBetweenSpawns;
	}

	/**
	 * Whether or not to flip the texture of spawned game objects.
	 */
	public void setFlipTexture(boolean flipTexture) {
		this.flipTexture = flipTexture;
	}

	/**
	 * Sets the range from which a horizontal position will be picked.
	 */
	public void setHorizontalPositionRange(int min, int max) {
		this.minHorizontalPosition = min;
		this.maxHorizontalPosition = max;
	}

	/**
	 * Sets the time before any new spawns can be attempted.
	 */
	public void setTimeBetweenSpawns(float timeBetweenSpawns) {
		this.timeBetweenSpawns = timeBetweenSpawns;
	}

	public void update(float delta) {
		timeSinceSpawn += delta;
		if (timeSinceSpawn >= timeBetweenSpawns && (spawns.size == 0 || previousSpawnHasBeenVisible())
				&& random.nextFloat() < chanceOfSpawn) {
			layer.addGameObject(createNewSpawn());
			timeSinceSpawn -= timeBetweenSpawns;
			deleteOldStructures();
		}
	}
}
