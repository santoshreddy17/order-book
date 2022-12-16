package org.santosh.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class OrderBucket implements Comparable<OrderBucket> {

    private final double price;
    private final ConcurrentMap<Long, BOrder> bucketMap;
    private long total;

    public OrderBucket(final double price) {
        this.price = price;
        this.bucketMap = new ConcurrentSkipListMap<>();
    }

    public double getPrice() {
        return price;
    }

    public long getTotal() {
        return total;
    }


    public void addOrder(final BOrder order) {
        bucketMap.put(order.getId(), order);
        total += order.getSize();
    }

    public void removeOrder(final Long orderId) {
        final BOrder order = bucketMap.get(orderId);
        bucketMap.remove(order.getId());
        total -= order.getSize();
    }

    public boolean isZeroValue() {
        return 0 == total;
    }

    public BOrder updateOrder(final Long orderId,
                              final Long newSize) {
        final BOrder oldOrder = bucketMap.get(orderId);
        final BOrder newOrder = new BOrder(orderId, oldOrder.getPrice(), oldOrder.getSide(), newSize);
        bucketMap.put(orderId, newOrder);
        total = total - oldOrder.getSize() + newSize;
        return newOrder;
    }

    public List<BOrder> getOrders() {
        return new ArrayList<>(bucketMap.values());
    }

    @Override
    public int compareTo(OrderBucket o) {
        return Double.compare(this.getPrice(), o.getPrice());
    }
}
