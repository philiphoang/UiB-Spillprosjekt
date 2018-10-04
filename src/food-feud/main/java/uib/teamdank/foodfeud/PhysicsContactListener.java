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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class PhysicsContactListener implements ContactListener {

	private final Map<String, Integer> activePlayerGroundContactCounts = new HashMap<>();

	public PhysicsContactListener(Match match) {
		Objects.requireNonNull(match, "match cannot be null");
		match.getPlayers().forEach(player -> {
			activePlayerGroundContactCounts.put(player.getName(), 0);
		});
	}

	@Override
	public void beginContact(Contact contact) {
		Object userDataA = contact.getFixtureA().getBody().getUserData();
		Object userDataB = contact.getFixtureB().getBody().getUserData();

		if (userDataA instanceof Projectile && userDataB instanceof Player) {
			projectileHit((Projectile) userDataA, (Player) userDataB);
		}
		else if(userDataA instanceof Player && userDataB instanceof Projectile) {
			projectileHit((Projectile) userDataB, (Player) userDataA);
		}
		else if(userDataA instanceof Projectile && userDataB instanceof Level) {
			projectileHitGround((Projectile) userDataA);
		}
		else if(userDataA instanceof Level && userDataB instanceof Projectile) {
			projectileHitGround((Projectile) userDataB);
		}
		else {
			updatePlayerGroundStatus(contact.getFixtureA(), contact.getFixtureB(), false);
		}
	}

	private void projectileHitGround(Projectile projectile) {
		projectile.startRemoveTimer();
	}

	private void projectileHit(Projectile projectile, Player player) {
		if(projectile.playerFired() != player) {
			projectile.markForRemoval();
			player.decreaseHealth(projectile.getDamage());
		}
	}

	@Override
	public void endContact(Contact contact) {
		updatePlayerGroundStatus(contact.getFixtureA(), contact.getFixtureB(), true);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// Not necessary
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// Not necessary
	}

	/**
	 * Updates the every player's "touching the ground" status by examining the
	 * potential contact between the player and the giwven fixtures. Nothing will
	 * happen if none of the fixtures belong to the player.
	 * <p>
	 * Since the player's ground sensor can collide with more than fixture at
	 * the same time, a count ({@link #activePlayerGroundContactCount}) is kept
	 * to properly update the player.
	 */
	private void updatePlayerGroundStatus(Fixture fixtureA, Fixture fixtureB, boolean endContact) {
		final Object userDataA = fixtureA.getUserData();
		final Object userDataB = fixtureB.getUserData();
		if (!(userDataA instanceof Player) && !(userDataB instanceof Player)) {
			return;
		}
		Player player = (Player) (userDataA instanceof Player ? userDataA : userDataB);
		final Map<String, Integer> counts = activePlayerGroundContactCounts;
		counts.put(player.getName(), counts.get(player.getName()) + (endContact ? -1 : 1));
		player.setOnGround(counts.get(player.getName()) != 0);
	}

}
