public class KiwerDriver implements StockBroker {
    KiwerAPI api = new KiwerAPI();

    @Override
    public void login(String id, String password) {
        api.login(id, password);
    }
}
