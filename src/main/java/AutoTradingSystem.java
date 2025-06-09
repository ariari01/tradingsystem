public class AutoTradingSystem {
    public static final int MAX_SELL_NICE_COUNT = 100;
    private StockBroker stockBroker;

    void selectStockBroker(StockBroker stockBroker) {
        this.stockBroker = stockBroker;
    }

    public void login(String id, String password) {
        if (isNullOrEmpty(id)) {
            throw new IllegalArgumentException("ID is Null");
        }

        if (isNullOrEmpty(password)) {
            throw new IllegalArgumentException("PASSWORD is Null");
        }

        stockBroker.login(id, password);
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public void sellNiceTiming(String stockCode, int share) {
        if (isNullOrEmpty(stockCode))
            throw new IllegalArgumentException();

        if (share <= 0)
            throw new IllegalArgumentException();

        int prevPrice = stockBroker.currentPrice(stockCode);
        for (int i = 0; i < MAX_SELL_NICE_COUNT; i++) {
            int curPrice = stockBroker.currentPrice(stockCode);
            if (prevPrice > curPrice) {
                stockBroker.sell(stockCode, share, curPrice);
                return;
            }
            prevPrice = curPrice;
        }
    }
}
