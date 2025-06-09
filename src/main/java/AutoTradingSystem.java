public class AutoTradingSystem {
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

    private static void isValidSellArguments(String stockCode, int price, int count) {
        if (stockCode.length() == 0 || price <= 0 || count <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public void sell(String stockCode, int price, int count) {
        isValidSellArguments(stockCode, price, count);
        stockBroker.sell(stockCode, price, count);
    }
}
