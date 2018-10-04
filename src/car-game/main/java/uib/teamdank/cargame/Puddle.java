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

import uib.teamdank.common.Item;
import uib.teamdank.common.util.AudioManager;
import uib.teamdank.common.util.TextureAtlas;
import uib.teamdank.common.util.WeatherData.WeatherType;
/**
 * A puddle in the road. Has negative effects if driven over by the
 * {@link Player}.
 */
public class Puddle extends Item implements RoadEntity {
	
	private static final String SOUND = "Sounds/splash.mp3";
	private static final String TEXTURE_REGION = "puddle";
	
	private static final float PLAYER_VELOCITY_MODIFIER = 1.1f;
	private static final int FUEL_PENALTY = 15;
	
	private final AudioManager audioManager;
	private boolean wasDrivenOver = false;
	
	public Puddle(AudioManager audioManager, TextureAtlas atlas, WeatherType wType) {
        super("Puddle", "A minitaure pond in the middle of the road.");
        this.audioManager = audioManager;
        if(wType == WeatherType.SNOW)
        	setTexture(atlas.getRegion(TEXTURE_REGION + "_snow"));
        else
        	setTexture(atlas.getRegion(TEXTURE_REGION));
        setScale(.4f);
        
        audioManager.preloadSounds(SOUND);
    }
    
    @Override
    public void drivenOverBy(Player player) {
    	if (!wasDrivenOver) {
    		player.decreaseHealth(getFuelPenalty());
    		audioManager.playSound(SOUND);
    		wasDrivenOver = true;
    	}
    	player.getVelocity().scl(PLAYER_VELOCITY_MODIFIER);
    }
    
    public int getFuelPenalty() {
        return FUEL_PENALTY;
    }
    
    public boolean wasDriverOven() {
		return wasDrivenOver;
	}

}
