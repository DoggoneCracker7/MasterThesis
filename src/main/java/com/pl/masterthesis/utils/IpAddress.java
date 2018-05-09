package com.pl.masterthesis.utils;

import java.util.Arrays;
import java.util.StringJoiner;

public final class IpAddress {
    private static final String VALIDATION_ERROR = "Adres %s nie może zostać poprawnie przekonwertowany.";
    private short[] address;
    private short mask;
    private boolean addressPool;

    public IpAddress(String addressAsText) {
        String validationMsg = validateGivenAddress(addressAsText);
        if (validationMsg.isEmpty()) {
            String[] splittedAddress = addressAsText.split("\\.");
            this.address = new short[]{Short.valueOf(splittedAddress[0]),
                    Short.valueOf(splittedAddress[1]),
                    Short.valueOf(splittedAddress[2]),
                    Short.valueOf(splittedAddress[3])};
        } else {
            throw new IllegalArgumentException(validationMsg);
        }
    }

    public IpAddress(short[] address) {
        String validationMsg = validateGivenAddress(address);
        if (validationMsg.isEmpty()) {
            this.address = new short[]{address[0], address[1], address[2], address[3]};
        } else {
            throw new IllegalArgumentException(validationMsg);
        }
    }

    public short[] getAddress() {
        return address;
    }

    public void setAddress(short[] address) {
        this.address = address;
    }

    public short getMask() {
        return mask;
    }

    public void setMask(short mask) {
        this.mask = mask;
    }

    public boolean isAddressPool() {
        return addressPool;
    }

    public void setAddressPool(boolean addressPool) {
        this.addressPool = addressPool;
    }

    public String getAddressAsString() {
        return getAddressAsString(address);
    }

    private String validateGivenAddress(String addressAsText) {
        String[] splittedAddress = addressAsText.split("\\.");
        if (splittedAddress.length > 4) {
            return String.format(VALIDATION_ERROR, addressAsText);
        }
        return validateGivenAddress(new short[]{Short.valueOf(splittedAddress[0]),
                Short.valueOf(splittedAddress[1]),
                Short.valueOf(splittedAddress[2]),
                Short.valueOf(splittedAddress[3])});
    }

    private String validateGivenAddress(short[] address) {
        if (address.length > 4) {
            return String.format(VALIDATION_ERROR, getAddressAsString(address));
        }
        for (short part : address) {
            if (part > 255 || part < 0) {
                return String.format(VALIDATION_ERROR, getAddressAsString(address));
            }
        }

        return "";
    }

    private String getAddressAsString(short[] address) {
        StringJoiner joiner = new StringJoiner(".");

        joiner.add(String.valueOf(address[0]));
        joiner.add(String.valueOf(address[1]));
        joiner.add(String.valueOf(address[2]));
        joiner.add(String.valueOf(address[3]));

        return joiner.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpAddress ipAddress = (IpAddress) o;

        return mask == ipAddress.mask && Arrays.equals(address, ipAddress.address);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(address);
        result = 31 * result + (int) mask;
        return result;
    }
}
