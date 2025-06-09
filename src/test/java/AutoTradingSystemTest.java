import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutoTradingSystemTest {
    @Mock
    KiwerAPI kiwerApi;

    @Mock
    NemoAPI nemoApi;




    @Nested
    class LoginTest {
        AutoTradingSystem autoTradingSystem;

        public static final String NOT_IMPORTANT_ID = "ID";
        public static final String NOT_IMPORTANT_PASSWORD = "PASSWORD";
        public static final String EMPTY_STRING = "";
        public static final String NOT_IMPORTANT_STOCK_CODE = "STOCKSTOCK";
        public static final int NOT_IMPORTANT_PRICE = 123;
        public static final int NOT_IMPORTANT_COUNT = 1;

        @BeforeEach
        void setUp() {
            autoTradingSystem = new AutoTradingSystem();
        }

        @Test
        void 시스템_로그인_시_ID가_Null이면_예외발생() {
            assertThatThrownBy(() -> autoTradingSystem.login(null, NOT_IMPORTANT_PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 시스템_로그인_시_ID가_Empty이면_예외발생() {
            assertThatThrownBy(() -> autoTradingSystem.login(EMPTY_STRING, NOT_IMPORTANT_PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 시스템_로그인_시_Password가_Null이면_예외발생() {
            assertThatThrownBy(() -> autoTradingSystem.login(NOT_IMPORTANT_ID, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 시스템_로그인_시_Password가_Empty이면_예외발생() {
            assertThatThrownBy(() -> autoTradingSystem.login(NOT_IMPORTANT_ID, EMPTY_STRING))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void Broker가_Kiwer_일_때_로그인_성공() {
            autoTradingSystem.selectStockBroker(new KiwerDriver(kiwerApi));
            doNothing().when(kiwerApi).login(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);

            autoTradingSystem.login(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);

            verify(kiwerApi, only()).login(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);
        }

        @Test
        void Broker가_Nemo_일_때_로그인_성공() {
            autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            doNothing().when(nemoApi).certification(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);

            autoTradingSystem.login(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);

            verify(nemoApi, only()).certification(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);
        }

        @Test
        void sell_kiwer_api() {
            autoTradingSystem.selectStockBroker(new KiwerDriver(kiwerApi));
            doNothing().when(kiwerApi).sell(anyString(), anyInt(), anyInt());

            autoTradingSystem.sell(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_PRICE, NOT_IMPORTANT_COUNT);

            verify(kiwerApi, only()).sell(anyString(), anyInt(), anyInt());
        }

        @Test
        void sell_nemo_api() {
            autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            doNothing().when(nemoApi).sellingStock(anyString(), anyInt(), anyInt());

            autoTradingSystem.sell(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_PRICE, NOT_IMPORTANT_COUNT);

            verify(nemoApi, only()).sellingStock(anyString(), anyInt(), anyInt());
        }

        @Test
        void invalid_input_for_sell() {
            autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                autoTradingSystem.sell("", 0, 0);
            });
        }
    }


    @Nested
    class BuyTest {
        AutoTradingSystem autoTradingSystem;
        public static final String TEST_STOCK_CODE = "APPL";
        public static final int TEST_VALID_PRICE = 33;
        public static final int TEST_VALID_COUNT = 123;
        public static final int TEST_INVALID_PRICE = -1;
        public static final int TEST_INVALID_COUNT = -1;

        @BeforeEach
        void setUp() {
            autoTradingSystem = new AutoTradingSystem();
        }

        @Test
        void Broker가_Kiwer_일_때_Buy_성공() {
            autoTradingSystem.selectStockBroker(new KiwerDriver(kiwerApi));
            autoTradingSystem.buy(TEST_STOCK_CODE, TEST_VALID_PRICE, TEST_VALID_COUNT);
            verify(kiwerApi, times(1)).buy(anyString(), anyInt(), anyInt());
        }

        @Test
        void Broker가_Kiwer_일_때_Illegal_Parameter_입력_Exception_발생() {
            autoTradingSystem.selectStockBroker(new KiwerDriver(kiwerApi));
            assertThrows(IllegalArgumentException.class, () -> {
                autoTradingSystem.buy(TEST_STOCK_CODE, TEST_INVALID_PRICE, TEST_VALID_COUNT);
            });

            assertThrows(IllegalArgumentException.class, () -> {
                autoTradingSystem.buy(TEST_STOCK_CODE, TEST_VALID_PRICE, TEST_INVALID_COUNT);
            });
        }

        @Test
        void Broker가_Nemo_일_때_Buy_성공() {
            autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            autoTradingSystem.buy(TEST_STOCK_CODE, TEST_VALID_PRICE, TEST_VALID_COUNT);
            verify(nemoApi, times(1)).purchasingStock(anyString(), anyInt(), anyInt());
        }

        @Test
        void Broker가_Nemo_일_때_Illegal_Parameter_입력_Exception_발생() {
            autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            assertThrows(IllegalArgumentException.class, () -> {
                autoTradingSystem.buy(TEST_STOCK_CODE, TEST_INVALID_PRICE, TEST_VALID_COUNT);
            });
            assertThrows(IllegalArgumentException.class, () -> {
                autoTradingSystem.buy(TEST_STOCK_CODE, TEST_VALID_PRICE, TEST_INVALID_COUNT);
            });
        }
    }

    @Nested
    class currentPriceTest {

        AutoTradingSystem autoTradingSystem;

        public static final String NOT_IMPORTANT_STOCK_CODE = "StockCode";

        @BeforeEach
        void setUp() {
            autoTradingSystem = new AutoTradingSystem();
        }


        @Test
        void 주식코드_값이NULL일때() throws InterruptedException {
            assertThatThrownBy(() -> autoTradingSystem.getCurrentMarketPrice(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void Kiwer_주식코드로_현재_시장가격_가져오기() throws InterruptedException {
            autoTradingSystem.selectStockBroker(new KiwerDriver(kiwerApi));
            int actual=autoTradingSystem.getCurrentMarketPrice(NOT_IMPORTANT_STOCK_CODE);
            assertNotNull(actual);
        }

        @Test
        void Nemo_주식코드로_현재_시장가격_가져오기() throws InterruptedException {
            autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            int actual=autoTradingSystem.getCurrentMarketPrice(NOT_IMPORTANT_STOCK_CODE);
            assertNotNull(actual);
        }

    }

}