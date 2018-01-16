package com.gs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkasthuri on 1/11/18.
 */
public class Packet {
    private String seqNo;
    private List<String[]> updateFeed;
    private List<Order> orders;
    /**
     * Create a Packet of update feeds grouped by seqNo
     * @param seqNo of the Packet
     */

    public Packet(String seqNo) {
        this.seqNo = seqNo;
        updateFeed = new ArrayList<String[]>();
        orders = new ArrayList<Order>();
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public List<String[]> getUpdateFeed() {
        return updateFeed;
    }

    public void setUpdateFeed(List<String[]> updateFeed) {
        this.updateFeed = updateFeed;
    }

    /**
     * add a feed with same seqNo to the packet
     * @param feed new feed from the packet
     */
    public void addFeed(String[] feed) {
        this.updateFeed.add(feed);
    }

    /**
     * Converts the update feed to a list of orders
     * @return List<Order>
     */
    public List<Order> getOrders() {
        if (orders == null || orders.size() == 0) {
            orders = new ArrayList<Order>();
            for (String[] feed : updateFeed) {
                Order order = new Order(feed);
                orders.add(order);
            }
        }
        return orders;
    }
}
