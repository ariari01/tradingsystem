public class NemoDriver implements StockBroker {

    NemoAPI api;

    @Override
    public void login(String ID, String Password) {
        api.certification(ID, Password);
    }
}
