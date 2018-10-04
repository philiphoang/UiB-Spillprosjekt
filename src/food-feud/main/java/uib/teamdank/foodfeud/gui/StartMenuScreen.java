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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import uib.teamdank.common.Game;
import uib.teamdank.common.gui.CreditScreen;
import uib.teamdank.common.gui.MenuScreen;
import uib.teamdank.foodfeud.FoodFeud;

public class StartMenuScreen extends MenuScreen implements uib.teamdank.common.gui.StartMenuScreen {
    private static final String LOGO = "Images/logo.png";
    private static final String PLAY = "Images/Buttons/ff_start.png";
    private static final String CREDIT = "Images/Buttons/ff_credit.png";
    private static final String EXIT = "Images/Buttons/ff_quit.png";

    private Table menu;
    private ImageButton logoButton;
    private Array<Button> buttons;
    private CreditScreen creditScreen;
    private Game game;

    public StartMenuScreen(FoodFeud game){
        super();
        this.game = game;
        creditScreen = new CreditScreen(game, "Images/Buttons/ff_back.png", "Data/credit_foodfeud.txt");
        buttons = new Array<>();
        menu = new Table();

        logoButton = createButton(LOGO, null);
        buttons.add(createButton(PLAY, this::newSetupGame));
        buttons.add(createButton(CREDIT, this::viewCredit));
        buttons.add(createButton(EXIT, this::exitGame));

        addToTables();

        menu.setFillParent(true);
        getStage().addActor(menu);
    }

    private void addToTables() {
        menu.add(logoButton).height((float) (logoButton.getHeight() /1.3)).pad(10, 0, 20, 0);
        menu.row();
        for (Button but : buttons) {
            menu.add(but).width((float) (but.getWidth() / 4)).height((float) (but.getHeight() / 4)).pad(5);
            menu.row();
        }
    }

	@Override
	public void exitGame() {
		Gdx.app.exit();
	}

	@Override
	public void newGame() {
		game.setScreen(game.newGame());
	}
	
	public void newSetupGame() {
		game.setScreen(((FoodFeud) game).newSetupGame());
	}

	@Override
	public void viewHighscores() {
        throw new UnsupportedOperationException("no highscores in this game");
	}

    public void viewCredit() {
        game.setScreen(creditScreen);
    }
}