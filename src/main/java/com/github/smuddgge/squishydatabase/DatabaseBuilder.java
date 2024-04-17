package com.github.smuddgge.squishydatabase;

import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import com.github.smuddgge.squishydatabase.implementation.mongo.MongoDatabase;
import com.github.smuddgge.squishydatabase.implementation.mysql.MySQLDatabase;
import com.github.smuddgge.squishydatabase.implementation.sqlite.SQLiteDatabase;
import com.github.smuddgge.squishydatabase.interfaces.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Represents the database builder.
 * <p>
 * A better way of connecting to a database.
 * <p>
 * This is used to build a database connection.
 * <p>
 * After obtaining an instance of the database,
 * the method {@link Database#setup()} can be called
 * to attempt to connect.
 */
public class DatabaseBuilder {

    private String type;
    private String path;
    private String connectionString;
    private String databaseName;
    private String username;
    private String password;

    public DatabaseBuilder() {
    }

    /**
     * Used to create a database builder.
     *
     * @param section The configuration section that contains
     *                the database connection infomation.
     * @param path    The path that should be used for the sqlite
     *                database connection.
     */
    public DatabaseBuilder(@NotNull ConfigurationSection section, @NotNull String path) {
        this.type = section.getString("type").toUpperCase();
        this.path = path;
        this.connectionString = section.getString("connectionString");
        this.databaseName = section.getString("databaseName");
        this.username = section.getString("username");
        this.password = section.getString("password");
    }

    /**
     * Used to get the type of database as an
     * uppercase string.
     *
     * @return The type of database.
     */
    public @Nullable String getType() {
        return type;
    }

    /**
     * Used to get the path that's used to connect to
     * the sqlite database.
     *
     * @return The path to the sqlite database.
     */
    public @Nullable String getPath() {
        return path;
    }

    /**
     * Used to get the connection string.
     * <p>
     * This is the url used to connect to a database
     * such as Mongo or MySql.
     *
     * @return The connection string.
     */
    public @Nullable String getConnectionString() {
        return connectionString;
    }

    /**
     * Used to get the database's name.
     * <p>
     * This is used when connecting to a mongo database.
     *
     * @return The database's name.
     */
    public @Nullable String getDatabaseName() {
        return databaseName;
    }

    /**
     * Used to get the username used to log into the database.
     * <p>
     * This can be used when connecting to a mysql database.
     * However, if this is set the password should also be set.
     *
     * @return The database username.
     */
    public @Nullable String getUsername() {
        return username;
    }

    /**
     * Used to get the password used to log into the database.
     * <p>
     * This can be used when connecting to a mysql database.
     * However, if the username is not set it will try to just
     * use the connection string.
     *
     * @return The database password.
     */
    public @Nullable String getPassword() {
        return password;
    }

    /**
     * Used to get the instance of the database factory
     * that corresponds to the database type specified
     * in the configuration.
     *
     * @return The instance of the database factory.
     */
    public @NotNull DatabaseFactory getFactory() {
        return DatabaseFactory.valueOf(this.getType());
    }

    /**
     * Used to set the type of database to build.
     * <p>
     * You can provide a lowercase string, as this method
     * will also make it uppercase.
     *
     * @param type The type of database to build.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setType(@NotNull String type) {
        this.type = type.toUpperCase();
        return this;
    }

    /**
     * Used to set the path of the database.
     * <p>
     * This is used when creating a sqlite database.
     *
     * @param path The path to the database file.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setPath(@NotNull String path) {
        this.path = path;
        return this;
    }

    /**
     * Used to set the connection string.
     * <p>
     * This is used in most database connections to log into the database.
     *
     * @param connectionString The instance of the connection string.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setConnectionString(@NotNull String connectionString) {
        this.connectionString = connectionString;
        return this;
    }

    /**
     * Used to set the database name.
     * <p>
     * This will be used in a mongo database connection.
     *
     * @param databaseName The name of the database.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setDatabaseName(@NotNull String databaseName) {
        this.databaseName = databaseName;
        return this;
    }

    /**
     * Used to set the username to log in with.
     * <p>
     * This can be used in a mysql connection.
     * However, if the password is not set it will fail.
     *
     * @param username The username used to log into the database.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setUsername(@NotNull String username) {
        this.username = username;
        return this;
    }

    /**
     * Used to set the password to log in with.
     * <p>
     * This can be used in a mysql connection
     * However, if the username is not set it will
     * just try to use the connection string.
     *
     * @param password The password used to log into the database.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setPassword(@NotNull String password) {
        this.password = password;
        return this;
    }

    /**
     * Used to build a sqlite database.
     *
     * @param path The path to the sqlite file.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setSqlite(@NotNull String path) {
        this.type = "SQLITE";
        this.path = path;
        return this;
    }

    /**
     * Used to build a mysql database.
     *
     * @param connectionString The connection string.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setMySql(@NotNull String connectionString) {
        this.type = "MYSQL";
        this.connectionString = connectionString;
        return this;
    }

    /**
     * Used to build a mysql database with username and password separate.
     *
     * @param connectionString The connection string to use.
     * @param username         The username to use.
     * @param password         The password to use.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setMySql(@NotNull String connectionString, @NotNull String username, @NotNull String password) {
        this.type = "MYSQL";
        this.connectionString = connectionString;
        this.username = username;
        this.password = password;
        return this;
    }

    /**
     * Used to build a mongo database.
     *
     * @param connectionString The connection string to use.
     * @param databaseName     The database name that will be logged into.
     * @return This instance.
     */
    public @NotNull DatabaseBuilder setMongo(@NotNull String connectionString, @NotNull String databaseName) {
        this.type = "MONGO";
        this.connectionString = connectionString;
        this.databaseName = databaseName;
        return this;
    }

    /**
     * Used to build a connection to a database.
     *
     * @return The instance of the database.
     */
    public @NotNull Database build() {

        // Check if the database type is MYSQL.
        if (this.getFactory().equals(DatabaseFactory.MYSQL)) {
            if (this.getConnectionString() == null) throw new NullPointerException(
                    "You must specify a connection string for a MySQL database."
            );
            if (this.username == null) {
                return new MySQLDatabase(this.getConnectionString());
            }
            if (this.password == null) throw new NullPointerException(
                    "You must specify a password for a MySQL database if you provide a username."
            );
            return new MySQLDatabase(this.getConnectionString());
        }

        // Check if the database type is MONGO.
        if (this.getFactory().equals(DatabaseFactory.MONGO)) {
            if (this.getConnectionString() == null) throw new NullPointerException(
                    "You must specify a connection string for a Mongo database."
            );
            if (this.getDatabaseName() == null) throw new NullPointerException(
                    "You must specify a database name for a Mongo database."
            );
            return new MongoDatabase(this.getConnectionString(), this.getDatabaseName());
        }

        // Check if the database type is SQLITE.
        if (this.getFactory().equals(DatabaseFactory.SQLITE)) {
            if (this.path == null) throw new NullPointerException(
                    "You must specify a path for a SQLite database."
            );
            return new SQLiteDatabase(new File(this.path));
        }

        // Otherwise, the database type is invalid.
        throw new NullPointerException("Unknown database type: " + this.getType());
    }
}