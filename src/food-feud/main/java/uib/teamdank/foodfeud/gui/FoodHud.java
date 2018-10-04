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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import uib.teamdank.common.gui.MenuScreen;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.TextureAtlas;
import uib.teamdank.foodfeud.FoodFeud;
import uib.teamdank.foodfeud.Weapon;
import uib.teamdank.foodfeud.WeaponLoader;

public class FoodHud extends MenuScreen {

	private FoodFeud game;
	private Stage stage;
	private AssetManager assets;
	private ImageButton muteButton;

	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private BitmapFont font;
	private TextButtonStyle textButtonStyle;
	private TextButton time;

	private Table scoreTable;
	private Table muteTable;
	private Table weapons;
	private Table menu;

	private boolean muted = false;
	private boolean weaponDelay = false;

	private static final String MENU_PATH = "Images/Buttons/ff_menu.png";
	private ImageButton weaponMenuButton;
	private TextButton nextPlayerWait;
	private Table hiddenTable;

	public FoodHud() {
		stage = new Stage(new FitViewport(1920, 1080));

		this.assets = new AssetManager();

		menu = new Table();
		weapons = new Table();
		scoreTable = new Table();
		hiddenTable = new Table();

		generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/roboto.ttf"));
		parameter = new FreeTypeFontParameter();
		float dpi = Gdx.graphics.getDensity() + 1;
		parameter.size = (int) Math.ceil(50 * dpi);

		font = generator.generateFont(parameter);
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;

		nextPlayerWait = new TextButton("TIME TO NEXT PLAYER", textButtonStyle);
		hiddenTable.add(nextPlayerWait).pad(700, 0, 0, 900);

		time = new TextButton("0", textButtonStyle);
		scoreTable.add(time).width(300).pad(900, 0, 0, 1600);

		weaponMenuButton = setupButton(MENU_PATH);
		menu.add(weaponMenuButton).height((float) (weaponMenuButton.getHeight() / 4)).pad(980, 1670, 0, 0);

		setUpMute();
		addButtonListener();
		addToWeapons();
		setupStage();
	}

	private void setupStage() {
		hiddenTable.setFillParent(true);
		hiddenTable.setVisible(false);
		weapons.setFillParent(true);
		weapons.setVisible(false);
		scoreTable.setFillParent(true);
		menu.setFillParent(true);

		stage.addActor(hiddenTable);
		stage.addActor(weapons);
		stage.addActor(menu);
		stage.addActor(scoreTable);
		stage.addActor(muteTable);
	}

	public boolean isMuted() {
		return muted;
	}

	public void setAsInputProcessor() {
		Gdx.input.setInputProcessor(stage);
	}

	private void addToWeapons() {
		Weapon[] weaponsList = WeaponLoader.fromJson(assets, Gdx.files.internal("Data/weapons.json"));

		int i = 0;
		for (Weapon w : weaponsList) {
			i++;
			TextureRegion myTextureRegion = new TextureRegion(w.getTexture());
			TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
			ImageButton weaponButton = new ImageButton(myTexRegionDrawable);
			addButtonListener(weaponButton, () -> game.getGameScreen().getCurrentPlayer().setWeapon(w));
			weapons.add(weaponButton).width((float) (weaponButton.getWidth() / 1.3))
					.height((float) (weaponButton.getHeight() / 1.3)).pad(10);
			if (i % 10 == 0)
				weapons.row();
		}

		weapons.pad(850, 0, 5, 0);
		weapons.row();
	}

	private void addButtonListener() {
		weaponMenuButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Stage stage = event.getTarget().getStage();
				Vector2 mouse = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

				if (stage.hit(mouse.x, mouse.y, true) == event.getTarget()) {
					if (weapons.isVisible()) {
						weapons.setVisible(false);
						weaponDelay = true;
					} else {
						weapons.setVisible(true);
					}
				}
			}
		});
	}

	private void setUpMute() {
		TextureAtlas muteTextures = assets.getAtlas("Images/mute.json");
		muteButton = new ImageButton(new TextureRegionDrawable(muteTextures.getRegion("unmuted")),
				new TextureRegionDrawable(muteTextures.getRegion("unmuted")),
				new TextureRegionDrawable(muteTextures.getRegion("muted")));
		muteTable = new Table();
		muteTable.add(muteButton).width((float) (muteButton.getWidth() / 2.5))
				.height((float) (muteButton.getHeight() / 2.5)).pad(0, 200, 2000, 0);

		muteButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Stage stage = event.getTarget().getStage();
				Vector2 mouse = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

				if (stage.hit(mouse.x, mouse.y, true) == event.getTarget()) {
					toggleMute();
				}
			}
		});
	}

	private void toggleMute() {
		if (muted) {
			muted = false;
			game.setAudio(muted);
			muteButton.setChecked(false);
		} else {
			muted = true;
			game.setAudio(muted);
			muteButton.setChecked(true);
		}
	}

	public void setGame(FoodFeud game) {
		this.game = game;
	}

	public void setMute(boolean isMuted) {
		muted = isMuted;
		muteButton.setChecked(isMuted);
	}

	public void setInvisibleText(boolean b) {
		hiddenTable.setVisible(b);
	}

	public void render(float delta) {
		time.act(delta);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	public void setTime(long l) {
		time.setText(String.valueOf(l));
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public ImageButton setupButton(String imageString) {
		Texture myTexture = new Texture(Gdx.files.internal(imageString));
		TextureRegion myTextureRegion = new TextureRegion(myTexture);
		TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
		return new ImageButton(myTexRegionDrawable);
	}

	public boolean weaponsAreVisible() {
		if (weapons.isVisible()) {
			return true;
		}
		if (weaponDelay) {
			weaponDelay = false;
			return true;
		}
		return false;
	}

}