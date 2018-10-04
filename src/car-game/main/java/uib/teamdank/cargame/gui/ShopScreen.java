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

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import uib.teamdank.cargame.CarGame;
import uib.teamdank.cargame.Player;
import uib.teamdank.common.gui.MenuScreen;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.TextureAtlas;

public class ShopScreen extends MenuScreen implements Screen {

	private static class CarButton extends ImageButton {
		private boolean unlocked;

		public CarButton(String name, Drawable unlockedImage, Drawable lockedImage) {
			super(lockedImage, lockedImage, unlockedImage);
			setDisabled(true);
			setName(name);
			setUnlocked(false);
		}

		public void setUnlocked(boolean unlocked) {
			this.unlocked = unlocked;
		}

		public void updateTexture() {
			if (unlocked && isChecked()) {
				setDisabled(true);
			} else if (unlocked) {
				setDisabled(false);
			}
		}
	}

	private class CarListener extends InputListener {
		private final CarButton source;

		public CarListener(CarButton source) {
			this.source = source;
		}

		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			return true;
		}

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			Stage myStage = event.getTarget().getStage();
			Vector2 mouse = myStage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

			if (myStage.hit(mouse.x, mouse.y, true) == event.getTarget()) {
				if (source.unlocked && source.isDisabled()) {
					game.getPlayer().setTexture(unlockedCarTextures.getRegion(source.getName()));
					source.updateTexture();
				} else if (!source.unlocked && game.getPlayer().getInventory().getGold() >= CAR_COST) {
					game.getPlayer().setTexture(unlockedCarTextures.getRegion(source.getName()));
					game.getPlayer().getInventory().removeGold(CAR_COST);
					game.getPlayer().unlockSkin(source.getName());
					setNewCoinCount(game.getPlayer().getInventory().getGold());

					source.setUnlocked(true);
					source.setChecked(true);
					source.updateTexture();
				} else {
					source.updateTexture();
				}
			}
		}
	}
	
	private final List<CarButton> carButtons = new ArrayList<>();
	private static final int CAR_COST = 10;

	private CarGame game;
	
	private Table menu;
	private Table cars;
	private Table coinsTable;
	
	private AssetManager assets;
	private TextureAtlas roadEntityTextures;
	private TextureAtlas buttonTexture;
	private TextureAtlas lockedCarTextures;
	private TextureAtlas unlockedCarTextures;
	
	private BitmapFont font;
	private ImageButton coinImage;
	private ImageButton backButton;
	private TextButtonStyle textButtonStyle;
	private TextButton coinsCount;
	private TextButton helpText;
	
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	

	public ShopScreen(CarGame game) {
		super();
		this.game = game;

		menu = new Table();
		cars = new Table();
		coinsTable = new Table();

		this.assets = new AssetManager();

		buttonTexture = assets.getAtlas("Images/Buttons/cg_buttons.json");
		backButton = setupImage(buttonTexture.getRegion("cg_back"));

		roadEntityTextures = assets.getAtlas("Images/road_entity_sheet.json");
		lockedCarTextures = assets.getAtlas("Images/locked_car_sheet.json");
		unlockedCarTextures = assets.getAtlas("Images/car_sheet.json");

		coinImage = setupImage(roadEntityTextures.getRegion("coin"));
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/roboto.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		float dpi = Gdx.graphics.getDensity() + 1;
		parameter.size = (int) Math.ceil(20 * dpi);
		font = generator.generateFont(parameter);

		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;

		coinsCount = new TextButton("0", textButtonStyle);
		

		coinsTable = new Table();
		coinsTable.add(coinImage).pad(0, 1600, 900, 0);
		coinsTable.add(coinsCount).pad(0, 20, 900, 0);

		String help = "You need to click on the car \n to unlock it and select it.\n \n You need " + CAR_COST + " gold \n to unlock a new car";
		helpText = new TextButton(help, textButtonStyle);
		helpText.pad(0, 1300, 0, 0);
		
		setupCars();
		setupScreen();
		addButtonListener(backButton, () -> game.setScreen(game.getStartMenuScreen()));

		menu.setFillParent(true);
		coinsTable.setFillParent(true);
		helpText.setFillParent(true);
		getStage().addActor(helpText);
		getStage().addActor(menu);
		getStage().addActor(coinsTable);
	}

	private ImageButton setupImage(TextureRegion textureRegion) {
		TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(textureRegion);
		return new ImageButton(myTexRegionDrawable);
	}

	@Override
	public void dispose() {
		assets.dispose();
	}

	public void goBack() {
		game.setScreen(game.getStartMenuScreen());
	}

	@Override
	public void render(float delta) {
		coinsCount.act(delta);
		super.render(delta);
	}

	@Override
	public void show() {
		super.show();

		final Player player = game.getPlayer();
		player.unlockSkin("car_forward");
		coinsCount.setText(String.valueOf(player.getInventory().getGold()));
		for (CarButton button : carButtons) {
			button.unlocked = player.hasUnlockedSkin(button.getName());
			button.setChecked(button.unlocked);
			button.updateTexture();
		}
	}

	private void setupCars() {
		unlockedCarTextures.forEachRegion((name, texture) -> {
			CarButton carButton = new CarButton(name, new TextureRegionDrawable(texture),
					new TextureRegionDrawable(lockedCarTextures.getRegion(name)));
			carButton.addListener(new CarListener(carButton));
			carButtons.add(carButton);
		});
	}

	private void setupScreen() {
		for (int i = 0; i < carButtons.size(); i++) {
			if (i % 3 == 0)
				cars.row();
			cars.add(carButtons.get(i)).height((float) (carButtons.get(i).getHeight() / 1.5))
					.width((float) (carButtons.get(i).getWidth() / 1.5)).pad(15);
		}
		menu.add(cars);
		menu.row();
		menu.add(backButton).width((float) (backButton.getWidth() / 4)).height((float) (backButton.getHeight() / 4))
				.pad(0, 0, 0, 0);
	}

	public void setNewCoinCount(int i) {
		coinsCount.setText(String.valueOf(i));
	}
}
