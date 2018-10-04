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
package uib.teamdank.common.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uib.teamdank.common.Game;
import uib.teamdank.common.GameObject;
import uib.teamdank.common.util.TimedEvent;

/**
 * Screen for the main gameplay of a game.
 * <p>
 * Weather data, procedural generation and other nifty classes can be found in
 * the {@link uib.teamdank.common.util}-package.
 */
public class GameScreen implements Screen {

	private final List<Layer> layers = new ArrayList<>();
	private final List<TimedEvent> timedEvents = new ArrayList<>();

	private final Game game;

	public GameScreen(Game game) {
		Objects.requireNonNull(game, "game cannot be null");
		this.game = game;
	}

	public void addGameObject(Layer layer, GameObject gameObject) {
		layer.addGameObject(gameObject);
	}
	
	public void addLayer(Layer layer) {
		Objects.requireNonNull(layer, "layer cannot be null");
		layers.add(layer);
	}
	
	public void addTimedEvent(TimedEvent event) {
		this.timedEvents.add(Objects.requireNonNull(event, "timed event cannot be null"));
	}
	
	@Override
	public void dispose() {
		forEachGameObject(gameObject -> {
			gameObject.getTexture().getTexture().dispose();
		});
	}
	
	/**
	 * Loops through every game object on every layer starting with the first
	 * layer and passes the game objects to the given action.
	 */
	public void forEachGameObject(Consumer<GameObject> action) {
		for (Layer layer : layers) {
			layer.forEachGameObject(action);
		}
	}

	public Game getGame(){
		return game;
	}

	@Override
	public void hide() {
		// Not necessary as of yet
	}

	/**
	 * Pauses the game and shows a {@link PauseMenuScreen}.
	 */
	public void pause() {
		game.setScreen(game.getPauseMenuScreen());
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
		
		update(delta);
		
		SpriteBatch batch = game.getSpriteBatch();
		batch.begin();
		layers.forEach(layer -> {
			layer.preRender(batch, delta);
			layer.forEachGameObject(gameObject -> {
				gameObject.render(batch, delta);
			});
		});
		batch.end();
		 
	}
	
	@Override
	public void resize(int width, int height) {
		// Not necessary as of yet
	}

	@Override
	public void resume() {
		// Not necessary as of yet
	}

	@Override
	public void show() {
		// Not necessary as of yet
	}

	public void update(float delta) {
		
		// Update game objects
		for (Layer layer : layers) {
			layer.forEachGameObject(gameObject -> {
				gameObject.update(delta);
				onUpdateGameObject(delta, layer, gameObject);
			});
			layer.removeMarkedGameObjects();
		}
		
		// Update timed events
		Iterator<TimedEvent> eventIterator = timedEvents.iterator();
		while (eventIterator.hasNext()) {
			TimedEvent event = eventIterator.next();
			if (event.isDone()) {
				eventIterator.remove();
			} else {
				event.update(delta);
			}
		}
				
	}

	protected void onUpdateGameObject(float delta, Layer layer, GameObject gameObject) {
		// Can be overridden to perform operations before an update
	}

}