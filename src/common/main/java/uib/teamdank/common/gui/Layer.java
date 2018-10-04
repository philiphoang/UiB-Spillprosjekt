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
package uib.teamdank.common.gui;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import uib.teamdank.common.GameObject;

/**
 * A layer of {@link GameObject}s.
 */
public class Layer {
	private final Array<GameObject> gameObjects = new Array<>();

	private boolean solid;

	public Layer(boolean solid) {
		setSolid(solid);
	}

	public void addGameObject(GameObject object) {
		Objects.requireNonNull(object, "game object cannot be null");
		gameObjects.add(object);
	}

	/**
	 * Loops through every game object in this layer and passes it to the
	 * specified consumer.
	 */
	public void forEachGameObject(Consumer<GameObject> action) {
		gameObjects.forEach(action);
	}

	/**
	 * @return returns the array of game objects in the layer
	 */
	public Array<GameObject> getAllObjects() {
		return gameObjects;
	}

	/**
	 * @return amount of game objects in this layer
	 */
	public int getSize() {
		return gameObjects.size;
	}
	
	/**
	 * @return whether this layer is checked for collision
	 */
	public boolean isSolid() {
		return solid;
	}
	
	/**
	 * This method is called before the rendering of the game
	 * objects in this layer ({@link #render(SpriteBatch, float)}.
	 */
	public void preRender(SpriteBatch batch, float delta) {
		// Can be overridden
	}

	/**
	 * Removes every game object in this layer that is marked for removal.
	 */
	public void removeMarkedGameObjects() {
		
		// Use iterator instead of for-loop to prevent
		// modification of list while looping through
		Iterator<GameObject> it = gameObjects.iterator();
		while (it.hasNext()) {
			GameObject obj = it.next();
			if (obj.isMarkedForRemoval()) {
				it.remove();
			}
		}
		
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}
}
