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
package uib.teamdank.nightlife;

import uib.teamdank.common.Item;
import uib.teamdank.common.Upgrade;
import uib.teamdank.common.Upgradeable;

/**
 * A player-owned tower.
 */
public class Tower extends Item implements Upgradeable {

    public Tower(String n, String d) {
        super(n, d);
    }

    @Override
	public void applyUpgrade(Upgrade upgrade) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return amount of damage a projectile from this tower inflicts
	 */
	public double getDamage() {
		// TODO Auto-generated method stub
		return -1;
	}

	/**
	 * @return the amount of damage this tower will do per second
	 */
	public double getDamagePerSecond() {
		// TODO Auto-generated method stub
		return -1;
	}

	/**
	 * @return the firing speed of this tower in seconds
	 */
	public double getFiringSpeed() {
		// TODO Auto-generated method stub
		return -1;
	}

	/**
	 * @return the initial cost of this tower
	 */
	public int getInitialCost() {
		// TODO Auto-generated method stub
		return -1;
	}

}
