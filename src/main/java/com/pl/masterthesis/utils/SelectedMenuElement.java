package com.pl.masterthesis.utils;

import com.pl.masterthesis.utils.enums.MenuElementType;

public class SelectedMenuElement {
    private static final SelectedMenuElement INSTANCE = new SelectedMenuElement();
    private MenuElementType currentlySellectedElement;

    public static SelectedMenuElement getInstance() {
        return INSTANCE;
    }

    public MenuElementType getCurrentlySellectedElement() {
        return currentlySellectedElement;
    }

    public void setCurrentlySellectedElement(MenuElementType currentlySellectedElement) {
        this.currentlySellectedElement = currentlySellectedElement;
    }
}
