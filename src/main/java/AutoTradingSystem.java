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

    public void buy(String stockCode, int price, int count) {
        isValidBuyArguments(stockCode, price, count);
        stockBroker.buy(stockCode, price, count);
    }

    private static void isValidBuyArguments(String stockCode, int price, int count) {
        if (stockCode.length() == 0 || price <= 0 || count <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getPrice(String stockCode){
        return stockBroker.getPrice(stockCode);
    }

    boolean checkIncreasingTrend(String stockCode){
        return stockBroker.checkIncreasingTrend(stockCode);
    }

    int getCount(int amount, int currentPrice){
        return amount / currentPrice;
    }
}

class Application {
    AutoTradingSystem autoTradingSystem;

    public Application(AutoTradingSystem autoTradingSystem) {
        this.autoTradingSystem = autoTradingSystem;
    }

    public void buyNiceTiming(String stockCode, int amount){
        // 현재가 도출
        int currentPrice = autoTradingSystem.getPrice(stockCode);

        // 증가하는 추세 확인
        if (!autoTradingSystem.checkIncreasingTrend(stockCode)) return;
        if (amount < currentPrice) return;

        int count = autoTradingSystem.getCount(amount, currentPrice);
        for(int i=0; i<count; i++){
            autoTradingSystem.buy(stockCode, currentPrice, count);
        }
    }
}
