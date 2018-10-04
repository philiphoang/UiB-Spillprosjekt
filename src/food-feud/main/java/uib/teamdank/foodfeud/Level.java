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
package uib.teamdank.foodfeud;

import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

public class Level implements Disposable {
	
	private static final int PLAYER_POSITION_MARGIN = 25;
	
	private final String name;
	private final World world;
	private final float sizeRatio;
	private final Texture background;
	private final Texture foreground;
	
	public Level(String name, World world, float sizeRatio, Texture background, Texture foreground) {
		if (background.getWidth() != foreground.getWidth()) {
			throw new IllegalArgumentException("foreground must be as wide as background");
		}
		if (background.getHeight() != foreground.getHeight()) {
			throw new IllegalArgumentException("foreground must be as tall as background");
		}
		
		this.name = Objects.requireNonNull(name, "name cannot be null");
		this.world = Objects.requireNonNull(world, "world cannot be null");
		this.sizeRatio = sizeRatio;
		this.background = Objects.requireNonNull(background, "background cannot be null");
		this.foreground = Objects.requireNonNull(foreground, "foreground cannot be null");
	}
	
	public float getSizeRatio() {
		return sizeRatio;
	}
	
	@Override
	public void dispose() {
		background.dispose();
		foreground.dispose();
		world.dispose();
	}
	
	public void distributePlayers(List<Player> players) {
		float stepX = (getWidth() - PLAYER_POSITION_MARGIN * 2f) / (players.size());
		for (int i = 0; i < players.size(); i++) 
		{
			final Body body = players.get(i).getBody();
			if (body == null) {
				throw new IllegalStateException("player does not have a body");
			}
			
			final Vector2 pos = body.getPosition();
			pos.y = getHeight() / 2f;
			pos.x = PLAYER_POSITION_MARGIN + (stepX * (i + .5f));
			body.setTransform(pos, 0);
		}
	}
	
	public Texture getBackground() {
		return background;
	}
	
	public Texture getForeground() {
		return foreground;
	}
	
	public int getHeight() {
		return (int) (getBackground().getHeight() * sizeRatio);
	}
	
	public String getName() {
		return name;
	}
	
	public void updateWorld() {
		world.step(1 / 12f, 6, 4);
	}
	
	public int getWidth() {
		return (int) (getBackground().getWidth() * sizeRatio);
	}
	
	public World getWorld() {
		return world;
	}
	
}
