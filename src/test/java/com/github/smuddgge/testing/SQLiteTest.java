package com.github.smuddgge.testing;

import com.github.smuddgge.DatabaseCredentials;
import com.github.smuddgge.DatabaseFactory;
import com.github.smuddgge.interfaces.Database;
import com.github.smuddgge.records.Customer;
import com.github.smuddgge.tables.CustomerTable;
import com.github.smuddgge.utility.Query;
import com.github.smuddgge.utility.results.ResultChecker;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

/**
 * <h1>Represents the sqlite tests</h1>
 */
public class SQLiteTest {

    /**
     * Create a database factory.
     */
    public static DatabaseFactory databaseFactory = DatabaseFactory.SQLITE;

    @Test
    public void testGetFirstRecord() {
        Database database = databaseFactory.create(
                new DatabaseCredentials().setPath("src/test/resources/database.sqlite3")
        ).setDebugMode(true);

        // Create table.
        CustomerTable customerTable = new CustomerTable();
        database.createTable(customerTable);

        // Create a new record.
        Customer customer = new Customer();
        customer.identifier = UUID.randomUUID().toString();
        customer.name = "Smudge";

        // Insert the record.
        customerTable.insertRecord(customer);

        // Get the record back from the table.
        Customer result = customerTable.getFirstRecord(
                new Query().match("identifier", customer.identifier)
        );

        assert result != null;

        // Check results.
        new ResultChecker()
                .expect(result.identifier, customer.identifier)
                .expect(result.name, customer.name);
    }

    @Test
    public void testGetRecordList() {
        Database database = databaseFactory.create(
                new DatabaseCredentials().setPath("src/test/resources/database.sqlite3")
        ).setDebugMode(true);

        // Create table.
        CustomerTable customerTable = new CustomerTable();
        database.createTable(customerTable);

        // Create a new record.
        Customer customer1 = new Customer();
        customer1.identifier = UUID.randomUUID().toString();
        customer1.name = UUID.randomUUID().toString();

        Customer customer2 = new Customer();
        customer2.identifier = UUID.randomUUID().toString();
        customer2.name = customer1.name;

        // Insert the record.
        customerTable.insertRecord(customer1);
        customerTable.insertRecord(customer2);

        // Get the record back from the table.
        List<Customer> result = customerTable.getRecordList(
                new Query().match("name", "Smudge")
        );

        assert result != null;

        // Check results.
        new ResultChecker()
                .expect(result.get(0).identifier, customer1.identifier)
                .expect(result.get(0).name, customer1.name)
                .expect(result.get(1).identifier, customer2.identifier)
                .expect(result.get(1).name, customer2.name);
    }
}