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

import java.util.Objects;

/**
 * A timed event notifies a listener every set number of seconds. It must be
 * updated with the time by calling {@link #update(float)}.
 */
public class TimedEvent {

	private final float frequencySeconds;
	private final Runnable action;

	private boolean loop;
	private float currentTime;

	public TimedEvent(float frequencyInSeconds, boolean loop, Runnable action) {
		if (frequencyInSeconds <= 0) {
			throw new IllegalArgumentException("frequency must be over zero");
		}
		Objects.requireNonNull(action, "action cannot be null");
		this.frequencySeconds = frequencyInSeconds;
		this.action = action;
		setLoop(loop);
	}

	public TimedEvent(float frequencyInSeconds, Runnable action) {
		this(frequencyInSeconds, false, action);
	}

	public boolean isDone() {
		return (!loop && currentTime > frequencySeconds);
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void update(float delta) {
		if (!isDone()) {
			this.currentTime += delta;
			if (currentTime >= frequencySeconds) {
				action.run();
				currentTime -= frequencySeconds;
			}
		}
	}

}
