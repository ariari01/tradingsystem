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
    public void sell(String stockCode, int price, int count) {
        api.sellingStock(stockCode, price, count);
    }

    @Override
    public void buy(String stockCode, int price, int quantity) {
        if (price<0 ||quantity<0) throw new IllegalArgumentException("para is negative integer");
        try {
            api.purchasingStock(stockCode, price, quantity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
