
public class KiwerDriver implements StockBroker {
    KiwerAPI api = new KiwerAPI();

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
    public void sell(String stockCode, int price, int count) {
        api.sell(stockCode, price, count);
    }
  
    @Override
    public void buy(String stockCode, int price, int count) {
        try {
            api.buy(stockCode, count, price);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public int getMarketPrice(String stockCode,int min) throws InterruptedException {
        //Kiwer는 현재 시간의 Price만 제공한다.
        if(min<=1){
            return api.currentPrice(stockCode);
        }else {
            Thread.sleep(min);
            return api.currentPrice(stockCode);
        }
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
