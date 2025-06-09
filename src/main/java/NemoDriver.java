import jdk.jshell.spi.ExecutionControl;

public class NemoDriver implements StockBroker {
    private final NemoAPI api;

    public NemoDriver() {
        api = new NemoAPI();
    }

    public NemoDriver(NemoAPI api) {
        this.api = api;
    }

    @Override
    public void login(String id, String password) {
        api.certification(id, password);
    }

    @Override
    public void buy(String stockCode, int count, int price) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void sell(String stockCode, int count, int price) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int currentPrice(String stockCode) {
        throw new RuntimeException("Not implemented");
    }
}
