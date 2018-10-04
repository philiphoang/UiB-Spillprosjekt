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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

public class AssetManager implements Disposable {

	private final AudioManager audioManager = new AudioManager();
	private final Map<String, TextureAtlas> atlases = new HashMap<>();
	private final Map<String, Animation> animations = new HashMap<>();

	public Animation getAnimation(String fileName) {
		Objects.requireNonNull(fileName);
		
		// Load animation and cache it
		if (!animations.containsKey(fileName)) {
			Animation anim = Animation.createFromJson(Gdx.files.internal(fileName), atlases);
			animations.put(fileName, anim);
		}
		
		return animations.get(fileName);
	}
	
	public TextureAtlas getAtlas(String fileName) {
		Objects.requireNonNull(fileName);
		
		// Load atlas and cache it
		if (!atlases.containsKey(fileName)) {
			TextureAtlas atlas = TextureAtlas.createFromJson(Gdx.files.internal(fileName));
			atlases.put(fileName, atlas);
		}
		
		return atlases.get(fileName);
	}
	
	public AudioManager getAudio() {
		return audioManager;
	}
	
	public int getLoadedAnimationCount() {
		return animations.size();
	}
	
	public int getLoadedAtlasCount() {
		return atlases.size();
	}
	
	@Override
	public void dispose() {
		audioManager.dispose();
		atlases.forEach((file, atlas) -> atlas.dispose());
	}
}
