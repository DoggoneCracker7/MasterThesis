package com.pl.masterthesis.utils;

import javafx.scene.shape.Circle;

import java.util.Objects;

public class ConnectionBuffer {
    private static Circle BUFFERED_NODE;

    public static Circle getBufferedNode() {
        return BUFFERED_NODE;
    }

    public static void setBufferedNode(Circle bufferedNode) {
        BUFFERED_NODE = bufferedNode;
    }

    public static boolean isEmpty() {
        return Objects.isNull(BUFFERED_NODE);
    }

    public static void clear() {
        BUFFERED_NODE = null;
    }
}
