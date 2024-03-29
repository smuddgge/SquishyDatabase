package com.github.smuddgge.squishydatabase.record;

import com.github.smuddgge.squishydatabase.Query;
import com.github.smuddgge.squishydatabase.interfaces.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a record in the {@link Database}
 */
public class Record {

    /**
     * Used to get all the record fields.
     * The {@link IgnoreField} fields will be left out.
     *
     * @return The full list of record fields.
     */
    public List<RecordField> getFieldList() {
        List<RecordField> recordFieldList = new ArrayList<>();

        // Loop though every field in the class.
        for (Field field : this.getClass().getFields()) {
            RecordField recordField = new RecordField(this, field);

            if (recordField.isIgnored()) continue;
            recordFieldList.add(recordField);
        }

        return recordFieldList;
    }

    /**
     * Used to get the record field list that only contains
     * one type of field.
     * The {@link IgnoreField} fields will be left out.
     *
     * @param type The type of field to get.
     * @return The list of fields with the specific
     * record field type.
     */
    public List<RecordField> getFieldList(RecordFieldType type) {
        List<RecordField> recordFieldList = new ArrayList<>();

        // Loop though every record field.
        for (RecordField recordField : this.getFieldList()) {

            // Check if the field is not the correct type.
            if (recordField.getFieldType() != type) continue;
            recordFieldList.add(recordField);
        }

        return recordFieldList;
    }

    /**
     * Used to get the list of field names.
     *
     * @return The names of the fields in this record.
     */
    public List<String> getFieldNameList() {
        List<String> fieldNameList = new ArrayList<>();

        for (RecordField recordField : this.getFieldList()) {
            fieldNameList.add(recordField.getKey());
        }

        return fieldNameList;
    }

    /**
     * Used to get a field from this record.
     *
     * @param name The name of the record.
     * @return The requested record field.
     */
    public @Nullable RecordField getField(@NotNull String name) {
        for (RecordField recordField : this.getFieldList()) {
            if (recordField.getKey().equals(name)) return recordField;
        }
        return null;
    }

    /**
     * Used to get the primary key.
     *
     * @return The instance of the primary key.
     */
    public @NotNull RecordField getPrimaryKey() {
        return this.getFieldList(RecordFieldType.PRIMARY).get(0);
    }

    /**
     * Used to append the result set to this record.
     *
     * @param results The instance of the results.
     * @return This instance.
     * @throws SQLException If a field does not exist in the result.
     */
    public Record append(ResultSet results) throws SQLException {
        for (RecordField recordField : this.getFieldList()) {
            recordField.setValue(results.getObject(recordField.getKey()));
        }

        return this;
    }

    /**
     * Used to get the record as a query.
     *
     * @return The requested query.
     */
    public @NotNull Query asQuery() {
        Query query = new Query();

        for (RecordField field : this.getFieldList()) {
            if (field.getValue() == null) continue;
            query.match(field.getKey(), field.getValue());
        }

        return query;
    }

    /**
     * <h1>Used to toggle a boolean</h1>
     * It return true when false and false when true.
     *
     * @param variable The instance of a variable.
     */
    public static @NotNull Object getOpposite(@NotNull Object variable) {

        // If the field is a string.
        if (variable instanceof String value) {
            if (value.equalsIgnoreCase("true")) return "false";
            return "true";
        }

        // If the field is a boolean
        if (variable instanceof Boolean value) {
            return !value;
        }

        // If the field is an integer.
        if (variable instanceof Integer value) {
            return value == 1 ? 0 : 1;
        }

        throw new RuntimeException("Variable isn't a string, boolean or integer.");
    }
}
