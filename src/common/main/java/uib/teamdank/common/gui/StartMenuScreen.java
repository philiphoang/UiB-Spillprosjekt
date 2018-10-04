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

import com.badlogic.gdx.Screen;

import uib.teamdank.common.Score;

/**
 * Screen and the graphical user interface for a start menu.
 */
public interface StartMenuScreen extends Screen {

	/**
	 * Exits the game.
	 */
	public void exitGame();

	/**
	 * Starts a new game.
	 */
	public void newGame();

	/**
	 * Shows a {@link HighscoreMenuScreen} with the relevant {@link Score}s.
	 */
	public void viewHighscores();

}
