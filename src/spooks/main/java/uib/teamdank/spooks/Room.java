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
package uib.teamdank.spooks;

import java.util.List;

import uib.teamdank.common.Inventory;
import uib.teamdank.common.ItemHolder;
import uib.teamdank.common.ItemRecipe;
import uib.teamdank.common.util.Difficulty;

/**
 * A room that has a time limit and items in it.
 */
public class Room implements ItemHolder {

	public Difficulty getDifficulty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return an immutable list of the available crafting recipes in this room
	 */
	public List<ItemRecipe> getRecipes() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getTimeLimit() {
		// TODO Auto-generated method stub
		return -1;
	}

	/**
	 * @return whether player has performed the necessary actions to solve this
	 *         room
	 */
	public boolean isSolved() {
		// TODO Auto-generated method stub
		return false;
	}
}
