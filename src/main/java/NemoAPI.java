import java.util.Random;
import java.time.LocalDateTime;

public class NemoAPI {
    public void certification( String ID, String Pass) {
        System.out.println("[네모]" + ID + "님 로그인 성공");
    }
    public void purchasingStock( String stockCode,int price, int count){
        System.out.println("[네모]" + stockCode + "를 " + price + "가격에 매수하였음");
    }
    public void sellingStock( String stockCode , int price, int count) {
        System.out.println("[네모]" + stockCode + "를 " + price + "가격에 매도하였음");
    }
    public int getMarketPrice ( String stockCode, int minute) {
        //minute ms초 이후 구매가 되는 방식 (최소 1 ms)
        if (minute <= 0) minute = 1;
        Thread.sleep(minute);
        Random random = new Random();
        return random.nextInt() % 10 * 100 + 5000;
    }
}