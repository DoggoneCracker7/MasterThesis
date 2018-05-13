package com.pl.masterthesis.models.prototypes;

import com.pl.masterthesis.models.Package;

public interface Transmittable {
    void receivePackage(Package receivedPackage);

    void sendPackage(Package packageToSend);
}
