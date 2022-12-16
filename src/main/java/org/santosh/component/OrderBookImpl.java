package org.santosh.component;

import org.santosh.model.Order;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

import static org.santosh.model.Order.BID;
import static org.santosh.model.Order.OFFER;

/**
 * Order Book Implementation.
 * A limit order book stores customer orders on a price time priority basis. The highest bid and lowest oer
 * are considered "best" with all other orders stacked in price levels behind.
 */
public class OrderBookImpl implements OrderBook {



    private final ConcurrentSkipListMap<Double, Set<Order>> bidMap;
    private final ConcurrentSkipListMap<Double, Set<Order>> offerMap;
    private final ConcurrentMap<Long, Order> orderMap;

    public OrderBookImpl() {
        this.offerMap = new ConcurrentSkipListMap<>();
        this.bidMap = new ConcurrentSkipListMap<>(Comparator.reverseOrder());
        this.orderMap = new ConcurrentHashMap<>();
    }

    /**
     * Add Order to appropriate side.
     *
     * @param order order
     */
    public void addOrder(final Order order) {
        if (order.isBidOrder()) {
            addBidOrder(order);
        } else if (order.isOfferOrder()) {
            addOfferOrder(order);
        } else {
            throw new RuntimeException("Invalid Order type specified:" + order);
        }
    }

    /**
     * Remove Order from the appropriate side.
     *
     * @param orderId order id.
     */
    public void removeOrder(final Long orderId) {
        final Order order = orderMap.get(orderId);
        if (order.isBidOrder()) {
            removeOrder(bidMap, order, orderId);
        } else if (order.isOfferOrder()) {
            removeOrder(offerMap, order, orderId);
        } else {
            throw new RuntimeException("Order not found for order Id " + orderId);
        }

    }

    private void removeOrder(final ConcurrentSkipListMap<Double, Set<Order>> sideMap,
                             final Order order,
                             final Long orderId) {
        sideMap.get(order.getPrice()).remove(order);
        if (sideMap.get(order.getPrice()).isEmpty()) {
            sideMap.remove(order.getPrice());
            orderMap.remove(orderId);
        }
    }

    @Override
    public void updateOrder(final Long orderId,
                            final Long newSize) {
        final Order order = orderMap.get(orderId);
        final Order newOrder = new Order(orderId, order.getPrice(), order.getSide(), newSize);
        if (order.isBidOrder()) {
            bidMap.get(order.getPrice()).remove(order);
            bidMap.get(order.getPrice()).add(newOrder);
        } else if (order.isOfferOrder()) {
            offerMap.get(order.getPrice()).remove(order);
            offerMap.get(order.getPrice()).add(newOrder);
        }
        orderMap.put(orderId, newOrder);
    }

    @Override
    public Double getPrice(final char side, final int level) {
        int index = level - 1;
        switch (side) {
            case BID:
                return (Double) bidMap.keySet().toArray()[index];
            case OFFER:
                return (Double) offerMap.keySet().toArray()[index];
            default:
                throw new RuntimeException("Invalid combination of side " + side + " level" + level);
        }
    }

    @Override
    public Long getTotalOrders(final char side, final int level) {
        int index = level - 1;
        switch (side) {
            case BID:
                return new ArrayList<>(bidMap.entrySet()).get(index).getValue()
                        .parallelStream()
                        .map(Order::getSize)
                        .reduce(0L, Long::sum);
            case OFFER:
                return new ArrayList<>(offerMap.entrySet()).get(index).getValue()
                        .parallelStream()
                        .map(Order::getSize)
                        .reduce(0L, Long::sum);
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
    public List<Order> getAllOrdersBySide(final char side) {
        switch (side) {
            case BID:
                return bidMap.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            case OFFER:
                return offerMap.values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
            default:
                throw new RuntimeException("Invalid side" + side);
        }
    }

    private void addBidOrder(final Order order) {
        final Set<Order> bidListForPrice = bidMap.getOrDefault(order.getPrice(), Collections.synchronizedSet(new LinkedHashSet<>()));
        bidListForPrice.add(order);
        bidMap.put(order.getPrice(), bidListForPrice);
        orderMap.put(order.getId(), order);
    }

    private void addOfferOrder(final Order order) {
        final Set<Order> offerListForPrice = offerMap.getOrDefault(order.getPrice(), Collections.synchronizedSet(new LinkedHashSet<>()));
        offerListForPrice.add(order);
        offerMap.put(order.getPrice(), offerListForPrice);
        orderMap.put(order.getId(), order);
    }



}
