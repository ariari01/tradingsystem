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

    public void buy(String stockCode, int price, int count) {
        try {
            api.purchasingStock(stockCode, price, count);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getMarketPrice(String stockCode,int min) throws InterruptedException {
        api.getMarketPrice(stockCode,min);
    }
}
