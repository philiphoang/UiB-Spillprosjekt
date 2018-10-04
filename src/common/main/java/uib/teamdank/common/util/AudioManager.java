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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class AudioManager implements Disposable {

	private final Map<String, Music> tracks = new HashMap<>();
	private final Map<String, Sound> sounds = new HashMap<>();

	private boolean muted = false;

	@Override
	public void dispose() {
		tracks.forEach((name, track) -> track.dispose());
		sounds.forEach((name, sound) -> sound.dispose());
	}

	private Sound getSound(String name) {
		Objects.requireNonNull(name, "sound name cannot be null");
		if (!sounds.containsKey(name)) {
			sounds.put(name, Gdx.audio.newSound(Gdx.files.internal(name)));
		}
		return sounds.get(name);
	}

	private Music getTrack(String name) {
		Objects.requireNonNull(name, "track name cannot be null");
		if (!tracks.containsKey(name)) {
			tracks.put(name, Gdx.audio.newMusic(Gdx.files.internal(name)));
		}
		return tracks.get(name);
	}
	
	public void loopSound(String name) {
		getSound(name).stop();
		long id = getSound(name).play();
		getSound(name).setLooping(id, true);
	}

	public void loopTrack(String name) {
		getTrack(name).stop();
		getTrack(name).setLooping(true);
		getTrack(name).play();
	}

	public void mute() {
		muted = true;
		tracks.forEach((name, track) -> track.setVolume(0));
		sounds.forEach((name, sound) -> sound.stop());
	}

	public void pauseAll() {
		tracks.forEach((name, track) -> track.pause());
		sounds.forEach((name, sound) -> sound.pause());
	}

	public void playSound(String name) {
		if(!muted)
			getSound(name).play();
	}

	public void playTrack(String name) {
		stopTrack(name);
		getTrack(name).play();
	}

	public void preloadSounds(String... names) {
		Objects.requireNonNull(names, "list of names cannot be null");
		if (names.length == 0) {
			throw new IllegalArgumentException("at least one name must be provided");
		}
		Arrays.asList(names).forEach(n -> Objects.requireNonNull(n, "a name cannot be null"));

		for (String name : names) {
			getSound(name);
		}
	}

	public void resumeAll() {
		tracks.forEach((name, track) -> track.play());
		sounds.forEach((name, sound) -> sound.resume());
	}

	public void stopTrack(String name) {
		getTrack(name).stop();
		getTrack(name).setLooping(false);
	}

	public void unmute() {
		muted = false;
		tracks.forEach((name, track) -> track.setVolume(1));
	}
}
