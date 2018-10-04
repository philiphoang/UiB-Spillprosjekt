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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uib.teamdank.common.util.Animation;
import uib.teamdank.common.util.AssetManager;

public class AssetManagerTest extends LibGdxDependentTest {

	public static void main(String[] args) {
		new LwjglApplication(new Game() {
			private static final float BIRD_SCALE = 12f;

			private SpriteBatch batch;
			private AssetManager assets;
			private Animation animation;

			@Override
			public void create() {
				this.batch = new SpriteBatch();
				this.assets = new AssetManager();
				assets.getAtlas("bird_atlas.json");
				this.animation = assets.getAnimation("bird_anim.json");
			}
			
			@Override
			public void dispose() {
				batch.dispose();
				assets.dispose();
			}

			public void render() {
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

				animation.update(Gdx.graphics.getDeltaTime());
				
				final TextureRegion frame = animation.getTexture();
				final int w = (int) (frame.getRegionWidth() * BIRD_SCALE);
				final int h = (int) (frame.getRegionHeight() * BIRD_SCALE);
				final int x = (Gdx.graphics.getWidth() - w) / 2;
				final int y = (Gdx.graphics.getHeight() - h) / 2;
				batch.begin();
				batch.draw(frame, x, y, w, h);
				batch.end();
			}
		});
	}

	@Test
	public void testAnimationCached() {
		AssetManager assets = new AssetManager();
		assets.getAnimation("bird_anim.json");
		assets.getAnimation("bird_anim.json");
		assertThat(assets.getLoadedAnimationCount(), is(1));
	}

	@Test
	public void testAtlasCached() {
		AssetManager assets = new AssetManager();
		assets.getAtlas("bird_atlas.json");
		assets.getAtlas("bird_atlas.json");
		assertThat(assets.getLoadedAtlasCount(), is(1));
	}
	
	@Test
	public void testSharedAtlas() {
		AssetManager assets = new AssetManager();
		assets.getAtlas("bird_atlas.json");
		assets.getAnimation("bird_anim.json");
		assertThat(assets.getLoadedAtlasCount(), is(1));
	}
	
}
