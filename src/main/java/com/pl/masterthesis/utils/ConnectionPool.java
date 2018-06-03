package com.pl.masterthesis.utils;

import com.pl.masterthesis.models.Connection;
import com.pl.masterthesis.models.Interface;

import java.util.*;

public final class ConnectionPool {
    private static final ConnectionPool INSTANCE = new ConnectionPool();
    private List<Connection> connections;

    public static ConnectionPool get() {
        return INSTANCE;
    }

    private ConnectionPool() {
        this.connections = new ArrayList<>();
    }

    public void addConnection(Connection... connection) {
        connections.addAll(Arrays.asList(connection));
    }

    public Optional<Connection> getConnection(Interface sourceInterface) {
        Objects.requireNonNull(sourceInterface, "sourceInterface cannot be null");
        for (Connection connection : connections) {
            if (connection.containsInterface(sourceInterface)) {
                return Optional.of(connection);
            }
        }
        return Optional.empty();
    }
}
