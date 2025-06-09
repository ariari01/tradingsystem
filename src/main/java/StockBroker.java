public interface StockBroker {
    void login(String ID, String Password);
    void getMarketPrice(String stockCode, int minute) throws InterruptedException;
}