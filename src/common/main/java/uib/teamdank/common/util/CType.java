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
package uib.teamdank.common.util;

public enum CType {
	// These constants are used to set category
	// and mask for collision filters and
	// are to the power of two, 2^x where x = 0 -> 15
	CATEGORY_PLAYER ((short) 1), 
	CATEGORY_WORLD ((short) 2),
	CATEGORY_PROJECTILE ((short) 4);
	
	private short value;    
	
	private CType(short value) {
		this.value = value;
	}
	
	public short getValue() {
		return value;
	}
}
