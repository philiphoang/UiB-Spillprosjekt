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
import java.util.function.BiConsumer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * A texture atlas manages named regions on a larger tileset which can be
 * retrieved by calling {@link #getRegion(String)}.
 * <p>
 * <b>Note:</b>When an atlas is created the entire tileset image
 * is loaded into memory. Call {@link #dispose()} when done.
 */
public class TextureAtlas implements Disposable {

	private static class Region {
		@SerializedName("name") String name;
		@SerializedName("x") int x;
		@SerializedName("y") int y;
		@SerializedName("w") int w;
		@SerializedName("h") int h;
		@SerializedName("userPointX") int userPointX = 0;
		@SerializedName("userPointY") int userPointY = 0;
	}
	
	/**
	 * Creates a new texture atlas from the specification in the given
	 * JSON-file.
	 */
	public static TextureAtlas createFromJson(FileHandle handle){
		Objects.requireNonNull(handle, "file handle cannot be null");
		TextureAtlas atlas = new Gson().fromJson(handle.reader("UTF-8"), TextureAtlas.class);
		atlas.load();
		return atlas;
	}
	
	@SerializedName("tileset") private String tilesetFile;
	@SerializedName("regions") private Region[] regions;
	
	private transient Map<String, TextureRegion> textureCache = new HashMap<>();
	private transient Map<String, Vector2> userPointChache = new HashMap<>();
	private transient Texture tileset;
	
	private TextureAtlas() {
		// Hide constructor
	}
	
	@Override
	public void dispose() {
		tileset.dispose();
	}

	public void forEachRegion(BiConsumer<String, TextureRegion> action) {
		Objects.requireNonNull(action, "action cannot be null");
		textureCache.forEach(action);
	}
	
	/**
	 * @return a copy of all regions in this texture atlas
	 */
	public TextureRegion[] getAllRegions() {
		return textureCache.values().toArray(new TextureRegion[textureCache.size()]);
	}
	
	public Vector2 getUserPoint(String name) {
		validateRegion(name);
		return userPointChache.get(name);
	}
	
	/**
	 * @return the texture region with the given name 
	 */
	public TextureRegion getRegion(String name) {
		validateRegion(name);
		return textureCache.get(name);
	}
	
	private void validateRegion(String name) {
		if (tileset == null) {
			throw new IllegalStateException("texture atlas has not loaded the tileset yet, call #load()");
		}
		if (!textureCache.containsKey(name)) {
			throw new IllegalArgumentException("no such region: " + name);
		}
	}

	private void load() {
		FileHandle sheetFile = Gdx.files.internal(tilesetFile);
		if (sheetFile == null) {
			throw new IllegalArgumentException("no such tileset: " + tilesetFile);
		}
		this.tileset = new Texture(sheetFile);
		for (Region region : regions) {
			textureCache.put(region.name, new TextureRegion(tileset, region.x, region.y, region.w, region.h));
			userPointChache.put(region.name, new Vector2(region.userPointX, region.userPointY));
		}
	}
	
}
