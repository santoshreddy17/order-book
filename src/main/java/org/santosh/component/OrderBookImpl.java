package org.santosh.component;

import org.santosh.model.Order;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class OrderBook {

    private BlockingQueue<Order> bidQueue;
    private BlockingQueue<Order> offerQueue;    

    public OrderBook() {
        bidQueue = new PriorityBlockingQueue<>(0, Comparator.comparing(Order::getPrice).reversed());
        offerQueue = new PriorityBlockingQueue<>(0, Comparator.comparing(Order::getPrice));
    }

    public void addOrder(final Order order) {
        if ('B' == order.getSide()) {
            addBidOrder(order);
        } else if ('O' == order.getSide()) {
            addOfferOrder(order);
        } else {
            throw new RuntimeException("Invalid Order type specified:" + order);
        }
    }

    private void addBidOrder(final Order order) {
        bidQueue.add(order);
    }

    private void addOfferOrder(final Order order) {
        offerQueue.add(order);
    }


}
