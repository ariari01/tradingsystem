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
}
