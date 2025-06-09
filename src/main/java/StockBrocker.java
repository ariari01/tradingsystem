public interface StockBrocker {
    StockBrocker selectStockBroker(String name);
    void login(String ID, String Password);
    void buy(String stockCode, int count, int price);
    void sell(String stockCode, int count, int price);
    int currentPrice(String stockCode);
    boolean buyNiceTiming(String stockCode, int amount);
    boolean sellNiceTiming(String stockCode, int quantity);
}