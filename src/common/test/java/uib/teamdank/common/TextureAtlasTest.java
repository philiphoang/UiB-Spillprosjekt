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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import uib.teamdank.common.util.TextureAtlas;

public class TextureAtlasTest extends LibGdxDependentTest {

	public static void main(String[] args) {
		new LwjglApplication(new Game() {
			private static final float BIRD_SCALE = 12f;

			private SpriteBatch batch;
			private TextureAtlas atlas;

			private Animation<TextureRegion> birdAnim;
			private float time = 0;

			@Override
			public void create() {
				this.batch = new SpriteBatch();
				this.atlas = TextureAtlas.createFromJson(Gdx.files.internal("bird_atlas.json"));

				this.birdAnim = new Animation<>(.1f, atlas.getRegion("bird1"), atlas.getRegion("bird2"), atlas.getRegion("bird3"), atlas.getRegion("bird4"), atlas.getRegion("bird5"));
				birdAnim.setPlayMode(PlayMode.LOOP);
			}

			@Override
			public void dispose() {
				batch.dispose();
				atlas.dispose();
			}
			
			public void render() {
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

				time += Gdx.graphics.getDeltaTime();

				final TextureRegion frame = birdAnim.getKeyFrame(time);
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

	@Test(expected = NullPointerException.class)
	public void testTextureAtlasFactoryNull() {
		TextureAtlas.createFromJson(null);
	}

	@Test
	public void testTextureAtlasLoadedProperly() {
		TextureAtlas atlas = TextureAtlas.createFromJson(Gdx.files.internal("bird_atlas.json"));
		String[] regions = { "bird1", "bird2", "bird3", "bird4", "bird5" };
		for (String region : regions) {
			assertThat(atlas.getRegion(region), notNullValue());
			assertThat(atlas.getRegion(region).getRegionWidth(), is(19));
			assertThat(atlas.getRegion(region).getRegionHeight(), is(22));
		}
	}
	
}
