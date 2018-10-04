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

import org.junit.Before;
import org.junit.Test;
import uib.teamdank.common.Item;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Testing class for Item
 *
 * @author oli007
 */
public class ItemTest {
    private String name;
    private String desc;
    private Item item;

    @Before
    public void setup(){
        name = "Ost";
        desc = "En ost";
        item = new Item(name, desc);
    }

    @Test
    public void testGetDescription() throws Exception {
        assertThat(desc, is(equalTo(item.getDescription())));
    }

    @Test
    public void testGetName() throws Exception {
        assertThat(name, is(equalTo(item.getName())));
    }

}