package ru.araclecore.battlecore.connection.connection;

import org.bukkit.plugin.java.JavaPlugin;
import ru.araclecore.battlecore.connection.Connection;
import ru.araclecore.battlecore.connection.utilities.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Statement {

    private String table;
    private String selection;
    private String key;
    private final List<String> values = new ArrayList<>();
    private Condition condition = new Condition("", "");

    private final List<Modifier> modifiers = new ArrayList<>();
    private final List<Column> columns = new ArrayList<>();

    public Condition condition() {
        return condition;
    }

    public void condition(String column, String value) {
        this.condition = new Condition(column, parsValue(value));
    }

    public String selection() {
        return selection;
    }

    public void selection(String selection) {
        this.selection = selection;
    }

    public String key() {
        return key;
    }

    public void key(String key) {
        this.key = key;
    }

    public String table() {
        return table;
    }

    public void table(String table) {
        this.table = table;
    }

    public void value(String value) {
        values.add(parsValue(value));
    }

    private final JavaPlugin instance;

    public Statement(JavaPlugin instance) {
        this.instance = instance;
    }

    private String parsValue(String value) {
        if (value.matches("^.*\\D.*$")) value = "'" + value + "'";
        return value;
    }

    public String values() {
        AtomicReference<String> values = new AtomicReference<>("");
        this.values.forEach(value -> values.set(values + "," + value));
        return values.get().replaceFirst(",", "");
    }

    public void modifier(String column, String value) {
        modifiers.add(new Modifier(column, parsValue(value)));
    }

    public String modifiers() {
        AtomicReference<String> modifiers = new AtomicReference<>("");
        this.modifiers.forEach(modifier -> modifiers.set(modifiers + "," + modifier.column + " = " + modifier.value));
        return modifiers.get().replaceFirst(",", "");
    }

    public void selection(String name, String type) {
        columns.add(new Column(name, type));
    }

    public void column(String name, String type) {
        columns.add(new Column(name, type));
    }

    public String columns() {
        AtomicReference<String> columns = new AtomicReference<>("");
        this.columns.forEach(column -> columns.set(columns + "," + column.name + " " + column.type));
        return columns.get().replaceFirst(",", "");
    }

    public String create(KEY key) {
        String statement = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", table(), columns());
        if (key.equals(KEY.ENABLE))
            statement = String.format("CREATE TABLE IF NOT EXISTS %s (%s, PRIMARY KEY  (%s))", table(), columns(), key());
        if (Connection.settings.Boolean("Debug")) Logger.info(instance, "Debug: " + statement);
        return statement;
    }

    public String insert() {
        String statement = String.format("INSERT INTO %s VALUES (%s)", table(), values());
        if (Connection.settings.Boolean("Debug")) Logger.info(instance, "Debug: " + statement);
        return statement;
    }

    public List<String> select() {
        List<String> query = new ArrayList<>();
        String statement = "SELECT %s FROM %s WHERE %s";
        statement = String.format(statement, selection(), table(), condition.column + " = " + condition.value);
        if (Connection.settings.Boolean("Debug")) Logger.info(instance, "Debug: " + statement);
        query.add(statement);
        query.add(selection());
        return query;
    }

    public String update() {
        String statement = String.format("UPDATE %s SET %s WHERE %s", table(), modifiers(), condition.column + " = " + condition.value);
        if (Connection.settings.Boolean("Debug")) Logger.info(instance, "Debug: " + statement);
        return statement;
    }

    private record Modifier(String column, String value) {
    }

    private record Column(String name, String type) {
    }

    private record Condition(String column, String value) {
    }

    public enum KEY {
        ENABLE,
        DISABLE
    }
}

