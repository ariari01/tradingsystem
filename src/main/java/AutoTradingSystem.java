public class AutoTradingSystem {
    private StockBroker stockBroker;

    void selectStockBroker(String name) {
        switch (name) {
            case "Kiwer" -> stockBroker = new KiwerDriver();
            case "Nemo" -> stockBroker = new NemoDriver();
            default -> throw new RuntimeException();
        }
    }
}
