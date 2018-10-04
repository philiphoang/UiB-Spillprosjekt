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
 * Interface for Items in the game
 */
public class Item extends GameObject {

    private final String description;
    private final String name;

    public Item (String n, String d){
    	this.name = n;
    	this.description = d;
	}
    /**
     *
     * @return description of this item
     */
	public String getDescription() {
		return description;
	}

    /**
     *
     * @return name of this item
     */
	public String getName() {
		return name;
	}
}
