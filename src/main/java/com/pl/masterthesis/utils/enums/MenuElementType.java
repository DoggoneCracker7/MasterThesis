package com.pl.masterthesis.utils.enums;

import com.pl.masterthesis.utils.Constants;

public enum MenuElementType {
    ROUTER("ROUTER", Constants.ROUTER_IMAGE_URL, Constants.ENLARGED_ROUTER_IMAGE_URL),
    END_DEVICE("END_DEVICE", Constants.END_DEVICE_IMAGE_URL, Constants.ENLARGED_END_DEVICE_IMAGE_URL),
    CABLE("CABLE", Constants.CABLE_IMAGE_URL),
    DELETE("DELETE", Constants.DELETE_IMAGE_URL),
    LEAVE("LEAVE", Constants.LEAVE_IMAGE_URL);

    private String name;
    private String iconUrl;
    private String iconToCircleUrl;

    MenuElementType(String name, String iconUrl) {
        this(name, iconUrl, null);
    }

    MenuElementType(String name, String iconUrl, String iconToCircleUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
        this.iconToCircleUrl = iconToCircleUrl;
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
}
