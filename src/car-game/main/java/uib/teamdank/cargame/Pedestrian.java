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

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uib.teamdank.common.Actor;
import uib.teamdank.common.util.Animation;
import uib.teamdank.common.util.AudioManager;

/**
 * A pedestrian in the road. Has positive effects if driven over by the
 * {@link Player}.
 */
public class Pedestrian extends Actor implements RoadEntity {

	private static final String SOUND = "Sounds/dead_pedestrian.mp3";

	private final AudioManager audioManager;
	private boolean wasDrivenOver = false;

	private final long scoreBonus;

	public Pedestrian(AudioManager audioManager, long scoreBonus, float vx, float vy, boolean goLeft, TextureRegion texture) {
		this(audioManager, scoreBonus, vx, vy, goLeft, Animation.createSingleFramed(texture));
	}
	
	public Pedestrian(AudioManager audioManager, long scoreBonus, float vx, float vy, boolean goLeft, Animation anim) {
		this.audioManager = audioManager;
		setAnimation(anim);
		setScale(.9f);
		
		getVelocity().set(vx, vy);
		if (goLeft) {
			getVelocity().x *= -1;
			setFlipHorizontally(true);
		}
		this.scoreBonus = scoreBonus;
		
		audioManager.preloadSounds(SOUND);
	}
	

	@Override
	public void drivenOverBy(Player player) {
		if (!wasDrivenOver) {
			player.getScore().addToScore(this.scoreBonus);
			audioManager.playSound(SOUND);
			wasDrivenOver = true;
		}
		this.markForRemoval();

	}
	
	public void restrictHorizontally(int minX, int maxX) {
		if (getPosition().x < minX) {
			getPosition().x = minX;
			getVelocity().x *= -1;
			setFlipHorizontally(false);
		} else if (getPosition().x > maxX-this.getWidth()) {
			getPosition().x = maxX-this.getWidth();
			getVelocity().x *= -1;
			setFlipHorizontally(true);
		}
	}
	
}
