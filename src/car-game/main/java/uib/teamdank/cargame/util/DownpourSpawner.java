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
package uib.teamdank.cargame.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import uib.teamdank.common.GameObject;
import uib.teamdank.common.gui.Layer;

public class DownpourSpawner extends ScrollingSpawner {
    private Queue<Array<GameObject>> rows = new Queue<>();
    private float lastHeight;

    public DownpourSpawner(Layer layer, OrthographicCamera camera, WeatherGenerator weatherGenerator) {
        super(layer, camera, weatherGenerator);
    }

    private GameObject createNewSpawn(float x, float y) {
        GameObject spawn = getGenerator().generate(getRandom());
        spawn.getPosition().y = y;
        spawn.getPosition().x = x;
        spawn.setFlipHorizontally(false);
        getSpawns().add(spawn);
        getLayer().addGameObject(spawn);
        lastHeight = spawn.getHeight();
        return spawn;
    }

    private void setup() {
        rows = new Queue<>();
        float y = getCamera().position.y - getCamera().viewportHeight / 2;
        while(y < getCamera().position.y + getCamera().viewportHeight / 2) {
            Array<GameObject> row = spawnRow(y);
            rows.addLast(row);
            y += lastHeight;
        }
        rows.addLast(spawnRow(y));
    }

    private Array<GameObject> spawnRow(float y) {
        Array<GameObject> row = new Array<>();
        float x = 0 - getCamera().viewportWidth / 2;
        while (x < getCamera().viewportWidth / 2) {
            GameObject sp = createNewSpawn(x, y);
            row.add(sp);
            x += sp.getWidth();
        }
        return row;
    }

    @Override
    public void update(float delta) {
        if(rows.size == 0) {
            setup();
        }

        else {
            Array<GameObject> row = rows.first();

            if(row.get(0).getPosition().y + lastHeight < getCamera().position.y - getCamera().viewportHeight / 2) {
                moveRowUp(row);
                rows.addLast(rows.removeFirst());
            }
        }
    }

    private void moveRowUp(Array<GameObject> row) {
        for(int i = 0; i < row.size; i++) {
            Vector2 pos = row.get(i).getPosition();
            if(rows.size > 1)
                pos.y = rows.last().get(0).getPosition().y + lastHeight;
            else
                pos.y += lastHeight;
            pos.x = row.get(i).getWidth()*i - getCamera().position.x - getCamera().viewportWidth / 2;
        }
    }
}
