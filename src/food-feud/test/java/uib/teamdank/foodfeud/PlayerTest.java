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
package uib.teamdank.foodfeud;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uib.teamdank.common.LibGdxDependentTest;
import uib.teamdank.common.util.AssetManager;

public class PlayerTest extends LibGdxDependentTest {
	private Player player;
	
	
	@Before
	public void setUp() {
		player = new Player(new AssetManager(), Team.ALPHA, "sturle");
	}
	
//	@Test
//	public void addWeapon() {
//		Weapon w = new Weapon("TestWeapon", "TestDescription");
//		player.getInventory().addItem(w);
//		assertThat(player.getInventory().getItem(0), is(equalTo(w)));
//	}
	
	@Test
	public void getHealth(){
		assertThat(player.getHealth(), is(equalTo(100)));
	}
	
	@Test
	public void getName(){
		assertThat(player.getName(), is(equalTo("sturle")));
	}
	

}
