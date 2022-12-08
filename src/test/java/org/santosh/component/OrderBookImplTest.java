package org.santosh.component;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.santosh.model.Order;

import java.util.List;

import static org.santosh.component.OrderBookImpl.BID;
import static org.santosh.component.OrderBookImpl.OFFER;

public class OrderBookImplTest {


    private OrderBook orderBook;

    @Before
    public void setUp() {
        orderBook = new OrderBookImpl();
    }

    @Test
    public void addOrder() {
        orderBook.addOrder(new Order(1, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(2, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(3, 3.5d, BID, 100L));
        orderBook.addOrder(new Order(4, 3.5d, OFFER, 100L));
        orderBook.addOrder(new Order(5, 1.5d, OFFER, 100L));
        Assert.assertEquals(2.5, orderBook.getPrice(BID, 2), 0);
        Assert.assertEquals(3.5, orderBook.getPrice(BID, 1), 0);
    }

    @Test
    public void removeOrder() {
        orderBook.addOrder(new Order(1, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(2, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(3, 3.5d, BID, 100L));
        orderBook.addOrder(new Order(4, 4.5d, BID, 100L));
        orderBook.removeOrder(3L);
        Assert.assertEquals(2.5, orderBook.getPrice(BID, 2), 0);
    }

    @Test
    public void updateOrder() {
        orderBook.addOrder(new Order(3, 3.5d, BID, 100L));
        orderBook.addOrder(new Order(1, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(2, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(5, 1.5d, BID, 100L));
        orderBook.updateOrder(3L, 150L);
        Assert.assertEquals(Long.valueOf(150), orderBook.getTotalOrders(BID, 1));
    }

    @Test
    public void getPrice() {
        orderBook.addOrder(new Order(1, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(2, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(3, 3.5d, BID, 100L));
        orderBook.addOrder(new Order(4, 3.5d, OFFER, 100L));
        orderBook.addOrder(new Order(5, 1.5d, OFFER, 100L));
        Assert.assertEquals(2.5, orderBook.getPrice(BID, 2), 0);
        Assert.assertEquals(1.5, orderBook.getPrice(OFFER, 1), 0);
    }

    @Test
    public void getTotalOrders() {
        orderBook.addOrder(new Order(1, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(2, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(3, 3.5d, BID, 102L));
        orderBook.addOrder(new Order(4, 3.5d, OFFER, 100L));
        orderBook.addOrder(new Order(5, 1.5d, OFFER, 100L));
        Assert.assertEquals(Long.valueOf(102), orderBook.getTotalOrders(BID, 1));
        Assert.assertEquals(Long.valueOf(200), orderBook.getTotalOrders(BID, 2));
    }

    @Test
    public void getAllOrdersBySide() {
        orderBook.addOrder(new Order(1, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(2, 2.5d, BID, 100L));
        orderBook.addOrder(new Order(3, 3.5d, BID, 102L));
        orderBook.addOrder(new Order(4, 3.5d, OFFER, 100L));
        orderBook.addOrder(new Order(5, 1.5d, OFFER, 100L));
        List<Order> allOrdersBySide = orderBook.getAllOrdersBySide(BID);
        Assert.assertEquals(3, allOrdersBySide.size());
        Assert.assertEquals(3, allOrdersBySide.get(0).getId());
        Assert.assertEquals(1, allOrdersBySide.get(1).getId());
        Assert.assertEquals(2, allOrdersBySide.get(2).getId());

    }
}