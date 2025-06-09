public class MockDriver implements StockBroker {

    @Override
    public void login(String ID, String Password) {
    }

    @Override
    public void buy(String stockCode, int count, int price) {
        System.out.print("Mock Driver Buy Success");
    }
}
