package org.santosh.component;

import org.santosh.model.BOrder;
import org.santosh.model.OrderBucket;
import org.santosh.model.OrderSide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class OrderBookBucketImpl implements OrderBook<BOrder> {


    private final ConcurrentSkipListMap<Double, OrderBucket> bidMap;
    private final ConcurrentSkipListMap<Double, OrderBucket> offerMap;
    private final ConcurrentMap<Long, BOrder> orderMap;

    public OrderBookBucketImpl() {
        this.offerMap = new ConcurrentSkipListMap<>();
        this.bidMap = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
        this.orderMap = new ConcurrentHashMap<>();
    }

    /**
     * Add Order.
     *
     * @param order order.
     */
    @Override
    public void addOrder(final BOrder order) {
        switch (order.getSide()) {
            case BID:
                bidMap.computeIfAbsent(order.getPrice(), OrderBucket::new).addOrder(order);
                orderMap.put(order.getId(), order);
                break;
            case OFFER:
                offerMap.computeIfAbsent(order.getPrice(), OrderBucket::new).addOrder(order);
                orderMap.put(order.getId(), order);
                break;
            default:
                throw new RuntimeException("Invalid Order type specified:" + order);
        }
    }

    /**
     * Remove Order.
     *
     * @param orderId orderId.
     */
    @Override
    public void removeOrder(Long orderId) {
        final BOrder order = orderMap.get(orderId);
        switch (order.getSide()) {
            case BID:
                removeOrder(orderId, order, bidMap);
                break;
            case OFFER:
                removeOrder(orderId, order, offerMap);
                break;
            default:
                throw new RuntimeException("Invalid Order type specified:" + order);
        }
    }

    private void removeOrder(final Long orderId, final BOrder order, final ConcurrentSkipListMap<Double, OrderBucket> sideMap) {
        final OrderBucket orderBucket = sideMap.get(order.getPrice());
        orderBucket.removeOrder(orderId);
        if (orderBucket.isZeroValue()) {
            sideMap.remove(order.getPrice());
        }
        orderMap.remove(orderId);
    }

    /**
     * Update order.
     *
     * @param orderId order Id.
     * @param newSize newSize for the order.
     */
    @Override
    public void updateOrder(Long orderId, Long newSize) {
        BOrder order = orderMap.get(orderId);
        switch (order.getSide()) {
            case BID:
                final BOrder updatedBidOrder = bidMap.get(order.getPrice()).updateOrder(orderId, newSize);
                orderMap.replace(orderId, updatedBidOrder);
                break;
            case OFFER:
                final BOrder updatedOfferOrder = offerMap.get(order.getPrice()).updateOrder(orderId, newSize);
                orderMap.replace(orderId, updatedOfferOrder);
                break;
            default:
                throw new RuntimeException("Invalid Order type specified:" + order);
        }
    }

    /**
     * Get Price.
     *
     * @param side  side.
     * @param level level of price.
     * @return price for side & level.
     */
    @Override
    public Double getPrice(char side, int level) {
        final OrderSide orderSide = OrderSide.getOrderSide(side);
        int index = level - 1;
        switch (orderSide) {
            case BID:
                return (Double) bidMap.keySet().toArray()[index];
            case OFFER:
                return (Double) offerMap.keySet().toArray()[index];
            default:
                throw new RuntimeException("Invalid combination of side " + side + " level" + level);
        }
    }

    /**
     * Get Total orders for side & level.
     *
     * @param side  side.
     * @param level level.
     * @return total orders for side & level.
     */
    @Override
    public Long getTotalOrders(char side, int level) {
        final OrderSide orderSide = OrderSide.getOrderSide(side);
        int index = level - 1;
        switch (orderSide) {
            case BID:
                return new ArrayList<>(bidMap.entrySet()).get(index).getValue().getTotal();
            case OFFER:
                return new ArrayList<>(offerMap.entrySet()).get(index).getValue().getTotal();
            default:
                throw new RuntimeException("Invalid combination of side " + side + " level" + level);
        }
    }

    /**
     * Get All Orders by Side.
     *
     * @param side side.
     * @return Get all orders from the side of the book in level & time order.
     */
    @Override
    public List<BOrder> getAllOrdersBySide(char side) {
        final OrderSide orderSide = OrderSide.getOrderSide(side);
        switch (orderSide) {
            case BID:
                return bidMap.values().stream().map(OrderBucket::getOrders).flatMap(Collection::stream).collect(Collectors.toList());
            case OFFER:
                return offerMap.values().stream().map(OrderBucket::getOrders).flatMap(Collection::stream).collect(Collectors.toList());
            default:
                throw new RuntimeException("Invalid side" + side);
        }
    }
}
