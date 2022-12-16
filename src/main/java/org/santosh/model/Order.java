package org.santosh.model;

import java.util.Objects;


public class Order {

    public static final char BID = 'B';
    public static final char OFFER = 'O';
    private final long id;
    private final double price;
    private final char side;
    private final long size;

    public Order(long id, double price, char side, long size) {
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public char getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }

    public boolean isBidOrder() {
        return BID == this.getSide();
    }

    public boolean isOfferOrder() {
        return OFFER == this.getSide();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && side == order.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, side);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", side=" + side +
                ", size=" + size +
                '}';
    }

}
