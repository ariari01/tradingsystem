public interface StockBroker {
    void login(String ID, String Password);
    void buy(String stockCode, int count, int price);
    void sell(String stockCode, int count, int price);
    int currentPrice(String stockCode);
}