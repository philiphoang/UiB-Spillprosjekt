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


import java.util.Arrays;
import java.util.List;
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
import uib.teamdank.common.Score;
import uib.teamdank.common.gui.MenuScreen;
import uib.teamdank.foodfeud.FoodFeud;

public class HighscoreMenuScreen extends MenuScreen implements uib.teamdank.common.gui.HighscoreMenuScreen {
    private static final String BACK = "Images/Buttons/ff_back.png";
    private static final String HIGHSCORE = "Images/Buttons/ff_highscore.png";
    private static final String SCORES = "Data/highscore.json";

    private ImageButton backButton;
    private ImageButton highscoreButton;
    private Table menu;
    private FoodFeud game;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontParameter parameter;
    private BitmapFont font;
    private Label nameLabel;
    private Label scoreLabel;

    public HighscoreMenuScreen(FoodFeud game) {
    	super();
        this.game = game;

        menu = new Table();
        menu.setFillParent(true);

        highscoreButton = createButton(HIGHSCORE, null);
        menu.add(highscoreButton)
                .width((highscoreButton.getWidth() / 4))
                .height((highscoreButton.getHeight() / 4))
                .expand().align(Align.top).padTop(Gdx.graphics.getHeight() / 16);

        menu.row();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/roboto.ttf"));
        parameter = new FreeTypeFontParameter();
        float dpi = Gdx.graphics.getDensity() + 1;
        parameter.size = (int) Math.ceil(50 * dpi);

        font = generator.generateFont(parameter);

        nameLabel = new Label("", new LabelStyle(font, Color.WHITE));
        scoreLabel = new Label("", new LabelStyle(font, Color.WHITE));

        FileHandle handle = Gdx.files.external(SCORES);
        if (!handle.exists())
            handle = Gdx.files.internal("Data/highscore.json");
        setScores(Arrays.asList(Score.createFromJson(handle)));

        HorizontalGroup hg = new HorizontalGroup();
        hg.space(Gdx.graphics.getWidth() / 4);
        hg.addActor(nameLabel);
        hg.addActor(scoreLabel);

        menu.add(hg).expand().align(Align.center);
        menu.row();

        backButton = createButton(BACK, this::goBack);
        menu.add(backButton)
                .width((backButton.getWidth() / 4))
                .height((backButton.getHeight() / 4))
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
        nameLabel.act(delta);
        scoreLabel.act(delta);
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
}