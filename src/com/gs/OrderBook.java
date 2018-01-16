package com.gs;

import java.util.*;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rkasthuri on 1/11/18.
 */
public class OrderBook {

    public static final int LEVELS = 10;
    /**
     * The bid and ask attribute maintains level to Order mapping for buys and asks
     * Map is used for update actions and look up
     * ConcurrentHashMap is used to avoid Concurrent Modification Exception
     */
    private Map<Integer, Order> bidLevels;
    private Map<Integer, Order> askLevels;

    public OrderBook() {
        bidLevels = new ConcurrentHashMap<Integer, Order>(LEVELS);
        askLevels = new ConcurrentHashMap<Integer, Order>(LEVELS);

    }

    public Map<Integer, Order> getBid() {
        return bidLevels;
    }

    public Order getBid(int i) {
        return bidLevels.get(i);
    }

    public void setBid(Map<Integer, Order> bid) {
        this.bidLevels = bid;
    }

    public Map<Integer, Order> getAsk() {
        return askLevels;
    }

    public Order getAsk(int i) {
        return askLevels.get(i);
    }

    public void setAsk(Map<Integer, Order> ask) {
        this.askLevels = ask;
    }

    /**
     * Updates the OrderBook with the orders received in the packet
     * The action mention in the feed is used to update
     * @param order Order in the packet
     * @param seqNo Sequence number of the packet
     */
    public void updateOrderBook(Order order, String seqNo) {
        try {
            int level = order.getLevelNo();
            String side = order.getSide();
            char action = order.getAction().charAt(0);
            switch (action) {
                case 'U':
                    if (side.equals("B")) {
                        updateOrder(bidLevels, level, order);
                    } else {
                        updateOrder(askLevels, level, order);
                    }
                    break;
                case 'N':
                    if (side.equals("B")) {
                        insertOrder(bidLevels, level, order);
                    } else {
                        insertOrder(askLevels, level, order);
                    }
                    break;
                case 'D':
                    if (side.equals("B")) {
                        deleteOrder(bidLevels, level);
                    } else {
                        deleteOrder(askLevels, level);
                    }
                    break;
                default:
                    System.out.println("Error processing order for the sequence " + seqNo);
                    System.out.println("Error processing order for the order " + order.toString());
            }
        } catch (Exception ex) {
            System.out.println("Error processing order for the sequence " + seqNo + " " + ex);

        }
    }

    /**
     * Updates an Order if it exists else creates a new order for the corresponding level
     * @param levels - Map that maintains the Orders by the level
     * @param level - Current Level to update
     * @param order Order to be updated
     */
    public void updateOrder(Map<Integer, Order> levels, int level, Order order) {
        if (levels.containsKey(level) && levels.get(level) != null) {
            Order current = levels.get(level);
            current.setSize(order.getSize());
            current.setPrice(order.getPrice());
        }else {
            insertOrder(levels, level, order);
        }
    }

    /**
     * Adds an order at the given level pushing existing other orders down
     * by one level
     * @param levels
     * @param level
     * @param order
     */
    public void insertOrder(Map<Integer, Order> levels, int level, Order order) {
        int i;
        for (i = LEVELS-2; i>= level; i--) {
            Order o = levels.get(i);
            if (o != null) {
                int ordLevel = o.getLevelNo()+1;
                o.setLevelNo(ordLevel);
                levels.put(i+1, o);
            }else {
                levels.remove(i+1);
            }

        }
        levels.put(level, order);
    }

    /**
     * Deletes an order at the give level.
     * Pushes the order below current level up by one
     * @param levels
     * @param level
     */
    public void deleteOrder(Map<Integer, Order> levels, int level) {
        levels.remove(level);
        /*Iterator<Map.Entry<Integer, Order>> it = levels.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Order> entry = it.next();
            if (entry.getKey() > level) {
                Order o = entry.getValue();
                int levelNo = entry.getKey() - 1;
                o.setLevelNo(levelNo);
                levels.put(levelNo, o);
                it.remove();
            }
        }*/
        for (int i =level+1; i < LEVELS; i++) {
            Order o = levels.get(i);
            if (o != null) {
                int ordLevel = o.getLevelNo()-1;
                o.setLevelNo(ordLevel);
                levels.put(i-1, o);
            }else {
                levels.remove(i-1);
            }
        }
    }

    /**
     * It is used validate if the orderbook is complete
     * after processing a packet. Returns true if the bid and ask levels
     * are complete and are in the correct order
     * @return orderBook completion
     */

    public boolean isBookComplete() {
        boolean complete = (askLevels.size() == LEVELS ? (bidLevels.size() == LEVELS ? true : false) :false);
        if (complete) {
            Double bidPrev = bidLevels.get(0).getPrice();
            Double askPrev = askLevels.get(0).getPrice();
            for (int i=1; i < LEVELS; i++) {
                double bidPrice = bidLevels.get(i).getPrice();
                double askPrice = askLevels.get(i).getPrice();
                if (bidPrev >= bidPrice && askPrev <= askPrice) {
                    bidPrev = bidPrice;
                    askPrev = askPrice;
                } else {
                    complete = false;
                    break;
                }
            }
        }
        return complete;
    }

}
