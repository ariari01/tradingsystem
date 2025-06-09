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


    int getCurrentMarketPrice(String stockCode) throws InterruptedException {

        if (isNullOrEmpty(stockCode)) {
            throw new IllegalArgumentException("주식코드가 잘못되었습니다");
        }



        stockBroker.getMarketPrice(stockCode,1);

        return 0;
    }

}
