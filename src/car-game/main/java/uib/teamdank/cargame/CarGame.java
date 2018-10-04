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

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uib.teamdank.cargame.gui.GameScreen;
import uib.teamdank.cargame.gui.PauseMenuScreen;
import uib.teamdank.cargame.gui.StartMenuScreen;
import uib.teamdank.common.Game;
import uib.teamdank.common.util.WeatherData;


/**
 * The main game class for Car Game.
 */
public class CarGame extends Game {
	private static final String TITLE = "Carl the Crasher";
	private StartMenuScreen startMenuScreen;
	private GameScreen gameScreen;
	private PauseMenuScreen pauseMenuScreen;
	private SpriteBatch batch;
	
	private Player player;
	private WeatherData wData;
	
	private boolean isMuted;

    public static void main(String[] args){
    	LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = TITLE;
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new CarGame(), config) ;
    }
	
	@Override
	public void create() {
		player = Player.create();
		wData = WeatherData.create();
		
		startMenuScreen = new StartMenuScreen(this);
		pauseMenuScreen = new PauseMenuScreen(this);

		batch = new SpriteBatch();
		
		setScreen(startMenuScreen);
	}

	@Override
	public GameScreen getGameScreen() {
		return gameScreen;
	}

	@Override
	public PauseMenuScreen getPauseMenuScreen() {
		return pauseMenuScreen;
	}

	@Override
	public StartMenuScreen getStartMenuScreen() {
		return startMenuScreen;
	}
	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public SpriteBatch getSpriteBatch() {
		return batch;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	@Override 
	public void dispose() {
		super.dispose();
		batch.dispose();
		screen.dispose();
		player.saveAsJson();
	}

	@Override
	public GameScreen newGame() {
		gameScreen = new GameScreen(this);
		gameScreen.setStartAudio(isMuted);
		return gameScreen;
		
	}
	
	public void setAudio(boolean b) {
		isMuted = b;
	}
	
	public WeatherData getWeatherData(){
		return wData;
	}
}
