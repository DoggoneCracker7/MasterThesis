package com.pl.masterthesis.utils;

import com.pl.masterthesis.models.SendReceiveDevice;

import java.util.Objects;

public class PackageSendNode {
    private static SendReceiveDevice BUFFERED_NODE;

    public static SendReceiveDevice getBufferedNode() {
        return BUFFERED_NODE;
    }

    public static void setBufferedNode(SendReceiveDevice bufferedNode) {
        BUFFERED_NODE = bufferedNode;
    }

    public static boolean isEmpty() {
        return Objects.isNull(BUFFERED_NODE);
    }

    public static void clear() {
        BUFFERED_NODE = null;
    }
}
