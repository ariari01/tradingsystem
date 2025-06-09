public class NemoDriver implements StockBroker {
    NemoAPI api = new NemoAPI();

    @Override
    public void login(String id, String password) {
        api.certification(id, password);
    }
}
