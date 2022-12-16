package org.santosh.component;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.santosh.model.BOrder;
import org.santosh.model.Order;

import java.util.List;

import static org.santosh.model.OrderSide.BID;
import static org.santosh.model.OrderSide.OFFER;


public class OrderBookBucketImplTest {


    private OrderBook<BOrder> orderBook;

    @Before
    public void setUp() {
        orderBook = new OrderBookBucketImpl();
    }

    @Test
    public void addOrder() {
        orderBook.addOrder(new BOrder(1, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(2, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(3, 3.5d, BID, 100L));
        orderBook.addOrder(new BOrder(4, 3.5d, OFFER, 100L));
        orderBook.addOrder(new BOrder(5, 1.5d, OFFER, 100L));
        Assert.assertEquals(2.5, orderBook.getPrice(Order.BID, 2), 0);
        Assert.assertEquals(3.5, orderBook.getPrice(Order.BID, 1), 0);
    }

    @Test
    public void removeOrder() {
        orderBook.addOrder(new BOrder(1, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(2, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(3, 3.5d, BID, 100L));
        orderBook.addOrder(new BOrder(4, 4.5d, BID, 100L));
        orderBook.removeOrder(3L);
        Assert.assertEquals(2.5, orderBook.getPrice(Order.BID, 2), 0);
    }

    @Test
    public void updateOrder() {
        orderBook.addOrder(new BOrder(3, 3.5d, BID, 100L));
        orderBook.addOrder(new BOrder(1, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(2, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(5, 1.5d, BID, 100L));
        orderBook.updateOrder(3L, 150L);
        Assert.assertEquals(Long.valueOf(150), orderBook.getTotalOrders(Order.BID, 1));
    }

    @Test
    public void getPrice() {
        orderBook.addOrder(new BOrder(1, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(2, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(3, 3.5d, BID, 100L));
        orderBook.addOrder(new BOrder(4, 3.5d, OFFER, 100L));
        orderBook.addOrder(new BOrder(5, 1.5d, OFFER, 100L));
        Assert.assertEquals(2.5, orderBook.getPrice(Order.BID, 2), 0);
        Assert.assertEquals(1.5, orderBook.getPrice(Order.OFFER, 1), 0);
    }

    @Test
    public void getTotalOrders() {
        orderBook.addOrder(new BOrder(1, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(2, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(3, 3.5d, BID, 102L));
        orderBook.addOrder(new BOrder(4, 3.5d, OFFER, 100L));
        orderBook.addOrder(new BOrder(5, 1.5d, OFFER, 100L));
        Assert.assertEquals(Long.valueOf(102), orderBook.getTotalOrders(Order.BID, 1));
        Assert.assertEquals(Long.valueOf(200), orderBook.getTotalOrders(Order.BID, 2));
    }

    @Test
    public void getAllOrdersBySide() {
        orderBook.addOrder(new BOrder(1, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(2, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(7, 2.5d, BID, 100L));
        orderBook.addOrder(new BOrder(3, 3.5d, BID, 102L));
        orderBook.addOrder(new BOrder(4, 3.5d, OFFER, 100L));
        orderBook.addOrder(new BOrder(5, 1.5d, OFFER, 100L));
        List<BOrder> allOrdersBySide = orderBook.getAllOrdersBySide(Order.BID);
        Assert.assertEquals(4, allOrdersBySide.size());
        Assert.assertEquals(3, allOrdersBySide.get(0).getId());
        Assert.assertEquals(1, allOrdersBySide.get(1).getId());
        Assert.assertEquals(2, allOrdersBySide.get(2).getId());
        Assert.assertEquals(7, allOrdersBySide.get(3).getId());

    }
}