package com.pl.masterthesis.utils;

import com.pl.masterthesis.ui.WorkingSpacePanel;

public class SaveLoadController {
    private static final String SPACE = " ";
    private static final String ENDLINE = "\n";
    private WorkingSpacePanel workingSpacePanel;

    public SaveLoadController(WorkingSpacePanel workingSpacePanel) {
        this.workingSpacePanel = workingSpacePanel;
    }

    /*
    FILE FORMAT:
    SendReceiveDeviceType   xCoordinate     yCoordinate     identifier
    Connection              startDeviceId   endDeviceId     startInterfaceIP    endInterfaceIP  netIP
     */

    public void saveCurrentView(String directoryPath) {

    }

    public void loadFileToCurrentWorkspace() {

    }

}
