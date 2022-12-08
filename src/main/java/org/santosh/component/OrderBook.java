package org.santosh.component;

import org.santosh.model.Order;

import java.util.List;

/**
 * Order Book Interface.
 * A limit order book stores customer orders on a price time priority basis. The highest bid and lowest oer
 * are considered "best" with all other orders stacked in price levels behind.
 */
public interface OrderBook {

     /**
      * Add Order.
      *
      * @param order order.
      */
     void addOrder(Order order);

     /**
      * Remove Order.
      *
      * @param orderId orderId.
      */
     void removeOrder(Long orderId);

     /**
      * Update order.
      *
      * @param orderId order Id.
      * @param newSize newSize for the order.
      */
     void updateOrder(Long orderId, Long newSize);

     /**
      * Get Price.
      *
      * @param side  side.
      * @param level level of price.
      * @return price for side & level.
      */
     Double getPrice(char side, int level);

     /**
      * Get Total orders for side & level.
      *
      * @param side  side.
      * @param level level.
      * @return total orders for side & level.
      */
     Long getTotalOrders(char side, int level);


     /**
      * Get All Orders by Side.
      *
      * @param side side.
      * @return Get all orders from the side of the book in level & time order.
      */
     List<Order> getAllOrdersBySide(char side);


}
