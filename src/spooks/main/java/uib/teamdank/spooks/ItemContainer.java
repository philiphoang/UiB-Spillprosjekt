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

import uib.teamdank.common.Inventory;
import uib.teamdank.common.Item;
import uib.teamdank.common.ItemHolder;

/**
 * An item that has inventory of other items. 
 */
public class ItemContainer extends Item implements ItemHolder {

    private final Inventory bag;

    public ItemContainer(String n, String d) {
        super(n, d);
        this.bag = new Inventory();
    }

    @Override
	public Inventory getInventory() {
        return this.bag;
	}

}
