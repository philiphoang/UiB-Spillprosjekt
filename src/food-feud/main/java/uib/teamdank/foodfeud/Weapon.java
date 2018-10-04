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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import uib.teamdank.common.Item;
import uib.teamdank.common.gui.Layer;
import uib.teamdank.foodfeud.gui.GameScreen;

/**
 * Represents a weapon item. A weapon can be thrown (distance depends of on
 * weight and thrower) and inflicts damage on impact.
 */
public class Weapon extends Item {

	public enum Type {
		// light bullets like guns
		LIGHT_BALLISTIC,
		// like uzi, HK416.. etc
		BURST_BALLISTIC,
		// arching ballistics like rpg and grenades..(only exploding one)
		HEAVY_BALLISTIC,
		// shotgun etc
		SPRAY_BALLISTIC;
	}

	private final int damage;
	private final Type type;

	private float mass;
	private boolean damagedPlayer = false;

	/**
	 * Creates a weapon that does no damage and weighs nothing.
	 * 
	 * @param name
	 * @param descr
	 *            weapon description
	 */
	public Weapon(String name, String descr, TextureRegion texture, int damage, float mass, Type type) {
		super(name, descr);
		setTexture(texture);
		this.damage = damage;
		this.mass = mass;
		this.type = type;
	}

	public void fire(GameScreen game, Weapon weapon, Player player, Layer layer, World world, Vector2 dir, long elapsedTime) {
			ProjectileSpawner spawner = new ProjectileSpawner();
			spawner.spawn(game, weapon, layer, world, player, dir, player.getX() + player.getWidth(),
					player.getY() + player.getHeight(), elapsedTime);
		}
	
	public int getDamage() {
		return damage;
	}

	public float getMass() {
		return mass;
	}

	public Type getType() {
		return type;
	}

	public void hitPlayer(Player player) {
		if (!damagedPlayer) {
			player.decreaseHealth(getDamage());
			this.markForRemoval();
			damagedPlayer = true;
		}
	}

}