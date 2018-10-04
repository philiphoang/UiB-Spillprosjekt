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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.Gson;

import uib.teamdank.common.util.CType;

public class LevelLoader {

	private static class LevelModel {
		String name;
		String background;
		String foreground;
		float gravity;
		float sizeRatio;
		float[][] ground;
	}

	private LevelLoader() {
		// Hide constructor
	}

	public static Level createFromJson(FileHandle handle) {
		LevelModel model = new Gson().fromJson(handle.reader("UTF-8"), LevelModel.class);

		Texture background = new Texture(model.background);
		Texture foreground = new Texture(model.foreground);

		
		World world = new World(new Vector2(0, model.gravity), true);
		Level level = new Level(model.name, world, model.sizeRatio, background, foreground);
		createGroundFixtures(world, model, level, background.getHeight());
		
		// Maybe this will work
		return level;
	}

	private static void createGroundFixtures(World world, LevelModel level, Level levelObj, int height) {
		BodyDef groundDef = new BodyDef();
		groundDef.type = BodyType.StaticBody;
		Body ground = world.createBody(groundDef);
		ground.setUserData(levelObj);

		for (int i = 0; i < level.ground.length; i++) {

			for (int j = 0; j < level.ground[i].length; j++) {
				
				// Invert Y-axis
				if (j % 2 == 1) {
					level.ground[i][j] = height - level.ground[i][j];
				}
				
				// Scale coordinates
				level.ground[i][j] *= level.sizeRatio;
				
			}
			
			if (level.ground[i].length / 2 > 8) {
				throw new IllegalArgumentException("one ground polygon must contain 3-8 points");
			}

			PolygonShape shape = new PolygonShape();
			shape.set(level.ground[i]);
			ground.createFixture(shape, 0.0f);
			shape.dispose();
		}
		
		for (Fixture fix : ground.getFixtureList()){
			Filter filter = fix.getFilterData();
			filter.categoryBits = CType.CATEGORY_WORLD.getValue();
			filter.maskBits = (short) (CType.CATEGORY_PROJECTILE.getValue() | CType.CATEGORY_PLAYER.getValue());
			fix.setFilterData(filter);
			fix.setFriction(0.75f);
		}

	}

}