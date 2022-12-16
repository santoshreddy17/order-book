package org.santosh.model;

import java.util.Objects;


public class BOrder {

    private final long id;
    private final double price;
    private final OrderSide side;
    private final long size;

    public BOrder(long id, double price, OrderSide side, long size) {
        this.id = id;
        this.price = price;
        this.side = side;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public OrderSide getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BOrder bOrder = (BOrder) o;
        return getId() == bOrder.getId() && Double.compare(bOrder.getPrice(), getPrice()) == 0 && getSize() == bOrder.getSize() && getSide() == bOrder.getSide();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSide());
    }

    @Override
    public String toString() {
        return "BOrder{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", side=" + side +
                ", size=" + size +
                '}';
    }
}
