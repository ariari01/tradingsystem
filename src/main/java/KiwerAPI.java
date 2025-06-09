import java.util.Random;

public class KiwerAPI {
    public void login( String ID, String Password) {
        System.out.println(ID + "님 로그인 성공");
    }
    public void buy( String stockCode, int count , int price) {
        System.out.println(stockCode + "를 " + price + "가격에 매수하였음");
    }
    public void sell( String stockCode , int count , int price) {
        System.out.println(stockCode + "를 " + price + "가격에 매도하였음");
    }
    public int currentPrice ( String stockCode) {
        Random random = new Random();
        return random.nextInt() % 10 * 100 + 5000;
    }
}