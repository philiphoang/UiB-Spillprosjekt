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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import uib.teamdank.cargame.CarGame;
import uib.teamdank.common.Score;
import uib.teamdank.common.gui.MenuScreen;

import java.util.Arrays;
import java.util.List;

public class HighscoreMenuScreen extends MenuScreen implements uib.teamdank.common.gui.HighscoreMenuScreen {
	private static final String BACK = "Images/Buttons/bs_back.png";
	private static final String HIGHSCORE = "Images/Buttons/cg_highscore.png";
	private static final String SCORES = "TeamDank/Carl the Crasher/highscore.json";

	private ImageButton backButton;
	private ImageButton highscoreButton;
	private FileHandle handle;
	private Table menu;
	private CarGame game;	
	private Label nameLabel;
	private Label scoreLabel;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontParameter parameter;

	public HighscoreMenuScreen(CarGame game) {
		super();
		this.game = game;
				
		menu = new Table();
		menu.setFillParent(true); 
		
		highscoreButton = createButton(HIGHSCORE, null);
		menu.add(highscoreButton)
			.width(highscoreButton.getWidth() / 4)
			.height(highscoreButton.getHeight() / 4)
			.expand().align(Align.top).padTop(Gdx.graphics.getHeight() / 16);
		
		menu.row();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/roboto.ttf"));
        parameter = new FreeTypeFontParameter();
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float dpi = 1 + Gdx.graphics.getDensity();
        int size = (int) Math.ceil(30 * dpi);

        parameter.size = size;
        parameter.size = (int) Math.ceil(20 * dpi);
		BitmapFont font = generator.generateFont(parameter);
		
		nameLabel = new Label("", new LabelStyle(font, Color.WHITE));
		scoreLabel = new Label("", new LabelStyle(font, Color.WHITE));
		
		HorizontalGroup hg = new HorizontalGroup();
		hg.space(Gdx.graphics.getWidth() / 4);
		hg.addActor(nameLabel);
		hg.addActor(scoreLabel);
		
		menu.add(hg).expand().align(Align.center);
		menu.row();
		
		backButton = createButton(BACK, this::goBack);
		menu.add(backButton)
			.width(backButton.getWidth() / 4)
			.height(backButton.getHeight() / 4)
			.expand().align(Align.bottom).padBottom(Gdx.graphics.getHeight() / 16);
		
		getStage().addActor(menu);
	}

	@Override
	public void goBack() {
		game.setScreen(game.getStartMenuScreen());
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		nameLabel.act(delta); //TODO: Needed?
		scoreLabel.act(delta); //TODO: Needed?
	}

	@Override
	public void setScores(List<Score> scores) {
		StringBuilder nameBuilder = new StringBuilder();
		StringBuilder scoreBuilder = new StringBuilder();

		for (int i = 0; i < 10 && i < scores.size(); i++) {
			Score score = scores.get(i);

			if (score.getName().length() > 40)
				nameBuilder.append(score.getName().substring(0, 37) + "...");
			else
				nameBuilder.append(score.getName());
			scoreBuilder.append(score.getScore());

			nameBuilder.append("\n");
			scoreBuilder.append("\n");
		}
		nameLabel.setText(nameBuilder.toString());
		scoreLabel.setText(scoreBuilder.toString());
	}

	@Override
	public void show() {
		super.show();
		
		handle = Gdx.files.external(SCORES);
		if (!handle.exists())
			handle = Gdx.files.internal("Data/highscore.json");
		setScores(Arrays.asList(Score.createFromJson(handle)));
	}

}
