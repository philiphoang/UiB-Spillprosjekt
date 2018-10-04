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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import uib.teamdank.cargame.CarGame;
import uib.teamdank.cargame.Player;
import uib.teamdank.common.Game;
import uib.teamdank.common.gui.MenuScreen;
import uib.teamdank.common.util.WeatherData;
import uib.teamdank.common.util.WeatherData.WeatherType;
import uib.teamdank.common.gui.CreditScreen;

public class StartMenuScreen extends MenuScreen implements uib.teamdank.common.gui.StartMenuScreen {
	private static final String BACKGROUND = "Images/menu_screen.png";
	private static final String PLAY = "Images/Buttons/start.png";
	private static final String HIGHSCORE = "Images/Buttons/cg_highscore.png";
	private static final String SHOP = "Images/Buttons/bs_shop.png";
	private static final String CREDIT = "Images/Buttons/bs_credit.png";
	private static final String EXIT = "Images/Buttons/bs_quit.png";

	private Table menu;

	private HighscoreMenuScreen highscoreMenuScreen;
	private CreditScreen creditScreen;
	private CarGame game;
	private ShopScreen shopScreen;
	private Player player;
	private WeatherData wData;
	private boolean cheatActivated;
	private WeatherType wType;
	private Stage stage;
	private String c1;
	private String c2;
	private String c3;

	public StartMenuScreen(CarGame game) {
		super();
		stage = new Stage(new FitViewport(1920, 1080));
		this.game = game;
		highscoreMenuScreen = new HighscoreMenuScreen(game);
		creditScreen = new CreditScreen(game, "Images/Buttons/bs_back.png", "Data/credit_crasher.txt");
		shopScreen = new ShopScreen(game);
		menu = new Table();

		// Background Image
		setupBackground();

		// Weather Data
		wData = game.getWeatherData();

		createButtons();

		// Player initialization
		player = game.getPlayer();
		player.setWeatherType(getWeather());
		player.getTexture();
		player.setScale(.5f);

		menu.setFillParent(true);
		getStage().addActor(menu);
	}

	private void createButtons() {
		Array<Button> buttons = new Array<>();

		buttons.add(createButton(PLAY, this::newGame));
		buttons.add(createButton(HIGHSCORE, this::viewHighscores));
		buttons.add(createButton(SHOP, this::viewShop));
		buttons.add(createButton(CREDIT, this::viewCredit));
		buttons.add(createButton(EXIT, this::exitGame));

		menu.pad(450, 1100, 0, 0);
		menu.row();
		for (Button but : buttons) {
			menu.add(but).width((float) (but.getWidth() / 4)).height((float) (but.getHeight() / 4)).pad(5);
			menu.row();
		}
	}

	private void setupBackground() {
		Texture backgroundTexture = new Texture(BACKGROUND);
		Image backgroundImage = new Image(backgroundTexture);
		getStage().addActor(backgroundImage);
	}

	@Override
	public void exitGame() {
		Gdx.app.exit();
	}

	@Override
	public void viewHighscores() {
		game.setScreen(highscoreMenuScreen);
	}

	public void viewCredit() {
		game.setScreen(creditScreen);
	}

	public void viewShop() {
		game.setScreen(shopScreen);
	}

	@Override
	public void newGame() {
		game.setScreen(game.newGame());
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		cheatForWeather();
	}

	private void cheatForWeather() {
		tryRain();
		trySun();
		trySnow();
		tryCoin();
		trySky();
	}

	private void tryRain() {
		if (Gdx.input.isKeyJustPressed(Keys.R))
			c1 = "R";
		if (Gdx.input.isKeyJustPressed(Keys.A) && c1 == "R")
			c2 = "A";
		if (Gdx.input.isKeyJustPressed(Keys.I) && c1 == "R" && c2 == "A") 
			c3 = "I";
		if (Gdx.input.isKeyJustPressed(Keys.N) && c1 == "R" && c2 == "A" && c3 == "I"){
			System.out.println("rain");
			wType = WeatherType.RAIN;
			resetKeys();
		}
	}

	private void trySun() {
		if (Gdx.input.isKeyJustPressed(Keys.S))
			c1 = "S";
		if (Gdx.input.isKeyJustPressed(Keys.U) && c1 == "S")
			c2 = "U";
		if (Gdx.input.isKeyJustPressed(Keys.N) && c1 == "S" && c2 == "U") {
			System.out.println("sun");
			wType = WeatherType.SUN;
			resetKeys();
		}
	}

	private void trySnow() {
		if (Gdx.input.isKeyJustPressed(Keys.S))
			c1 = "S";
		if (Gdx.input.isKeyJustPressed(Keys.N) && c1 == "S")
			c2 = "N";
		if (Gdx.input.isKeyJustPressed(Keys.O) && c1 == "S" && c2 == "N")
			c3 = "O";
		if (Gdx.input.isKeyJustPressed(Keys.W) && c1 == "S" && c2 == "N" && c3 == "O") {
			System.out.println("snow");
			wType = WeatherType.SNOW;
			resetKeys();
		}
	}

	private void tryCoin() {
		if (Gdx.input.isKeyJustPressed(Keys.C))
			c1 = "C";
		if (Gdx.input.isKeyJustPressed(Keys.O) && c1 == "C")
			c2 = "O";
		if (Gdx.input.isKeyJustPressed(Keys.I) && c1 == "C" && c2 == "O")
			c3 = "I";
		if (Gdx.input.isKeyJustPressed(Keys.N) && c1 == "C" && c2 == "O" && c3 == "I") {
			System.out.println("Give coin");
			player.getInventory().addGold(10);
			c1 = "";
			c2 = "";
			c3 = "";
		}
	}

	private void trySky() {
		if (Gdx.input.isKeyJustPressed(Keys.S))
			c1 = "S";
		if (Gdx.input.isKeyJustPressed(Keys.K) && c1 == "S")
			c2 = "K";
		if (Gdx.input.isKeyJustPressed(Keys.Y) && c1 == "S" && c2 == "K") {
			System.out.println("sky");
			wType = WeatherType.CLOUD;
			resetKeys();
		}
	}

	private void resetKeys() {
		c1 = "";
		c2 = "";
		c3 = "";
		cheatActivated = true;
	}

	public WeatherType getWeather() {
		if (cheatActivated) {
			cheatActivated = false;
			return wType;
		}
		System.out.println("using standard weather from yr.no");
		return wData.pullWeather("Norway", "Hordaland", "Bergen", "Bergen");
	}
}