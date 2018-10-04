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
 * An upgradeable object, which means it can receive an {@link Upgrade} to
 * permanently have certain properties positively changed.
 */
@FunctionalInterface
public interface Upgradeable {

	/**
	 * @param upgrade
	 *            the upgrade to apply to this upgradeable object
	 */
	public void applyUpgrade(Upgrade upgrade);

}
