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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import uib.teamdank.common.gui.Layer;
import uib.teamdank.foodfeud.Level;

public class BackgroundLayer extends Layer {

	private final Level level;

	public BackgroundLayer(Level level) {
		super(false);
		this.level = level;
	}

	@Override
	public void preRender(SpriteBatch batch, float delta) {
		batch.draw(level.getBackground(), 0, 0, level.getWidth(), level.getHeight());
	}

}
