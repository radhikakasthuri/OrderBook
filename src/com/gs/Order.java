package com.gs;

/**
 * Created by rkasthuri on 1/11/18.
 */
public class Order {

    private String action;
    private int levelNo;
    private String side;
    private double price;
    private int size;

    /** Creates an Order from the update feed received
     * @param updates Update Feed
     */
    public Order(String[] updates) {
        this.action = updates[1];
        this.levelNo = Integer.parseInt(updates[2]);
        this.side = updates[3];
        this.price = Double.parseDouble(updates[4]);
        this.size = Integer.parseInt(updates[5]);
    }
    public Order(int levelNo, double price, int size, String side) {
        this.levelNo = levelNo;
        this.price = price;
        this.side = side;
        this.size = size;
        this.action ="U";
    }

    public Order(String action, int levelNo, String side, double price, int size) {
        this.levelNo = levelNo;
        this.price = price;
        this.side = side;
        this.size = size;
        this.action = action;
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public String getSide() {
        return side;
    }

    @Override
    public String toString() {
        if (getAction() != null ) {
            String side = getSide().trim().equals("B") ? "bid" : "ask";
            return new StringBuilder(side).append(getLevelNo()).append("\t").append(String.format( "%.3f", getPrice())).append(",\t").append(side).append(getLevelNo()).append("\t").append(String.format( "%03d", getSize())).toString();
        }
        return "";
    }

    public void setSide(String side) {
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
