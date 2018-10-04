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

import com.badlogic.gdx.utils.Array;

/**
 * A collection of {@link Item}s with a maximum capacity.
 */
public class Inventory {

    private int gold;
    private Array<Item> bag;
    private int maxCapacity;
    private boolean full;


    public Inventory () {
        this(20);
    }

    public Inventory (int size){
        this(size, 0);
    }

    public Inventory (int size, int gold){
        this.bag = new Array<>(size);
        this.maxCapacity = size;
        this.gold = gold;
        this.full = false;
    }
	/**
	 * @param amount the amount of gold to add to this inventory
	 */
	public void addGold(int amount) {
		this.gold += amount;
	}
	
	/**
	 * Adds the given item to this inventory, if there is space.
	 */
	public void addItem(Item item) {
		if(this.bag.size != maxCapacity)
            this.bag.add(item);

		if(this.bag.size == maxCapacity)
		    this.full = true;

	}
	
	/**
	 * @return the maximum amount of items this inventory can hold
	 */
	public int getCapacity() {
		return this.maxCapacity;
	}
	
	/**
	 * @return the amount of gold this inventory contains
	 */
	public int getGold() {
		return this.gold;
	}

	/**
	 * @return the amount of items currently in this inventory
	 */
	public int getItemCount() {
		return this.bag.size;
	}

    /**
     * @param index
     * @return the Item at @index
     */
	public Item getItem(int index){
	    if(index >= bag.size)
	        return null;

        return bag.get(index);
    }
	/**
	 * @return an immutable list of the items in this inventory
	 */
	public Array<Item> getItems() {
		return this.bag;
	}

	/**
	 * @return whether or not this inventory is full
	 */
	public boolean isFull() {
		return this.full;
	}

	/**
	 * @param amount the amount of gold to remove from this inventory
	 */
	public void removeGold(int amount) {
		this.gold = this.gold - amount;
	}

	/**
	 * Removes the item with the given index.
	 */
	public void removeItem(int index) {
		bag.removeIndex(index);
	}

}
