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
 * An upgrade specifies a set of positive changes that can be made to an
 * {@link Upgradeable}.
 */
public interface Upgrade {

	/**
	 * @return a user friendly description of this upgrade
	 */
	public String getDescription();

	/**
	 * @return the level of this upgrade, which impacts its effects
	 */
	public int getLevel();

}
