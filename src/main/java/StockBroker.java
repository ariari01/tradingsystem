public interface StockBroker {
    void login(String ID, String Password);
    void sell(String stockCode, int price, int count);
    void buy(String stockCode, int price, int count);
    void getMarketPrice(String stockCode, int minute) throws InterruptedException;
}