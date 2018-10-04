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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
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
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import uib.teamdank.cargame.CarGame;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.TextureAtlas;

public class CarHud {
	
	private Stage stage;
	private AssetManager assets;
	private ImageButton fuelImage;
	private ImageButton coinImage;
	private ProgressBar bar;
	private ImageButton muteButton;
	
	private BitmapFont font;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;
	private TextButtonStyle textButtonStyle;
	private TextButton highscore;
	private TextButton coinsCount;
	private TextButton newHighscoreMessage;

	private Table scoreTable;
	private Table fuelTable;
	private Table coinsTable;
	private Table muteTable;
	private Table newHighscoreTable;
	private TextureAtlas roadEntityTextures;
	
	private CarGame game;

	private boolean muted = false;

	public CarHud() {
		stage = new Stage(new FitViewport(1920, 1080));
		
		this.assets = new AssetManager();
		roadEntityTextures = assets.getAtlas("Images/road_entity_sheet.json");

		coinImage = setupImage(roadEntityTextures.getRegion("coin"));
		
		setUpFuel();
		setUpMute();

		generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/roboto.ttf"));
		parameter = new FreeTypeFontParameter();
		float dpi = Gdx.graphics.getDensity() + 1;
		parameter.size = (int) Math.ceil(50 * dpi);
		font = generator.generateFont(parameter);
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		
		highscore = new TextButton("0", textButtonStyle);
		
		coinsCount = new TextButton("0", textButtonStyle);
		
		scoreTable = new Table();
		scoreTable.add(highscore).width(300).pad(900, 0, 0, 1600);
		
		coinsTable = new Table();
		coinsTable.add(coinImage).pad(0, 1600, 900, 0);
		coinsTable.add(coinsCount).pad(0, 20, 900, 0);
		
		scoreTable.setFillParent(true);
		
		coinsTable.setFillParent(true);
		
		newHighscoreInit();
		
		
		stage.addActor(scoreTable);
		stage.addActor(fuelTable);
		stage.addActor(coinsTable);
		stage.addActor(muteTable);
	}

	public boolean isMuted() {
		return muted;
	}

	public void setAsInputProcessor() {
		Gdx.input.setInputProcessor(stage);
	}

	private void setUpFuel() {
		fuelImage = setupImage(roadEntityTextures.getRegion("gastank"));
		
		Skin skin = new Skin();
		Pixmap pixmap = new Pixmap(40, 110, Format.RGBA8888);
		pixmap.setColor(Color.RED);
		pixmap.fill();
		skin.add("red", new Texture(pixmap));
		
		pixmap = new Pixmap(1, 110, Format.RGBA8888);
		pixmap.setColor(Color.GREEN);
		pixmap.fill();
		skin.add("green", new Texture(pixmap));

		ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
		style.background = skin.getDrawable("red");
		style.knobBefore = skin.getDrawable("green");

		bar = new ProgressBar(0, 100, 1, false, style);
		
		fuelTable = new Table();
		fuelTable.add(fuelImage).width((float) (fuelImage.getWidth() / 2)).height((float) (fuelImage.getHeight() / 2)).pad(900, 1400, 0, 0);
		fuelTable.add(bar).width(bar.getWidth()*3).pad(900, 10, 0, 0);
		
		fuelTable.setFillParent(true);
	}

	private ImageButton setupImage(TextureRegion textureRegion) {
		TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(textureRegion);
		return new ImageButton(myTexRegionDrawable);
	}

	private void setUpMute() {
		TextureAtlas muteTextures = assets.getAtlas("Images/mute.json");
		muteButton = new ImageButton(
				new TextureRegionDrawable(muteTextures.getRegion("unmuted")),
				new TextureRegionDrawable(muteTextures.getRegion("unmuted")),
				new TextureRegionDrawable(muteTextures.getRegion("muted"))
		);
		muteTable = new Table();
		muteTable.add(muteButton).width((float) (muteButton.getWidth() / 2.5)).height((float) (muteButton.getHeight() / 2.5)).pad(0, 200, 2000, 0);

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
		if(muted) {
			setMute(false);
			
		}
		else {
			setMute(true);
		}
	}
	
	public void setGame(CarGame game) {
		this.game = game;
	}
	
	public void setMute(boolean isMuted) {
		muted = isMuted;
		muteButton.setChecked(isMuted);
		game.getGameScreen().setMuted(isMuted);
		game.setAudio(isMuted);
	}

	public void render(float delta) {
		bar.act(delta);
		highscore.act(delta);
		coinsCount.act(delta);
		newHighscoreMessage.act(delta);
		stage.act(delta);
		stage.draw();
	}

	public void setCurrentFuel(int fuel, int max) {
		bar.setValue(fuel);
		bar.setRange(0, max);
	}
	
	public void setScore (long l) {
		highscore.setText(String.valueOf(l));
	}
	
	public void setCoins(int i) {
		coinsCount.setText(String.valueOf(i));
	}
	
	public void newHighscoreInit() {
		newHighscoreMessage = new TextButton("NEW HIGHSCORE!", textButtonStyle);
		
		newHighscoreTable = new Table();
		newHighscoreTable.add(newHighscoreMessage).width(300).pad(0, 0, 800, 0);
		newHighscoreTable.setFillParent(true);
		newHighscoreTable.setVisible(false);
		
		stage.addActor(newHighscoreTable);
	}
	
	public void setVisibleNewHighscore(boolean visible) {
		newHighscoreTable.setVisible(visible);
	}
	
	public boolean isVisibleNewHighscore(){
		return newHighscoreTable.isVisible();
	}
	
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
}