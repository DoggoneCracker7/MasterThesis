package com.pl.masterthesis.core.binding;

import com.pl.masterthesis.models.Connection;
import com.pl.masterthesis.models.Interface;
import javafx.scene.shape.Line;

import java.util.*;

public final class ConnectionPool {
    private static final ConnectionPool INSTANCE = new ConnectionPool();
    private Map<Connection, Line> connectionLineMap;

    public static ConnectionPool get() {
        return INSTANCE;
    }

    private ConnectionPool() {
        this.connectionLineMap = new HashMap<>();
    }

    public void addConnection(Connection... connections) {
        Arrays.stream(connections).forEach(connection -> connectionLineMap.put(connection, null));
    }

    public void addConnection(Connection connection, Line line) {
        connectionLineMap.put(connection, line);
    }

    public Optional<Connection> getConnection(Interface sourceInterface) {
        Objects.requireNonNull(sourceInterface, "sourceInterface cannot be null");
        for (Connection connection : connectionLineMap.keySet()) {
            if (connection.containsInterface(sourceInterface)) {
                return Optional.of(connection);
            }
        }
        return Optional.empty();
    }

    public Map<Connection, Line> getConnectionLineMap() {
        return connectionLineMap;
    }
}
