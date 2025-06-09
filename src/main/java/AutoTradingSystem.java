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
        if (isNullOrEmpty(id)) {
            throw new IllegalArgumentException("ID is Null");
        }

        if (isNullOrEmpty(password)) {
            throw new IllegalArgumentException("PASSWORD is Null");
        }

    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
