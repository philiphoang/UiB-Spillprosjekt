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

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uib.teamdank.common.Game;
import uib.teamdank.foodfeud.gui.GameScreen;
import uib.teamdank.foodfeud.gui.PauseMenuScreen;
import uib.teamdank.foodfeud.gui.SetupGame;
import uib.teamdank.foodfeud.gui.StartMenuScreen;

/**
 * The main game class for Food Feud.
 */
public class FoodFeud extends Game {
	private static final String TITLE = "Food Feud";
	private StartMenuScreen startMenuScreen;
    private GameScreen gameScreen;
    private SetupGame setupScreen;
	private PauseMenuScreen pauseMenuScreen;
	private SpriteBatch batch;
	private boolean isMuted;
	

    public static void main(String[] args){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = TITLE;
        config.width = 1280;
        config.height = 720;
        new LwjglApplication(new FoodFeud(), config) ;
    }

	@Override
	public void create() {
		startMenuScreen = new StartMenuScreen(this);
		pauseMenuScreen = new PauseMenuScreen(this);
		batch = new SpriteBatch();
		setScreen(startMenuScreen);
	}

	@Override
	public GameScreen newGame() {
		gameScreen = new GameScreen(this);
		gameScreen.setStartAudio(isMuted);
		return gameScreen;
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
	
	@Override 
	public void dispose() {
		super.dispose();
		batch.dispose();
		screen.dispose();
	}
	
	public SetupGame getSetupGame() {
		return setupScreen;
	}
	
	public SetupGame newSetupGame() {
		setupScreen = new SetupGame(this);
		return setupScreen;
	}

	public void setAudio(boolean b) {
		isMuted = b;
	}
}
