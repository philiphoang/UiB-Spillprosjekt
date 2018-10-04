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

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A looping image ensures that a texture always covers the screen vertically
 * by rendering the texture multiple time when needed, effectively looping it.  
 */
public class LoopingBackground {
	private final OrthographicCamera camera;
	private final Texture texture;
	private final float scale;
	
	private int horizontalPosition;
	private int lowestY;
	
	public LoopingBackground(OrthographicCamera camera, Texture texture, float scale) {
		this.camera = camera;
		this.texture = texture;
		this.scale = scale;
		
		this.lowestY = -30;
	}
	
	public void updateHorizontalPosition(int x) {
		this.horizontalPosition = x;
	}
	
	public void render(SpriteBatch batch) {
		if (lowestY + getHeight() < camera.position.y - camera.viewportHeight / 2) {
			lowestY += getHeight();
		}
		for (int y = lowestY; y < camera.position.y + camera.viewportHeight / 2; y += getHeight()) {
			batch.draw(texture, horizontalPosition, y, getWidth(), getHeight());
		}
		
	}
	
	public int getWidth() {
		return (int) (texture.getWidth() * scale);
	}
	
	private int getHeight() {
		// Method is private because the background loops
		// vertically, which makes the height obsolete
		return (int) (texture.getHeight() * scale);
	}
}
