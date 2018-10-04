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
package uib.teamdank.common.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import uib.teamdank.common.Game;

public class CreditScreen extends MenuScreen {

		private ImageButton backButton;
		private Game game;
		private String creditFile;
		private Container<ImageButton> buttonCont;
		private VerticalGroup creditGroup;
		
		private FreeTypeFontGenerator generator;
		private FreeTypeFontParameter parameter;

		public CreditScreen(Game game, String buttonFile, String fileWithCredit) {
			super();
			this.game = game;
			this.creditFile = fileWithCredit;
						
			backButton = createButton(buttonFile, this::goBack);

			buttonCont = new Container<>(backButton);
			buttonCont.width(backButton.getWidth() / 4).height(backButton.getHeight() / 4)
				.align(Align.bottomLeft)
				.pad(0, Gdx.graphics.getWidth() / 16, Gdx.graphics.getHeight() / 16, 0);

			getStage().addActor(buttonCont);
					
			creditGroup = new VerticalGroup();
			creditGroup.setWidth(Gdx.graphics.getWidth() - buttonCont.getMaxWidth());
			creditGroup.align(Align.center);

			getStage().addActor(creditGroup);
		}
		
		@Override
		public void show() {
			super.show();
			
			String[] lines = Gdx.files.internal(creditFile).readString().split("\\r?\\n");

			generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/roboto.ttf"));
			parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
			float dpi = Gdx.graphics.getDensity() + 1;
			parameter.size = (int) Math.ceil(30 * dpi);
			BitmapFont font = generator.generateFont(parameter);
			
			for (String line : lines) {
				Label l = new Label(line, new LabelStyle(font, Color.WHITE));
				l.setAlignment(Align.left);
				l.setWrap(true);
				l.setWidth(creditGroup.getWidth());
				
				creditGroup.addActor(new Container<Label>(l).width(creditGroup.getWidth()));
			}
			
			creditGroup.setPosition(2 * buttonCont.getMaxWidth(), (float) (-1.25*font.getCapHeight() * lines.length));
		}	

		@Override
		public void render(float delta) {
			super.render(delta);
			
			creditGroup.moveBy(0, delta * 50);
		}

		@Override
		public void hide() {
			super.hide();
			creditGroup.clear();
		}

		@Override
		public void dispose() {
			getStage().dispose();
			super.dispose();
		}

		public void goBack() {
			game.setScreen(game.getStartMenuScreen());
		}

	}


