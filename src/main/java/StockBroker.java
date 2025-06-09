public interface StockBroker {
    void login(String ID, String Password);
    boolean checkIncreasingTrend(String stockCode);
    void sell(String stockCode, int price, int count);
    void buy(String stockCode, int price, int count);
    int getMarketPrice(String stockCode, int minute) throws InterruptedException;
}