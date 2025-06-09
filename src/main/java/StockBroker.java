public interface StockBroker {
    void login(String ID, String Password);
    boolean checkIncreasingTrend(String stockCode);
}