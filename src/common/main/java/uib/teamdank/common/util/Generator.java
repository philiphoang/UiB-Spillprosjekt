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

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * A generator procedurally creates a certain kind of object.
 *
 * @param <T> the type the generator produces
 */
@FunctionalInterface
public interface Generator<T> {

	/**
	 * @param random the random from which to pull random values
	 * @return the procedurally generated object
	 */
	public T generate(Random random);

	/**
	 * Calls {@link #generate(Random)} the given amount of times and fills the
	 * specified list with the results.
	 */
	public default void generate(Random random, int amount, List<T> list) {
		Objects.requireNonNull(random, "random cannot be null");
		if (amount < 0) {
			throw new IllegalArgumentException("amount must be larger than zero");
		}
		Objects.requireNonNull(list, "list cannot be null");
		for (int i = 0; i < amount; i++) {
			list.add(generate(random));
		}
	}

}
