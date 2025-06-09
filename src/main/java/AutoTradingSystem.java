public class AutoTradingSystem {
    private StockBroker stockBroker;

    void selectStockBroker(String name) {
        switch (name) {
            case "Kiwer" -> stockBroker = new KiwerDriver();
            case "Nemo" -> stockBroker = new NemoDriver();
            default -> throw new RuntimeException();
        }
    }

    public void login(String id, String password) {
        if (id == null) {
            throw new IllegalArgumentException("ID is Null");
        }
    }
}
