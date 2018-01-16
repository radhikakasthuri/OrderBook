package com.gs;

import java.io.*;
import java.util.List;

/**
 * Created by rkasthuri on 1/11/18.
 */
public class OrderProcessing {

    public static final String INPUT_FILENAME = "resources/orderBookExercise.data";
    public static final String OUTPUT_FILENAME = "orderBook.data";
    BufferedWriter bufferedWriter = null;
    public static final int FEEDLENGTH = 6;
    OrderBook orderBook;
    static int packetCount = 0;
    static int recordCount = 0;

    public OrderProcessing() {
        this.orderBook = new OrderBook();
        prepareOutFile();
    }

    /**
     * Reads the feed from the data file
     * Groups them into packet
     * Process a packet to Orders
     * Updates the OrderBook
     */
    public void feedProcessing() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            // Reading the data file from the resource folder
            InputStream is = classLoader.getResourceAsStream(INPUT_FILENAME);
            InputStreamReader fileReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            String seqNo = null;
            Packet packet = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                //feed validation if the feed does not contain six columns - drop it
                String[] feed = line.split(",");
                if (feed.length == FEEDLENGTH) {
                    //Grouping feeds into Packets for order processing
                    if (seqNo == null || packet == null || !seqNo.equals(feed[0])) {
                        packetProcessing(packet);
                        seqNo = feed[0];
                        packet = new Packet(seqNo);
                        packetCount++;
                    }
                    packet.addFeed(feed);
                }
            }
            System.out.println("Packet Count :" + packetCount);
            System.out.println("Record Count :" + recordCount);
            packetProcessing(packet);
            bufferedReader.close();
            bufferedWriter.close();
        } catch (Exception ex) {
            System.out.println("Error in OrderProcessing.feedProcessing() " + ex);
        }
    }

    /**
     * Updates the orderBook with the Orders in the packet
     * @param packet
     */
    public void packetProcessing(Packet packet) {
        if (packet != null) {
            String seqNo = packet.getSeqNo();
            List<Order> orders = packet.getOrders();
            for (Order order : orders) {
                orderBook.updateOrderBook(order, seqNo);
            }
            if (orderBook.isBookComplete()) {
                recordCount++;
                generateOrderBookEntry(seqNo);
            }
        }
    }
    /**
     * Preparing the bufferedWriter
     * to write orderBook entry to the outputFile
     */
    public void prepareOutFile() {
        try {
            File outFile = new File(OUTPUT_FILENAME);
            outFile.createNewFile();
            FileWriter fileWriter = new FileWriter(outFile);
            bufferedWriter = new BufferedWriter(fileWriter);

        } catch (Exception ex) {
            System.out.println("Error in OrederProcessing Constructor - Cannot create file " + ex);
        }
    }

    /**
     * Writes the complete orderBook entry to the outputFile
     * for the given Sequence Number
     * @param seqNo
     */
    public void generateOrderBookEntry(String seqNo) {
        try {
            StringBuilder bidSb = new StringBuilder(seqNo);
            bidSb.append(",\t");
            StringBuilder askSb = new StringBuilder();
            for (int i=0; i < orderBook.LEVELS; i++) {
                Order bid = orderBook.getBid(i);
                Order ask = orderBook.getAsk(i);
                bidSb.append(bid.toString());
                bidSb.append(",\t");
                askSb.append(ask.toString());
                askSb.append(",\t");
            }
            askSb.setLength(askSb.length() - 2);
            bufferedWriter.write(bidSb.toString());
            bufferedWriter.write(askSb.toString());
            bufferedWriter.write("\n");
        } catch (Exception ex) {
            System.out.println("Error in OrderProcessing.generateOrderBookEntry" + ex);
        }
    }
}
