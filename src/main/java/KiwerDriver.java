public class KiwerDriver implements StockBroker {
    KiwerAPI api = new KiwerAPI();

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
    public void buy(String stockCode, int count, int price) {

    }

    @Override
    public void sell(String stockCode, int count, int price) {

    }

    @Override
    public int currentPrice(String stockCode) {
        return 0;
    }
}
