public interface StockBroker {
    void login(String ID, String Password);

    void sell(String stockCode, int price, int count);
}