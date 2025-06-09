import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutoTradingSystemTest {
    public static final String EMPTY_STRING = "";

    @Mock
    StockBroker mockStockBroker;

    @Mock
    KiwerAPI kiwerApi;

    @Mock
    NemoAPI nemoApi;

    @Nested
    class LoginTest {

        AutoTradingSystem autoTradingSystem;

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

        AutoTradingSystem autoTradingSystem;

        @BeforeEach
        void setUp() {
            autoTradingSystem = new AutoTradingSystem();
            autoTradingSystem.selectStockBroker(mockStockBroker);
        }

        @Test
        void 입력된_종목코드가_Null인_경우() {
            assertThatThrownBy(() -> autoTradingSystem.sellNiceTiming(null, NOT_IMPORTANT_STOCK_SHARE))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mockStockBroker, never()).currentPrice(anyString());
        }

        @Test
        void 입력된_종목코드가_Empty_String인_경우() {
            assertThatThrownBy(() -> autoTradingSystem.sellNiceTiming(EMPTY_STRING, NOT_IMPORTANT_STOCK_SHARE))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mockStockBroker, never()).currentPrice(anyString());
        }

        @Test
        void 입력된_주식_수량이_0인_경우() {
            assertThatThrownBy(() -> autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, 0))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mockStockBroker, never()).currentPrice(anyString());
        }

        @Test
        void 입력된_주식_수량이_마이너스인_경우() {
            assertThatThrownBy(() -> autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, -1))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(mockStockBroker, never()).currentPrice(anyString());
        }

        @Test
        void 변동이_없을_때_매도하지_않는_경우() {
            doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE).when(mockStockBroker).currentPrice(anyString());

            autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

            verify(mockStockBroker, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).currentPrice(anyString());
            verify(mockStockBroker, never()).sell(anyString(), anyInt(), anyInt());
        }

        @Test
        void 상승_추세가_100회_동안_계속될_때_매도하지_않는_경우() {
            AtomicInteger price = new AtomicInteger(NOT_IMPORTANT_CURRENT_STOCK_PRICE);
            doAnswer(invocationOnMock -> price.getAndAdd(100))
                    .when(mockStockBroker).currentPrice(anyString());

            autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

            verify(mockStockBroker, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).currentPrice(anyString());
            verify(mockStockBroker, never()).sell(anyString(), anyInt(), anyInt());
        }

        @Test
        void 하락_추세가_발생_시_매도하는_경우() {
            doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE, NOT_IMPORTANT_DECREASED_PRICE)
                    .when(mockStockBroker).currentPrice(anyString());

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
            void 변동이_없을_때_매도하지_않는_경우() {
                doReturn(NOT_IMPORTANT_CURRENT_STOCK_PRICE).when(kiwerApi).currentPrice(anyString());

                autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

                verify(kiwerApi, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).currentPrice(anyString());
                verify(kiwerApi, never()).sell(anyString(), anyInt(), anyInt());
            }

            @Test
            void 상승_추세가_100회_동안_계속될_때_매도하지_않는_경우() {
                AtomicInteger price = new AtomicInteger(NOT_IMPORTANT_CURRENT_STOCK_PRICE);
                doAnswer(invocationOnMock -> price.getAndAdd(100))
                        .when(kiwerApi).currentPrice(anyString());

                autoTradingSystem.sellNiceTiming(NOT_IMPORTANT_STOCK_CODE, NOT_IMPORTANT_STOCK_SHARE);

                verify(kiwerApi, times(WANTED_NUMBER_OF_CURRENT_PRICE_INVOCATIONS)).currentPrice(anyString());
                verify(kiwerApi, never()).sell(anyString(), anyInt(), anyInt());
            }

            @Test
            void 하락_추세가_발생_시_매도하는_경우() {
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
}