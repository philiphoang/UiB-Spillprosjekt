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

import java.util.Objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import uib.teamdank.common.util.CType;

public class PlayerBodyCreator {

	private static final float GROUND_DETECTOR_HEIGHT = 4;
	
	// These constants set the size of the colliders for
	// the players, which makes movement unified and not
	// different on different sized skins.
	private static final float BODY_WIDTH = 96f; 
	private static final float BODY_HEIGHT = 128f;
	private static final float FEET_RADIUS = 48f;
	
	private final Level level;
	private final World world;

	public PlayerBodyCreator(Level level) {
		this.level = Objects.requireNonNull(level, "level cannot be null");
		this.world = level.getWorld();
	}
	
	private void addBodyCollider(Player player, Body body) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((BODY_WIDTH / 2f) * player.getScale(),
						(BODY_HEIGHT / 2f) * player.getScale(),
						new Vector2(0,
								(BODY_HEIGHT / 2f) * player.getScale()
								+ (FEET_RADIUS * 2f) * player.getScale()),
						0);
		body.createFixture(createPlayerFixtureDef(shape));
		shape.dispose();
	}
	
	private void addFeetCollider(Player player, Body body) {
		CircleShape shape = new CircleShape();
		final float radius = FEET_RADIUS * player.getScale();
		shape.setRadius(radius);
		shape.setPosition(new Vector2(0, radius));
		body.createFixture(createPlayerFixtureDef(shape));
		shape.dispose();
	}
	
	private void addGroundDetector(Player player, Body body) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(player.getWidth() / 3, GROUND_DETECTOR_HEIGHT, new Vector2(0f, 0), 0);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		fixtureDef.filter.categoryBits = CType.CATEGORY_PLAYER.getValue();
		fixtureDef.filter.maskBits = (short) (CType.CATEGORY_PROJECTILE.getValue() | CType.CATEGORY_WORLD.getValue());
		body.createFixture(fixtureDef).setUserData(player);
		shape.dispose();
	}
	
	private FixtureDef createPlayerFixtureDef(Shape shape) {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0.0065f;
		fixtureDef.friction = 0.75f;
		fixtureDef.restitution = 0.1f;
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = CType.CATEGORY_PLAYER.getValue();
		fixtureDef.filter.maskBits = (short) (CType.CATEGORY_PROJECTILE.getValue() | CType.CATEGORY_WORLD.getValue());
		return fixtureDef;
	}
	
	public void initializeBody(Player player) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = .01f;
		Body body = world.createBody(bodyDef);
		
		player.setScale(.2f * level.getSizeRatio());

		body.setUserData(player);

		addGroundDetector(player, body);
		addFeetCollider(player, body);
		addBodyCollider(player, body);
		player.setBody(body);
	}

}
