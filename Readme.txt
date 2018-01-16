ReadMe:
==============================================================
Assumptions:
==============================================================
1. All the update feeds are for the same Security.
2. One output record per Sequence Number (Packet - feeds with the same sequence number).
3. OrderBook entry for Sequence Number is not printed if the book is not complete.
4. Order book is incomplete if the number of levels in bids and asks are not equal to 10 or if the price of the security is not in sorted order.
5. If a level in the orderbook is requested to be updated and is empty a new entry is created.

==============================================================
Steps to Run:
==============================================================
1. Download the project from the link in the mail
2. Unzip the project
3. cd OrderBook
4. java -jar OrderBook.jar
5. The input feed data is in the src/resources folder
6. The output is saved in /OrderBook/OrderBook.data

