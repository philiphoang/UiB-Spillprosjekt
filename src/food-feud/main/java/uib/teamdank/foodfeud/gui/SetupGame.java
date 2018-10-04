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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import uib.teamdank.common.Game;
import uib.teamdank.common.gui.MenuScreen;
import uib.teamdank.common.util.AssetManager;
import uib.teamdank.common.util.TextureAtlas;
import uib.teamdank.foodfeud.Match;
import uib.teamdank.foodfeud.MatchBuilder;
import uib.teamdank.foodfeud.Team;

public class SetupGame extends MenuScreen implements Screen {

	private static class PlayerButton extends ImageButton {
		private Team team;

		public PlayerButton(String name, Team t, Drawable unlockedImage) {
			super(unlockedImage);
			this.team = t;
			setName(name);
		}
		
		public Team getTeam() {
			return team;
		}
	}

	private class ButtonListener extends InputListener {
		private final PlayerButton source;

		public ButtonListener(PlayerButton source) {
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
				if (source.isChecked()) {
					selectedPlayers++;
					matchBuild.addPlayer(source.getName(), source.getTeam());
					source.setChecked(true);
				} else {
					selectedPlayers--;
					matchBuild.removePlayer(source.getTeam());
					source.setChecked(false);
				}
			}
		}
	}

	private Game game;

	private Table menu;
	private Table playerTable;
	private Table backButtonTable;
	private Table playerCountTable;

	private AssetManager assets;
	private BitmapFont font;
	private TextButtonStyle textButtonStyle;
	private TextButton helpText;

	private int playerCount;
	private TextureAtlas buttonTexture;
	private TextureAtlas playerTextures;
	
	private Match match;
	private ImageButton backButton;
	private final List<PlayerButton> playerButtons = new ArrayList<>();

	private int selectedPlayers = 0;

	private MatchBuilder matchBuild;

	private FreeTypeFontGenerator generator;

	private FreeTypeFontParameter parameter;

	public SetupGame(Game game) {
		super();
		this.game = game;

		playerCount = 1;

		menu = new Table();
		playerTable = new Table();
		playerCountTable = new Table();
		backButtonTable = new Table();
		new Table();

		this.assets = new AssetManager();
		
		matchBuild = new MatchBuilder(assets);

		buttonTexture = assets.getAtlas("Images/Buttons/ff_button.json");
		backButton = setupImage(buttonTexture.getRegion("back"));
		addButtonListener(backButton, () -> game.setScreen(game.getStartMenuScreen()));

		playerTextures = assets.getAtlas("Images/player_sheet.json");
		
		generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/roboto.ttf"));
		parameter = new FreeTypeFontParameter();
		float dpi = Gdx.graphics.getDensity() + 1;
		parameter.size = (int) Math.ceil(40 * dpi);
		font = generator.generateFont(parameter);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;

		helpText = new TextButton("How many players should there be?", textButtonStyle);
		menu.add(helpText);

		for (int i = 2; i < 5; i++) {
			TextButton playerNumber = new TextButton(String.valueOf(i), textButtonStyle);
			playerNumber.pad(60);
			playerNumber.setName(String.valueOf(i));
			addButtonListener(playerNumber, () -> setPlayers(playerNumber.getName()));
			playerCountTable.add(playerNumber);
		}
		
		setupPlayers();

		backButtonTable.add(backButton).width((float) (backButton.getWidth() / 4)).height((float) (backButton.getHeight() / 4));

		menu.pad(0, 0, 800, 0);
		playerCountTable.pad(0, 0, 500, 0);
		backButtonTable.pad(800, 0, 0, 0);
		
		menu.setFillParent(true);
		playerTable.setFillParent(true);
		backButtonTable.setFillParent(true);
		playerCountTable.setFillParent(true);
		
		getStage().addActor(menu);
		getStage().addActor(playerTable);
		getStage().addActor(playerCountTable);
		getStage().addActor(backButtonTable);
	}

	private ImageButton setupImage(TextureRegion textureRegion) {
		TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(textureRegion);
		return new ImageButton(myTexRegionDrawable);
	}

	private void setupPlayers() {
			PlayerButton playerHelper = new PlayerButton ("Gunnar \"Coach\" Schulze", Team.ALPHA, new TextureRegionDrawable(playerTextures.getRegion(Team.ALPHA.getBodyExpansion(0))));
			playerHelper.addListener(new ButtonListener(playerHelper));
			playerHelper.pad(60, 100, 60, 100);
			playerButtons.add(playerHelper);
			
			playerHelper = new PlayerButton ("Gertrude Skogsheim", Team.BETA, new TextureRegionDrawable(playerTextures.getRegion(Team.BETA.getBodyExpansion(0))));
			playerHelper.addListener(new ButtonListener(playerHelper));
			playerHelper.pad(60, 100, 60, 100);
			playerButtons.add(playerHelper);
			
			playerHelper = new PlayerButton ("Per-Ole Nordskog", Team.CHARLIE, new TextureRegionDrawable(playerTextures.getRegion(Team.CHARLIE.getBodyExpansion(0))));
			playerHelper.addListener(new ButtonListener(playerHelper));
			playerHelper.pad(60, 100, 60, 100);
			playerButtons.add(playerHelper);
			
			playerHelper = new PlayerButton ("Knut-Roger Regeltun", Team.DELTA, new TextureRegionDrawable(playerTextures.getRegion(Team.DELTA.getBodyExpansion(0))));
			playerHelper.addListener(new ButtonListener(playerHelper));
			playerHelper.pad(60, 100, 60, 100);
			playerButtons.add(playerHelper);
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
		super.render(delta);
		if (playerCount > 1) 
			addPlayersToScreen();
		
		checkPlayerAndMaybeStart();
	}

	private void checkPlayerAndMaybeStart() {
		if (playerCount == selectedPlayers){
			match = matchBuild.build();
			game.setScreen(game.newGame());
		}
	}

	private void addPlayersToScreen() {
		for (int i = 0; i < playerButtons.size(); i++) {
				playerTable.add(playerButtons.get(i)).pad(5);
		}
	}

	private void setPlayers(String name) {
		playerCount = Integer.parseInt(name);
	}
	
	public Match getMatch() {
		return match;
	}
	
	public AssetManager getAssets() {
		return assets;
	}

	@Override
	public void show() {
		super.show();
	}
}
