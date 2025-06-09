public interface StockBroker {
    void login(String ID, String Password);
    void buy(String stockCode, int price, int count);
    int getPrice(String stockCode);
    boolean checkIncreasingTrend(String stockCode);
}