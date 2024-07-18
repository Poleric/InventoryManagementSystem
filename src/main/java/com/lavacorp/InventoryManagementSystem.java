package com.lavacorp;

import com.lavacorp.database.Database;
import com.lavacorp.entities.InventoryDao;
import com.lavacorp.entities.Item;
import com.lavacorp.entities.ItemDao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

public class InventoryManagementSystem {
    private final static Logger LOGGER = LogManager.getLogger(Inventory.class);

    public static void main(String[] args) {
        Database.getInstance()
                .connect("inventory.db");

        try (Handle handle = Database.getInstance().getJdbi().open()) {
            handle.execute("""
            PRAGMA writable_schema = 1;
            delete from sqlite_master where type in ('table', 'index', 'trigger');
            PRAGMA writable_schema = 0;
            
            VACUUM;
            """);

            ItemDao itemDao = handle.attach(ItemDao.class);
            itemDao.createTable();
            InventoryDao inventoryDao = handle.attach(InventoryDao.class);
            inventoryDao.createTable();

            Item[] items = {
                    new Item("Lava Cup Noodle", 3.0),
                    new Item("Magma Cup Noodle", 3.5),
                    new Item("LAVA Lava Cake", 4.0)
            };
            for (Item item : items)
                try {
                    itemDao.create(item);
                } catch (UnableToExecuteStatementException e) {
                    LOGGER.error(e.getMessage());
                }

            for (Item item : itemDao.getAllItem())
                System.out.println("item_id = " + itemDao.getItemId(item) + " item = " + item);

            inventoryDao.increaseCount(1, 2);
            inventoryDao.increaseCount(2, 4);
            inventoryDao.increaseCount(3, 5);
            inventoryDao.decreaseCount(3, 1);

            System.out.println();

            System.out.println("inventoryDao.queryCount(3) = " + inventoryDao.queryCount(3));
        }
    }
}
