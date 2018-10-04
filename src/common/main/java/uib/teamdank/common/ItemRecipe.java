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
package uib.teamdank.common;

/**
 * A recipe to combine two items. There is no order to the recipe's ingredients.
 */
public class ItemRecipe {

	/**
	 * Removes the two ingredients required for this recipe from the given
	 * inventory and adds the resulting item. If the inventory does not contain
	 * the ingredients, nothing happens.
	 * 
	 * @return {@code true} if the item was crafted, {@code false} if not
	 */
	public boolean craft(Inventory inventory) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the name of the second item in the recipe
	 */
	public String getItem1() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the name of the second item in the recipe
	 */
	public String getItem2() {
		// TODO Auto-generated method stub
		return null;
	}

}
