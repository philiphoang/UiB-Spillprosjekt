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

import java.util.List;

import com.badlogic.gdx.Screen;

import uib.teamdank.common.Score;

/**
 * Screen and the graphical user interface for viewing a set of {@link Score}s.
 */
public interface HighscoreMenuScreen extends Screen {

	/**
	 * Returns to the previous screen.
	 */
	public void goBack();

	/**
	 * Sets the list of scores to display. The list and the scores it contains
	 * cannot be {@code null}, but the list can be empty.
	 */
	public void setScores(List<Score> scores);

}
