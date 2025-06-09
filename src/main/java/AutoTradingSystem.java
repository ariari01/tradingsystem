public class AutoTradingSystem {
    public static final String KIWER = "Kiwer";
    public static final String NEMO = "Nemo";

    private StockBroker stockBroker;

    void selectStockBroker(String name) {
        switch (name) {
            case KIWER -> stockBroker = new KiwerDriver();
            case NEMO -> stockBroker = new NemoDriver();
            default -> throw new RuntimeException();
        }
    }

    public void login(String id, String password) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID is Null");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("PASSWORD is Null");
        }

    }
}
