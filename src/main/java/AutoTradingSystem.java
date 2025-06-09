public class AutoTradingSystem {
    private StockBrocker stockBrocker;

    public AutoTradingSystem(StockBrocker stockBrocker) {
        this.stockBrocker = stockBrocker;
    }


    void selectStockBroker(StockBrocker stockBroker) {
        this.stockBrocker = stockBroker;
    }

    void getCurrentMarketPrice(String code){

    }

}
