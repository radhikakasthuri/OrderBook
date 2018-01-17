import com.gs.Order;
import com.gs.OrderBook;
import com.gs.OrderProcessing;
import com.gs.Packet;

import java.util.HashMap;

/**
 * Created by rkasthuri on 1/17/18.
 */
public class OrderProcessingTest {
    public static void main(String args[]) {

        HashMap<Integer, Order>  bids = new HashMap<Integer, Order>();
        bids.put(0, new Order(0, 132.922, 70, "B"));
        bids.put(1, new Order(1, 132.906, 11, "B"));
        bids.put(2, new Order(2, 132.891, 39, "B"));
        bids.put(3, new Order(3, 132.875, 26, "B"));
        bids.put(4, new Order(4, 132.828, 25, "B"));
        bids.put(6, new Order(6, 132.797, 25, "B"));
        bids.put(7, new Order(7, 132.781, 5, "B"));
        bids.put(8, new Order(8, 132.766, 25, "B"));
        bids.put(9, new Order(9, 132.750, 25, "B"));



        HashMap<Integer, Order>  asks = new HashMap<Integer, Order>();
        asks.put(0, new Order(0, 132.953, 1, "A"));
        asks.put(1, new Order(1, 132.969, 44, "A"));
        asks.put(2, new Order(2, 132.984, 59, "A"));
        asks.put(3, new Order(3, 133.000, 6, "A"));
        asks.put(4, new Order(4, 133.016, 6, "A"));
        asks.put(5, new Order(5, 133.031, 25, "A"));
        asks.put(6, new Order(6, 133.047, 25, "A"));
        asks.put(7, new Order(7, 133.062, 102, "A"));
        asks.put(8, new Order(8, 133.078, 5, "A"));
        asks.put(9, new Order(9, 133.094, 30, "A"));

        Packet p = new Packet("7959887");
        p.getOrders().add(new Order("U",0,"B",132.922,32));
        p.getOrders().add(new Order("U",2,"B",132.891,1));
        p.getOrders().add(new Order("U",5,"B",132.812,25));
        p.getOrders().add(new Order("D",6,"B",132.797,25));
        p.getOrders().add(new Order("N",9,"B",132.719,103));
        p.getOrders().add(new Order("U",4,"A",133.016,1));
        p.getOrders().add(new Order("D",6,"A",133.047,25));
        p.getOrders().add(new Order("N",9,"A",133.109,5));

        OrderProcessing orderProcessing = new OrderProcessing();
        orderProcessing.getOrderBook().setBid(bids);
        orderProcessing.getOrderBook().setAsk(asks);
        orderProcessing.packetProcessing(p);


    }
}

