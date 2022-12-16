package org.santosh.model;

public enum OrderSide {
    BID, OFFER;

    public static OrderSide getOrderSide(final char achar) {
        switch (achar) {
            case 'B':
                return BID;
            case 'O':
                return OFFER;
            default:
                throw new RuntimeException("Invalid Side character passed " + achar);
        }
    }

}
