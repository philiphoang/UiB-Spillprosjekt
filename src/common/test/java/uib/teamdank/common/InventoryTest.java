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

import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * Created by oml35 on 20-Apr-17.
 */
public class InventoryTest {
    private Inventory inventory;
    private Random random = new Random();

    @Before
    public void setUp() throws Exception {
        int maxCapacity = random.nextInt(1000) + 1;
        int gold = random.nextInt(1000);
        inventory = new Inventory(maxCapacity, gold);
    }

    @Test
    public void testAddGold() throws Exception {
        int amount = random.nextInt(1000);
        int result = inventory.getGold() + amount;
        inventory.addGold(amount);

        assertThat(result, is(equalTo(inventory.getGold())));
    }

    @Test
    public void testAddItem() throws Exception {
        int step = random.nextInt(inventory.getCapacity());
        Item item1 = new Item("name", "description");
        Item item2 = new Item("Cheese", "Delicious");
        for (int i = 0; i < step; i++) {
            if (i % 3 == 0)
                inventory.addItem(item1);
            else
                inventory.addItem(item2);
        }

        for (int i = 0; i < step - 1; i++) {
            if (i % 3 == 0)
                assertThat(item1, is(equalTo(inventory.getItem(i))));
            else
                assertThat(item2, is(equalTo(inventory.getItem(i))));
        }
    }

    @Test
    public void testAddToFullInventory() throws Exception {
        int step = random.nextInt(1000);
        int capacity = step;

        inventory = new Inventory(capacity);
        Item item = new Item("name", "desc");
        for (int i = 0; i < step; i++) {
            inventory.addItem(item);
        }

        Item item1 = new Item("Cheese", "Delicious");
        inventory.addItem(item1);

        assertThat(item, is(equalTo(inventory.getItem(inventory.getItemCount() - 1))));
    }

    @Test
    public void testGetCapacity() throws Exception {
        int capacity = random.nextInt(1000);
        inventory = new Inventory(capacity);

        assertThat(capacity, is(equalTo(inventory.getCapacity())));
    }

    @Test
    public void testGetGold() throws Exception {
        int capacity = random.nextInt(1000);
        int gold = random.nextInt(1000);
        inventory = new Inventory(capacity, gold);

        assertThat(gold, is(equalTo(inventory.getGold())));
    }

    @Test
    public void testGetItemCount() throws Exception {
        int capacity = random.nextInt(1000);
        int step = random.nextInt(capacity);
        inventory = new Inventory(capacity);
        Item item = new Item("name", "desc");

        for (int i = 0; i < step; i++) {
            inventory.addItem(item);
        }

        assertThat(step, is(equalTo(inventory.getItemCount())));
    }

    @Test
    public void testGetItems() throws Exception {
        inventory = new Inventory(3);
        Item item1 = new Item("Cheese", "Good");
        Item item2 = new Item("Beer", "Tuborg Lite");
        Item item3 = new Item("Wine", "Nice");

        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.addItem(item3);

        assertThat(item1, is(equalTo(inventory.getItem(0))));
        assertThat(item2, is(equalTo(inventory.getItem(1))));
        assertThat(item3, is(equalTo(inventory.getItem(2))));

    }

    @Test
    public void testIsFull() throws Exception {
        int step = random.nextInt(1000);
        int capacity = step;

        inventory = new Inventory(capacity);
        Item item = new Item("name", "desc");
        for (int i = 0; i < step; i++) {
            inventory.addItem(item);
        }

        assertThat(true, is(equalTo(inventory.isFull())));
    }

    @Test
    public void testRemoveGold() throws Exception {
        int capacity = random.nextInt(1000);
        int gold = random.nextInt(1000);
        int decrease = random.nextInt(gold);

        inventory = new Inventory(capacity, gold);
        inventory.removeGold(decrease);

        assertThat(gold - decrease, is(equalTo(inventory.getGold())));
    }

    @Test
    public void testRemoveItem() throws Exception {
        inventory = new Inventory(1);
        Item item = new Item("name", "desc");
        inventory.addItem(item);

        assertThat(item, is(equalTo(inventory.getItem(0))));

        inventory.removeItem(0);

        assertThat(null, is(equalTo(inventory.getItem(0))));
    }

}