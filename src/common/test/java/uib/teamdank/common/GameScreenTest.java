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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.function.Consumer;

import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;

import uib.teamdank.common.Game;
import uib.teamdank.common.GameObject;
import uib.teamdank.common.gui.GameScreen;
import uib.teamdank.common.gui.Layer;

public class GameScreenTest {
	Game mockG;
	GameScreen gs;
	Layer mockL1;

	@Before
	public void setUp() throws Exception {
		mockG = mock(Game.class);
		gs = new GameScreen(mockG);
		mockL1 = mock(Layer.class);
	}

	@Test
	public void testAddedLayerCannotBeNullInGameScreen() {
		try {
			gs.addLayer(null);
			assertTrue(false);
		} catch(NullPointerException e) {
			assertTrue(true);
		} catch(Exception e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testAddLayerToGameScreen() {
		try{
			gs.addLayer(mockL1);
			assertTrue(true);
		} catch (Exception e){
			assertTrue(false);
		}
	}
	
	@Test
	public void testAddGameObjectToLayerInGameScreen() {
		testAddLayerToGameScreen();
		try{
			gs.addGameObject(mockL1, mock(GameObject.class));
			verify(mockL1).addGameObject(any(GameObject.class));
			assertTrue(true);
		} catch (Exception e){
			assertTrue(false);
		}
	}
	
	@Test
	public void testGameCannotBeNullInGameScreen() {
		try {
			gs = new GameScreen(null); 
			assertTrue(false);
		} catch(NullPointerException e) {
			assertTrue(true);
		} catch(Exception e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void testPauseInGameScreen() {
		// Will throw a RuntimeException if the method is called:
		String s = "PauseMenuScreen is now active";
		doThrow(new RuntimeException(s)).when(mockG).setScreen(mockG.getPauseMenuScreen());
		
		try{
			gs.pause();
			assertTrue(false);
		} catch (RuntimeException e){
			assertEquals(e.getMessage(), s);
		} catch (Exception e){
			assertTrue(false);
		}
	}
	
	@Test
	public void testForEachGameObjectInGameScreen() {
		Consumer<GameObject> action = (obj -> { });
		
		// Test on GameScreen with 0 layers
		try {
			gs.forEachGameObject(action);
		} catch (Exception e){
			assertTrue(false);
		}
		
		// Test on GameScreen with 1 layer
		testAddLayerToGameScreen();
		gs.forEachGameObject(action);
		
		// Test on GameScreen with 2 layers
		Layer mockL2 = mock(Layer.class);
		gs.addLayer(mockL2);
		gs.forEachGameObject(action);
		
		verify(mockL1, times(2)).forEachGameObject(action);
		verify(mockL2).forEachGameObject(action);
	}
	
	/**
	 * This test is dependent on {@link Layer} working
	 */
	@Test
	public void testUpdateInGameScreen() {
		GameObject obj = new GameObject();
		obj.getPosition().set(1, 1);
		obj.getVelocity().set(1, 2);
		
		Layer l = new Layer(false);
		l.addGameObject(obj);
		
		gs.addLayer(l);
		gs.update(1);
		
		assertEquals(new Vector2(2, 3), obj.getPosition());
	}
	
}
