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
package uib.teamdank.spooks;

import org.junit.Before;
import org.junit.Test;
import uib.teamdank.common.Item;


import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by oml35 on 25-Apr-17.
 */
public class ItemContainerTest {
    Item item;
    ItemContainer i;

    @Before
    public void setUp() throws Exception {
        i = new ItemContainer("Shelf", "Store objects");
        item = new Item ("Sturle", "What a lad");
        i.getInventory().addItem(item);
    }

    @Test
    public void getInventory() throws Exception {
        int index = i.getInventory().getItemCount() -1;
        assertThat(item, is(equalTo(i.getInventory().getItem(index))));
    }

}