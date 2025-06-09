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

    boolean checkIncreasingTrend(String stockCode){
        if (stockBroker.checkIncreasingTrend(stockCode)) return true;
        return false;
    }
}

class Application {
    AutoTradingSystem autoTradingSystem;

    public Application(AutoTradingSystem autoTradingSystem) {
        this.autoTradingSystem = autoTradingSystem;
    }

    public void buyNiceTiming(String stockCode, int amount){
        // 증가하는 추세 확인
        if (!autoTradingSystem.checkIncreasingTrend(stockCode)) return;

    }
}
