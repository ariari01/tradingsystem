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
    public boolean checkIncreasingTrend(String stockCode) {
        try {
            int currentPrice = api.getMarketPrice(stockCode, 1000);

            for (int i = 0; i < 5; i++) {
                int nextPrice = api.getMarketPrice(stockCode, 1000);
                if (nextPrice <= currentPrice) return false;

                currentPrice = nextPrice;
            }
        } catch (InterruptedException e){
            System.out.println("[ERROR] checkIncreasingTrend " + e.getMessage());
        }

        return true;
    }
}
