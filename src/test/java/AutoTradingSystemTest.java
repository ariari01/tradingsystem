import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutoTradingSystemTest {
    public static final String EMPTY_STRING = "";

    AutoTradingSystem autoTradingSystem;

    @Mock
    StockBroker mockStockBroker;

    @Mock
    KiwerAPI kiwerApi;

    @Mock
    NemoAPI nemoApi;

    @Nested
    class LoginTest {

        public static final String NOT_IMPORTANT_ID = "ID";
        public static final String NOT_IMPORTANT_PASSWORD = "PASSWORD";

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
        void 로그인_성공() {
            autoTradingSystem.selectStockBroker(mockStockBroker);
            doNothing().when(mockStockBroker).login(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);

            autoTradingSystem.login(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);

            verify(mockStockBroker, only()).login(NOT_IMPORTANT_ID, NOT_IMPORTANT_PASSWORD);
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
    }

    @Nested
    class SellNiceTimingTest {
        public static final String NOT_IMPORTANT_STOCK_CODE = "123456";
        public static final int NOT_IMPORTANT_STOCK_SHARE = 3500;
        public static final int NOT_IMPORTANT_CURRENT_STOCK_PRICE = 10000;
        public static final int NOT_IMPORTANT_DECREASED_PRICE = NOT_IMPORTANT_CURRENT_STOCK_PRICE - 1000;
        public static final int WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS = AutoTradingSystem.MAX_SELL_NICE_COUNT + 1;
        
        @BeforeEach
        void setUp() {
            autoTradingSystem = new AutoTradingSystem();
            autoTradingSystem.selectStockBroker(mockStockBroker);
        }

        @Test
        void 입력된_종목코드가_Null인_경우() throws InterruptedException {
            assertThatThrownBy(() -> autoTradingSystem.sellNiceTiming(null, NOT_IMPORTANT_STOCK_SHARE))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mockStockBroker, never()).getMarketPrice(anyString(), anyInt());
        }

        @Test
        void 입력된_종목코드가_Empty_String인_경우() throws InterruptedException {
            assertThatThrownBy(() -> autoTradingSystem.sellNiceTiming(EMPTY_STRING, NOT_IMPORTANT_STOCK_SHARE))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mockStockBroker, never()).getMarketPrice(anyString(), anyInt());
        }

        @Test
        void 입력된_주식_수량이_0인_경우() throws InterruptedException {
            assertThatThrownBy(() -> autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, 0))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mockStockBroker, never()).getMarketPrice(anyString(), anyInt());
        }

        @Test
        void 입력된_주식_수량이_마이너스인_경우() throws InterruptedException {
            assertThatThrownBy(() -> autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, -1))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mockStockBroker, never()).getMarketPrice(anyString(), anyInt());
        }

        @Test
        void 변동이_없을_때_매도하지_않는_경우() throws InterruptedException {
            doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE).when(mockStockBroker).getMarketPrice(anyString(), anyInt());

            autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

            verify(mockStockBroker, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).getMarketPrice(anyString(), anyInt());
            verify(mockStockBroker, never()).sell(anyString(), anyInt(), anyInt());
        }

        @Test
        void 상승_추세가_100회_동안_계속될_때_매도하지_않는_경우() throws InterruptedException {
            AtomicInteger price = new AtomicInteger(NOT_IMPORTANT_CURRENT_STOCK_PRICE);
            doAnswer(invocationOnMock -> price.getAndAdd(100))
                    .when(mockStockBroker).getMarketPrice(anyString(), anyInt());

            autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

            verify(mockStockBroker, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).getMarketPrice(anyString(), anyInt());
            verify(mockStockBroker, never()).sell(anyString(), anyInt(), anyInt());
        }

        @Test
        void 하락_추세가_발생_시_매도하는_경우() throws InterruptedException {
            doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE, NOT_IMPORTANT_DECREASED_PRICE)
                    .when(mockStockBroker).getMarketPrice(anyString(), anyInt());

            autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

            verify(mockStockBroker, times(1)).sell(anyString(), anyInt(), anyInt());
        }

        @Nested
        class KiwerTest {

            @BeforeEach
            void setUp() {
                autoTradingSystem.selectStockBroker(new KiwerDriver(kiwerApi));
            }

            @Test
            void 변동이_없을_때_매도하지_않는_경우() throws InterruptedException {
                doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE).when(kiwerApi).currentPrice(anyString());

                autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

                verify(kiwerApi, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).currentPrice(anyString());
                verify(kiwerApi, never()).sell(anyString(), anyInt(), anyInt());
            }

            @Test
            void 상승_추세가_100회_동안_계속될_때_매도하지_않는_경우() throws InterruptedException {
                AtomicInteger price = new AtomicInteger(NOT_IMPORTANT_CURRENT_STOCK_PRICE);
                doAnswer(invocationOnMock -> price.getAndAdd(100))
                        .when(kiwerApi).currentPrice(anyString());

                autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

                verify(kiwerApi, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).currentPrice(anyString());
                verify(kiwerApi, never()).sell(anyString(), anyInt(), anyInt());
            }

            @Test
            void 하락_추세가_발생_시_매도하는_경우() throws InterruptedException {
                doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE, NOT_IMPORTANT_DECREASED_PRICE)
                        .when(kiwerApi).currentPrice(anyString());

                autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

                verify(kiwerApi, times(1)).sell(anyString(), anyInt(), anyInt());
            }
        }

        @Nested
        class NemoTest {

            @BeforeEach
            void setUp() {
                autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            }

            @Test
            void 변동이_없을_때_매도하지_않는_경우() throws InterruptedException {
                doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE).when(nemoApi).getMarketPrice(anyString(), anyInt());

                autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

                verify(nemoApi, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).getMarketPrice(anyString(), anyInt());
                verify(nemoApi, never()).sellingStock(anyString(), anyInt(), anyInt());
            }

            @Test
            void 상승_추세가_100회_동안_계속될_때_매도하지_않는_경우() throws InterruptedException {
                AtomicInteger price = new AtomicInteger(NOT_IMPORTANT_CURRENT_STOCK_PRICE);
                doAnswer(invocationOnMock -> price.getAndAdd(100))
                        .when(nemoApi).getMarketPrice(anyString(), anyInt());

                autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

                verify(nemoApi, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).getMarketPrice(anyString(), anyInt());
                verify(nemoApi, never()).sellingStock(anyString(), anyInt(), anyInt());
            }

            @Test
            void 하락_추세가_발생_시_매도하는_경우() throws InterruptedException {
                doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE, NOT_IMPORTANT_DECREASED_PRICE)
                        .when(nemoApi).getMarketPrice(anyString(), anyInt());

                autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

                verify(nemoApi, times(1)).sellingStock(anyString(), anyInt(), anyInt());
            }
        }
    }

    @Nested
    class SellTest {
        AutoTradingSystem autoTradingSystem;

        @BeforeEach
        void setUp() {
            autoTradingSystem = new AutoTradingSystem();
        }

        public static final String NOT_IMPORTANT_STOCK_CODE = "STOCKSTOCK";
        public static final int NOT_IMPORTANT_PRICE = 123;
        public static final int NOT_IMPORTANT_COUNT = 1;

        @Test
        @DisplayName("Kiwer 증권에 팔기")
        void sell_kiwer_api() {
            autoTradingSystem.selectStockBroker(new KiwerDriver(kiwerApi));
            doNothing().when(kiwerApi).sell(anyString(), anyInt(), anyInt());

            autoTradingSystem.sell(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_PRICE, NOT_IMPORTANT_COUNT);

            verify(kiwerApi, only()).sell(anyString(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("Nemo 증권에 팔기")
        void sell_nemo_api() {
            autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            doNothing().when(nemoApi).sellingStock(anyString(), anyInt(), anyInt());

            autoTradingSystem.sell(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_PRICE, NOT_IMPORTANT_COUNT);

            verify(nemoApi, only()).sellingStock(anyString(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("잘못된 파라메터 값으로 sell 호출 시 에러")
        void invalid_input_for_sell() {
            autoTradingSystem.selectStockBroker(new NemoDriver(nemoApi));
            Assertions.assertThrows(IllegalArgumentException.class, () -> autoTradingSystem.sell("", 0, 0));
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