package com.pl.masterthesis.models;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EndDevice extends SendReceiveDevice {
    private final Logger logger = Logger.getLogger(getClass().getName());


    public EndDevice(String name) {
        this(null, name);
    }

    public EndDevice(Interface inoutInterface, String name) {
        if (inoutInterface != null) {
            addIoInterface(inoutInterface);
        }
        setName(name);
        setOnlyOneInterfaceAllowed(true);
    }

    public void setInoutInterface(Interface inoutInterface) {
        inoutInterface.setOnReceiveConsumer(this::onReceive);
        addIoInterface(inoutInterface);
        incrementInterfacesIdNumber();
    }

    @Override
    protected void onAckReceived(Package receivedPackage) {
        logger.log(Level.INFO, "Potwierdzenie pakietu o identyfikatorze {0} od {1} dotarło prawidłowo " +
                "do urządzenia końcowego {2} po adresem {3}", new String[]{receivedPackage.getPackageID(),
                receivedPackage.getSource().getAddressAsString(), getName(), receivedPackage.getDestination().getAddressAsString()});
    }

    @Override
    protected void onRoutingTableUpdateReceived(Package receivedPackage) {
        logger.log(Level.INFO, "Urządzenie końowe {0} otrzymało tablicę routingu od {1}. ZIGNOROWANO",
                new String[]{getName(), receivedPackage.getSource().getAddressAsString()});
    }

    @Override
    protected void onPackageReachedDestination(Package receivedPackage) {
        logger.log(Level.INFO, "Pakiet o identyfikatorze {0} dotarł do urządzenia końcowego {1}  CEL",
                new String[]{receivedPackage.getPackageID(), getName()});
        sendPackage(receivedPackage.changeToAck());
    }

    @Override
    public void sendPackage(Package receivedPackage) {
        logger.log(Level.INFO, "Pakiet o identyfikatorze {0} jest wysyłany z urządzenia końcowego {1}",
                new String[]{receivedPackage.getPackageID(), getName()});
        if (getIoInterfaces().size() == 1) {
            getIoInterfaces().get(0).sendPackage(receivedPackage, getName());
        } else {
            logger.log(Level.SEVERE, "Urządzenei końcowe {0} posiada złą konfigurację interfejsów.",
                    new String[]{getName()});
        }
    }
}
