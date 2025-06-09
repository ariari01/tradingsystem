
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
    public void getMarketPrice(String stockCode,int min)  {
        //Kiwer는 현재 시간의 Price만 제공한다.
        if(min<=1){
            api.currentPrice(stockCode);
        }else {
            return ;
        }
    }
}
