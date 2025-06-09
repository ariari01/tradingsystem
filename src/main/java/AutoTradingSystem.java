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

    public void sellNiceTiming(String stockCode, int share) throws InterruptedException {
        if (isNullOrEmpty(stockCode))
            throw new IllegalArgumentException();

        if (share <= 0)
            throw new IllegalArgumentException();

        int prevPrice = stockBroker.getMarketPrice(stockCode, 100);
        for (int i = 0; i < MAX_SELL_NICE_COUNT; i++) {
            int curPrice = stockBroker.getMarketPrice(stockCode, 100);
            if (prevPrice > curPrice) {
                stockBroker.sell(stockCode, curPrice, share);
                return;
            }
            prevPrice = curPrice;
        }
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

    public void buy(String stockCode, int price, int count) {
        isValidBuyArguments(stockCode, price, count);
        stockBroker.buy(stockCode,price,count);
    }

    private static void isValidBuyArguments(String stockCode, int price, int count) {
        if (stockCode.isEmpty() || price <= 0 || count <= 0) {
            throw new IllegalArgumentException("para is negative integer");
        }
    }

    int getCurrentMarketPrice(String stockCode) throws InterruptedException {

        if (isNullOrEmpty(stockCode)) {
            throw new IllegalArgumentException("주식코드가 잘못되었습니다");
        }

        int currentTimeInfo = 0;
        stockBroker.getMarketPrice(stockCode, currentTimeInfo);

        return 0;
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

    public void buyNiceTiming(String stockCode, int amount) throws InterruptedException {
        // 현재가 도출
        int currentPrice = autoTradingSystem.getCurrentMarketPrice(stockCode);

        // 증가하는 추세 확인
        if (!autoTradingSystem.checkIncreasingTrend(stockCode)) return;
        if (amount < currentPrice) return;

        int count = autoTradingSystem.getCount(amount, currentPrice);
        for(int i=0; i<count; i++){
            autoTradingSystem.buy(stockCode, currentPrice, count);
        }
    }
}
