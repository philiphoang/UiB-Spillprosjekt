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
package uib.teamdank.foodfeud.gui;

import java.util.Date;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import uib.teamdank.common.Game;
import uib.teamdank.common.GameObject;
import uib.teamdank.common.gui.Layer;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.TimedEvent;
import uib.teamdank.foodfeud.FoodFeud;
import uib.teamdank.foodfeud.Level;
import uib.teamdank.foodfeud.LevelLoader;
import uib.teamdank.foodfeud.Match;
import uib.teamdank.foodfeud.PhysicsContactListener;
import uib.teamdank.foodfeud.PhysicsSimulated;
import uib.teamdank.foodfeud.Player;
import uib.teamdank.foodfeud.PlayerBodyCreator;

/**
 * The main gameplay screen.
 */
public class GameScreen extends uib.teamdank.common.gui.GameScreen {

	private static final String MUSIC_TRACK = "Music/happy_bgmusic.wav";
	private final BackgroundLayer backgroundLayer;
	private final Layer playerLayer;
	private final ForegroundLayer foregroundLayer;

	private final OrthographicCamera camera;
	private final Level level;
	private final Match match;

	private static final float TIME_BETWEEN_TIME = 1f;
	private static final int AMOUNT_PER_TIME = 1;

	private static final int TIME_TO_CHANGE = 5;
	private boolean isWaiting;
	private float waitingTime;
	private boolean hasShot;

	private static final int FINAL_TIME = 15;
	private int time;

	private final FoodHud hud;
	private final AssetManager assets;

	private boolean touched = false;
	private long startTime;
	private long elapsedTime;

	public GameScreen(Game game) {
		super(game);

		this.assets = ((FoodFeud) game).getSetupGame().getAssets();
		time = FINAL_TIME;
		isWaiting = false;
		hasShot = false;
		waitingTime = 5;

		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		this.level = LevelLoader.createFromJson(Gdx.files.internal("Data/field_level.json"));

		this.match = ((FoodFeud) game).getSetupGame().getMatch();

		level.getWorld().setContactListener(new PhysicsContactListener(match));

		camera.position.set(level.getWidth() / 2f, level.getHeight() / 2f, 0);
		camera.zoom = .5f * level.getSizeRatio();

		this.backgroundLayer = new BackgroundLayer(level);
		addLayer(backgroundLayer);
		this.playerLayer = new Layer(true);
		addLayer(playerLayer);
		this.foregroundLayer = new ForegroundLayer(level);
		addLayer(foregroundLayer);

		// HUD
		this.hud = new FoodHud();
		hud.setGame((FoodFeud) game);

		addTimedEvent(new TimedEvent(TIME_BETWEEN_TIME, true, () -> {
			time -= AMOUNT_PER_TIME;
		}));

		PlayerBodyCreator playerBodyCreator = new PlayerBodyCreator(level);
		for (Player player : match.getPlayers()) {
			playerBodyCreator.initializeBody(player);
			playerLayer.addGameObject(player);
		}
		level.distributePlayers(match.getPlayers());

	}

	private void checkPauseRequest() {
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			getGame().setScreen(getGame().getPauseMenuScreen());
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		assets.dispose();
	}

	@Override
	public void render(float delta) {
		getGame().getSpriteBatch().setProjectionMatrix(camera.combined);
		super.render(delta);

		// Render HUD
		hud.render(delta);

	}

	@Override
	public void show() {
		hud.setAsInputProcessor();

		assets.getAudio().loopSound(MUSIC_TRACK);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
	}

	@Override
	public void hide() {
		assets.getAudio().pauseAll();
	}

	@Override
	public void update(float delta) {

		final Player activePlayer = match.getActivePlayer();

		camera.position.set(activePlayer.getX(), activePlayer.getY(), 0f);
		camera.position.add(activePlayer.getWidth() / 2f, activePlayer.getHeight() / 2f, 0f);
		camera.update();

		// Update game objects
		super.update(delta);
		level.updateWorld();

		checkForMute();
		if (!isWaiting) {
			// User input
			checkPauseRequest();
			if (!activePlayer.isDead()) {
				movement(activePlayer);
			}
			// Prevent players exiting world
			for (Player player : match.getPlayers()) {
				if (player.getX() < 0) {
					player.moveRight(10);
				} else if (player.getX() > level.getWidth() - player.getWidth()) {
					player.moveLeft(10);
				}
			}

			match.checkForDead();
			checkVictory();

			// Temporary
			tryToShoot(activePlayer);

			if (Gdx.input.isKeyJustPressed(Keys.N)) {
				time = FINAL_TIME;
				match.nextTurn();
			}
		}

		checkTimeorDead(activePlayer);

		if (isWaiting) {
			hud.setInvisibleText(true);
			waitingTime = waitingTime - delta;
			hud.setTime((int) waitingTime);
		}
	}

	private void tryToShoot(Player activePlayer) {
		if (Gdx.input.isKeyJustPressed(Keys.SPACE) && match.CURRENT_AMMO_COUNT > 0 && !hasShot) {
			Vector3 aim3D = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			Vector2 aim = new Vector2(aim3D.x, aim3D.y);
			aim.sub(activePlayer.getPosition());
			fireMyWeapon(activePlayer, aim);
		}
		if (Gdx.input.justTouched() && !hud.weaponsAreVisible() && !hasShot) {

			startTime = System.currentTimeMillis();
			elapsedTime = 0;
			touched = true;
		}

		if (!Gdx.input.isTouched() && touched && match.CURRENT_AMMO_COUNT > 0) {
			elapsedTime = (new Date()).getTime() - startTime;
			Vector3 aim3D = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			Vector2 aim = new Vector2(aim3D.x, aim3D.y);
			aim.sub(activePlayer.getPosition());
			fireMyWeapon(activePlayer, aim);
			touched = false;
		}
	}

	private void fireMyWeapon(Player activePlayer, Vector2 aim) {
		boolean shot = activePlayer.fireWeapon(this, playerLayer, level.getWorld(), aim.nor(), elapsedTime * 100);
		if (shot) {
			shootingRecoil(activePlayer);
			match.decreaseCurrentAmmo(1);
			if (match.CURRENT_AMMO_COUNT == 0) {
				hasShot = true;
				time = 5;
			}
		}
	}

	private void shootingRecoil(Player activePlayer) {
		activePlayer.jump();
		activePlayer.jump();
		activePlayer.jump();
		activePlayer.jump();
		System.out.println(Gdx.input.getX() + ", " + camera.viewportWidth / 2);
		if (Gdx.input.getX() > camera.viewportWidth / 2)
			activePlayer.moveLeft(5);

		if (Gdx.input.getX() < camera.viewportWidth / 2)
			activePlayer.moveRight(5);

		activePlayer.walking = true;
	}

	private void movement(Player active) {
		if (Gdx.input.isKeyJustPressed(Keys.W) || Gdx.input.isKeyJustPressed(Keys.UP)) {
			active.jump();
		}
		if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
			if ((active.getBody().getLinearVelocity().x) > (-Player.MAX_VEL_X)) {
				active.moveLeft();
			}
			active.walking = true;

		}
		if ((Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT))) {
			if ((active.getBody().getLinearVelocity().x) < Player.MAX_VEL_X) {
				active.moveRight();
			}
			active.walking = true;
		}
	}

	@Override
	protected void onUpdateGameObject(float delta, Layer layer, GameObject gameObject) {

		// Dispose of physics bodies on deleted objects
		if (gameObject.isMarkedForRemoval() && gameObject instanceof PhysicsSimulated) {
			level.getWorld().destroyBody(((PhysicsSimulated) gameObject).getBody());
		}
	}

	public void setStartAudio(boolean isMuted) {
		hud.setMute(isMuted);
	}

	private void checkForMute() {
		if (hud.isMuted()) {
			assets.getAudio().mute();
		} else {
			assets.getAudio().unmute();
		}
	}

	public boolean isMuted() {
		return hud.isMuted();
	}

	/**
	 * checks if time has run out, forces new round if true
	 */
	public void checkTimeorDead(Player active) {
		hud.setTime(time);
		if (time == 0 && !isWaiting) {
			hud.setTime(TIME_TO_CHANGE);
			isWaiting = true;
		}
		if (waitingTime < 0 && isWaiting) {
			time = FINAL_TIME;
			isWaiting = false;
			hud.setInvisibleText(false);
			waitingTime = 5;
			hasShot = false;
			match.nextTurn();
		}
		if (active.isDead()) {
			time = FINAL_TIME;
			hud.setInvisibleText(false);
			hasShot = false;
			match.nextTurn();
		}
	}

	public Match getMatch() {
		return match;
	}

	public void checkVictory() {
		Player player = match.getWinner();
		if (player != null)
			getGame().setScreen(new EndingScreen((FoodFeud) getGame()));
	}

	public Player getCurrentPlayer() {
		return match.getActivePlayer();
	}
}