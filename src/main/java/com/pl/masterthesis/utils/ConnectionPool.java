package com.pl.masterthesis.utils;

import com.pl.masterthesis.models.Connection;
import com.pl.masterthesis.models.prototypes.Transmittable;

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

    public Optional<Connection> getConnection(Transmittable sourceTransmittable) {
        Objects.requireNonNull(sourceTransmittable, "sourceTransmittable cannot be null");
        for (Connection connection : connections) {
            if (connactionContainsTransmittable(connection, sourceTransmittable)) {
                return Optional.of(connection);
            }
        }
        return Optional.empty();
    }

    private boolean connactionContainsTransmittable(Connection connection, Transmittable sourceTransmittable) {
        Objects.requireNonNull(sourceTransmittable, "sourceTransmittable cannot be null");
        return sourceTransmittable.equals(connection.getFirstTransmittable()) || sourceTransmittable.equals(connection.getSecondTransmittable());
    }
}
