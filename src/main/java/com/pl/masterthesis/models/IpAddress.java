package com.pl.masterthesis.models;

import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;

import java.util.StringJoiner;

public final class IpAddress {
    private static final String IP_ADDRESS_VALIDATION_ERROR = "Adres %s nie może zostać poprawnie przekonwertowany.";
    private static final String MASK_VALIDATION_ERROR = "Maska musi być z przedziału 0-32 natomiat podana wartość wynosi %s";
    private long address;
    private int mask;
    private boolean addressPool;

    public IpAddress(String addressAsText) throws WrongIpAddressFormatException {
        this(addressAsText, 0);
    }

    public IpAddress(int[] address) throws WrongIpAddressFormatException {
        this(address, 0);
    }

    public IpAddress(String addressAsText, int mask) throws WrongIpAddressFormatException {
        String validationMsg = validateGivenAddress(addressAsText, mask);
        if (validationMsg.isEmpty()) {
            String[] splittedAddress = addressAsText.split("\\.");
            this.address = Long.valueOf(splittedAddress[0]) << 24
                    | Long.valueOf(splittedAddress[1]) << 16
                    | Long.valueOf(splittedAddress[2]) << 8
                    | Long.valueOf(splittedAddress[3]);
            this.mask = mask;
        } else {
            throw new WrongIpAddressFormatException(validationMsg);
        }
    }

    public IpAddress(int[] address, int mask) throws WrongIpAddressFormatException {
        String validationMsg = validateGivenAddress(address, mask);
        if (validationMsg.isEmpty()) {
            this.address = (long) address[0] << 24 | (long) address[1] << 16 | (long) address[2] << 8 | address[3];
            this.mask = mask;
        } else {
            throw new WrongIpAddressFormatException(validationMsg);
        }
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
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

    private String validateGivenAddress(String addressAsText, int mask) {
        String[] splittedAddress = addressAsText.split("\\.");
        if (splittedAddress.length > 4) {
            return String.format(IP_ADDRESS_VALIDATION_ERROR, addressAsText);
        }
        return validateGivenAddress(new int[]{Integer.valueOf(splittedAddress[0]),
                        Integer.valueOf(splittedAddress[1]),
                        Integer.valueOf(splittedAddress[2]),
                        Integer.valueOf(splittedAddress[3])}
                , mask);
    }

    private String validateGivenAddress(int[] address, int mask) {
        if (address.length > 4) {
            return String.format(IP_ADDRESS_VALIDATION_ERROR, getAddressAsString(address));
        }
        if (mask < 0 || mask > 32) {
            return String.format(MASK_VALIDATION_ERROR, mask);
        }
        for (long part : address) {
            if (part > 255 || part < 0) {
                return String.format(IP_ADDRESS_VALIDATION_ERROR, getAddressAsString(address));
            }
        }

        return "";
    }

    private String getAddressAsString(long address) {
        return getAddressAsString(new int[]{getOctet(address, 1),
                getOctet(address, 2),
                getOctet(address, 3),
                getOctet(address, 4)});
    }

    private String getAddressAsString(int[] address) {
        StringJoiner joiner = new StringJoiner(".");

        joiner.add(String.valueOf(address[0]));
        joiner.add(String.valueOf(address[1]));
        joiner.add(String.valueOf(address[2]));
        joiner.add(String.valueOf(address[3]));

        return joiner.toString();
    }

    public int getOctet(long address, int octetNumber) {
        if (octetNumber < 1 || octetNumber > 4) {
            return -1;
        }
        int rotateRight = 8 * (4 - octetNumber);
        int mask = 255 << rotateRight;
        long result = (address & mask) >> rotateRight;

        return (int) result;
    }

    public boolean containsAddress(IpAddress addressToCheck) {
        return addressToCheck.getAddress() >= address
                && addressToCheck.getAddress() < address + getAddressAmount(mask);
    }

    private long getAddressAmount(int mask) {
        int value = 1;
        int addressAmount = 0;

        for (int i = 0; i < 32 - mask; i++) {
            addressAmount += value;
            value *= 2;
        }

        return addressAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IpAddress ipAddress = (IpAddress) o;

        if (address != ipAddress.address) return false;
        return mask == ipAddress.mask;
    }
}
