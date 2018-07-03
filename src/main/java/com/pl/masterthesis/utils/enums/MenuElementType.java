package com.pl.masterthesis.utils.enums;

import com.pl.masterthesis.models.Connection;
import com.pl.masterthesis.models.EndDevice;
import com.pl.masterthesis.models.Router;
import com.pl.masterthesis.utils.Constants;

public enum MenuElementType {
    ROUTER("ROUTER", Constants.ROUTER_IMAGE_URL, Constants.ENLARGED_ROUTER_IMAGE_URL, Router.class),
    END_DEVICE("END_DEVICE", Constants.END_DEVICE_IMAGE_URL, Constants.ENLARGED_END_DEVICE_IMAGE_URL, EndDevice.class),
    CABLE("CABLE", Constants.CABLE_IMAGE_URL, Connection.class),
    LEAVE("LEAVE", Constants.LEAVE_IMAGE_URL),
    SEND("SEND", Constants.ENVELOPE_100x100_IMAGE_URL);

    private String name;
    private String iconUrl;
    private String iconToCircleUrl;
    private Class model;

    MenuElementType(String name, String iconUrl) {
        this(name, iconUrl, null, null);
    }

    MenuElementType(String name, String iconUrl, Class model) {
        this(name, iconUrl, null, model);
    }

    MenuElementType(String name, String iconUrl, String iconToCircleUrl, Class model) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.iconToCircleUrl = iconToCircleUrl;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getIconToCircleUrl() {
        return iconToCircleUrl;
    }

    public Class getModel() {
        return model;
    }
}
