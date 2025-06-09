
public class KiwerDriver implements StockBroker {
    private final KiwerAPI api;

    public KiwerDriver() {
        api = new KiwerAPI();
    }

    public KiwerDriver(KiwerAPI api) {
        this.api = api;
    }

    @Override
    public void login(String id, String password) {
        api.login(id, password);
    }

    @Override
    public void sell(String stockCode, int price, int count) {
        api.sell(stockCode, price, count);
    }
    @Override
    public void buy(String stockCode, int price, int quantity) {
        try {
            api.buy(stockCode, quantity, price);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
