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

    public Optional<Map.Entry<Connection, Line>> getConnectionLineByInterface(Interface sourceInterface) {
        Objects.requireNonNull(sourceInterface, "sourceInterface cannot be null");
        for (Map.Entry<Connection, Line> entry : connectionLineMap.entrySet()) {
            if (entry.getKey().containsInterface(sourceInterface)) {
                return Optional.of(entry);
            }
        }
        return Optional.empty();
    }

    public Map<Connection, Line> getConnectionLineMap() {
        return connectionLineMap;
    }
}
