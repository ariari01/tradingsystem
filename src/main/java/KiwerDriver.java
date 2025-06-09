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
    public void buy(String stockCode, int price, int count) {
        api.buy(stockCode, price, count);
    }

    @Override
    public int getPrice(String stockCode) {
        return api.currentPrice(stockCode);
    }

    @Override
    public boolean checkIncreasingTrend(String stockCode) {
        try {
            int currentPrice = api.currentPrice(stockCode);
            for (int i = 0; i < 5; i++) {
                Thread.sleep(1000);
                int nextPrice = api.currentPrice(stockCode);
                if (nextPrice <= currentPrice) return false;

                currentPrice = nextPrice;
            }
        } catch (InterruptedException e){
            System.out.println("[ERROR] checkIncreasingTrend " + e.getMessage());
        }

        return true;
    }
}
