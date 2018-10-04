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
package uib.teamdank.cargame.gui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import uib.teamdank.cargame.CarGame;
import uib.teamdank.cargame.Pedestrian;
import uib.teamdank.cargame.Player;
import uib.teamdank.cargame.RoadEntity;
import uib.teamdank.cargame.util.*;
import uib.teamdank.common.Game;
import uib.teamdank.common.GameObject;
import uib.teamdank.common.Score;
import uib.teamdank.common.gui.Layer;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.TextureAtlas;
import uib.teamdank.common.util.TimedEvent;
import uib.teamdank.common.util.WeatherData.WeatherType;

/**
 * The main gameplay screen.
 */
public class GameScreen extends uib.teamdank.common.gui.GameScreen {
	private static final String SCORES = "TeamDank/Carl the Crasher/highscore.json";
	private static final int AMOUNT_NEW_HIGHSCORE_MESSAGES = 4;
	
	private static final int AMOUNT_PER_SCORE = 1;
	private static final float TIME_BETWEEN_SCORE = 1f;

	private static final int AMOUNT_PER_FUEL_LOSS = 1;
	private static final float TIME_BETWEEN_FUEL_LOSS = .2f;

	private static final int CAR_VERTICAL_POSITION = 25;
	
	private static final String MUSIC_TRACK = "Tracks/happy_bgmusic.wav";
	private static final String ENGINE_TRACK = "Tracks/car_drive.wav";

	private final AssetManager assets;

	private final OrthographicCamera playerCamera;
	private final OrthographicCamera screenCamera;

	private final BackgroundLayer backgroundLayer;
	private final Layer weatherLayer;
	private final Layer roadEntityLayer;
	private final Layer pedestrianLayer;
	private final Layer carLayer;
	private final CarHud hud;

	private final Player player;

	private final ScrollingSpawner pedestrianSpawner;
	private final ScrollingSpawner roadEntitySpawner;
	private List<Score> score;
	private int numTimesNewHighscoreMessage;
	private TimedEvent onOffNewHighscoreMessage;
	
	private WeatherType wType;
	private ScrollingSpawner weatherSpawner;

	public GameScreen(Game game) {
		super(game);

		this.assets = new AssetManager();
		TextureAtlas carTextures = assets.getAtlas("Images/car_sheet.json");
		TextureAtlas roadEntityTextures = assets.getAtlas("Images/road_entity_sheet.json");
		TextureAtlas pedestrianTextures = assets.getAtlas("Images/pedestrian_sheet.json");
		
		// Cameras
		this.playerCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.screenCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Player initialization
		player = ((CarGame) game).getPlayer();
		player.reset();
		if (player.getTexture() == null) {
			player.unlockSkin("car_forward");
			player.setTexture(carTextures.getRegion("car_forward"));
		}
		player.setScale(.5f);
		addTimedEvent(new TimedEvent(TIME_BETWEEN_FUEL_LOSS, true, () -> {
			player.decreaseHealth(AMOUNT_PER_FUEL_LOSS);
		}));
		addTimedEvent(new TimedEvent(TIME_BETWEEN_SCORE, true, () -> {
			player.getScore().addToScore(AMOUNT_PER_SCORE);
		}));

		// Weather
		wType = ((CarGame) game).getStartMenuScreen().getWeather();
		// Don't know which one to be used:
		player.setWeatherType(wType);
		
		// Layers
		backgroundLayer = new BackgroundLayer(assets, playerCamera, screenCamera, player, wType);
		weatherLayer = new Layer(false);
		roadEntityLayer = new Layer(true);
		pedestrianLayer = new Layer(true);
		carLayer = new Layer(true);
		addLayer(backgroundLayer);
		addLayer(roadEntityLayer);
		addLayer(pedestrianLayer);
		addLayer(carLayer);
		addLayer(weatherLayer);
		carLayer.addGameObject(player);

		// Road entity spawner initialization
		this.roadEntitySpawner = new ScrollingSpawner(roadEntityLayer, playerCamera,
				new RoadEntityGenerator(assets.getAudio(), roadEntityTextures, wType));
		roadEntitySpawner.setHorizontalPositionRange(backgroundLayer.getRoadLeftX(), backgroundLayer.getRoadRightX());
		roadEntitySpawner.setChanceOfSpawn(.01f);
		roadEntitySpawner.setExtraVerticalSpaceBetweenSpawns(50);

		// pedestrian spawner initialization
		this.pedestrianSpawner = new ScrollingSpawner(pedestrianLayer, playerCamera,
					new PedestrianGenerator(assets, pedestrianTextures, wType));
		pedestrianSpawner.setHorizontalPositionRange(backgroundLayer.getRoadLeftX(), backgroundLayer.getRoadRightX());
		if(wType == WeatherType.SUN)
			pedestrianSpawner.setChanceOfSpawn(.015f);
		else
			pedestrianSpawner.setChanceOfSpawn(.01f);
		pedestrianSpawner.setExtraVerticalSpaceBetweenSpawns(50);
		
		// Weather spawner initialization
		if(wType == WeatherType.SNOW || wType == WeatherType.RAIN) {
			weatherSpawner = new DownpourSpawner(weatherLayer, playerCamera, new WeatherGenerator(wType));
		}
		else {
			weatherSpawner = new ScrollingSpawner(weatherLayer, playerCamera, new WeatherGenerator(wType));
			weatherSpawner.setHorizontalPositionRange(-Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
		}
		weatherSpawner.setChanceOfSpawn(2f);

		// HUD
		this.hud = new CarHud();
		hud.setGame((CarGame) game);
		
		FileHandle handle = Gdx.files.external(SCORES);
		if(!handle.exists())
			handle = Gdx.files.internal("Data/highscore.json");
		score = new LinkedList<>(Arrays.asList(Score.createFromJson(handle)));
		numTimesNewHighscoreMessage = 0;
		onOffNewHighscoreMessage = new TimedEvent(0.5f, true, () -> {
			if(player.getScore().getScore() > score.get(0).getScore()) {
				hud.setVisibleNewHighscore(!hud.isVisibleNewHighscore());
				numTimesNewHighscoreMessage++;
				if(numTimesNewHighscoreMessage >= AMOUNT_NEW_HIGHSCORE_MESSAGES*2) {
					onOffNewHighscoreMessage.setLoop(false);
					hud.setVisibleNewHighscore(false);
				}
			}
		});
		addTimedEvent(onOffNewHighscoreMessage);
	}

	private boolean checkForPauseRequest() {
		final boolean pause = Gdx.input.isKeyJustPressed(Keys.ESCAPE);
		if (pause) {
			getGame().setScreen(getGame().getPauseMenuScreen());
		}
		return pause;
	}

	/*
	private void checkForMute() {
		if(hud.isMuted()) {
			assets.getAudio().mute();
		} else {
			assets.getAudio().unmute();
		}
	}
	*/
	
	public boolean isMuted() {
		if(hud.isMuted())
			return true;
		return false;
	}
	
	public void setMuted(boolean bool){
		if(bool == true){
			assets.getAudio().mute();
		}
		else if (bool == false){
			assets.getAudio().unmute();
		    assets.getAudio().loopSound(MUSIC_TRACK);
		    assets.getAudio().loopSound(ENGINE_TRACK);
		}
	}
	
	public void setStartAudio(boolean isMuted) {
		hud.setMute(isMuted);
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
	}

	@Override
	public void hide() {
		assets.getAudio().pauseAll();
	}

	@Override
	protected void onUpdateGameObject(float delta, Layer layer, GameObject gameObject) {
	
		if (layer == roadEntityLayer || layer == pedestrianLayer) {
			if (gameObject instanceof RoadEntity && player.contains(gameObject)) {
				((RoadEntity) gameObject).drivenOverBy(player);
			}
		}
		
	}

	@Override
	public void render(float delta) {

		
		// Render layers
		super.render(delta);

		// Render HUD
		hud.render(delta);

	}

	@Override
	public void resize(int width, int height) {
		playerCamera.viewportWidth = screenCamera.viewportWidth = width;
		playerCamera.viewportHeight = screenCamera.viewportHeight = height;
		playerCamera.update();
		screenCamera.update();
	}

	@Override
	public void show() {
		hud.setAsInputProcessor();

		assets.getAudio().loopSound(MUSIC_TRACK);
		assets.getAudio().loopSound(ENGINE_TRACK);
	}
	
	@Override
	public void update(float delta) {

		// Update HUD
		updateHUD();

		// Updates game objects
		super.update(delta);
		player.restrictHorizontally(backgroundLayer.getRoadLeftX(), backgroundLayer.getRoadRightX());
		
		// Player input
		checkForPauseRequest();
		//checkForMute();
		boolean inputTurnLeft = Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT);
		boolean inputTurnRight = Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT);
		if (inputTurnLeft) {
			player.turnLeft();
		}
		if (inputTurnRight) {
			player.turnRight();
		}

		// Update player camera
		Vector2 playerPos = player.getPosition();
		float cameraX = playerPos.x + player.getWidth() / 2;
		float cameraY = playerPos.y + Gdx.graphics.getHeight() / 2 - CAR_VERTICAL_POSITION;
		playerCamera.position.set(cameraX, cameraY, 0);
		playerCamera.update();
		getGame().getSpriteBatch().setProjectionMatrix(playerCamera.combined);

				
		// Spawn new road entities
		roadEntitySpawner.update(delta);
		roadEntitySpawner.setHorizontalPositionRange(backgroundLayer.getRoadLeftX(), backgroundLayer.getRoadRightX());

		// Spawn new pedestrians
		pedestrianSpawner.update(delta);
		pedestrianSpawner.setHorizontalPositionRange(backgroundLayer.getRoadLeftX(), backgroundLayer.getRoadRightX());
		pedestrianLayer.forEachGameObject(obj ->
			((Pedestrian) obj).restrictHorizontally(backgroundLayer.getRoadLeftX(), backgroundLayer.getRoadRightX())
		);

		// Spawn new weather objects
		weatherSpawner.update(delta);

		// Check for game over
		if (player.isOutOfFuel() && player.getVelocity().y == 0) {
			getGame().setScreen(new EndingScreen((CarGame) getGame()));
		}
		
	}

	private void updateHUD() {
		hud.setCurrentFuel(player.getHealth(), player.getMaxHealth());
		hud.setScore(player.getScore().getScore());
		hud.setCoins(player.getInventory().getGold());
	}
	
	public void setWeather (WeatherType w) {
		wType = w;
	}
	
}