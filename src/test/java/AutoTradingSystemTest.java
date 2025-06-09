import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutoTradingSystemTest {
    public static final String EMPTY_STRING = "";

    @Mock
    StockBroker mockStockBroker;

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
    }

    @Nested
    class SellNiceTimingTest {
        public static final String NOT_IMPORTANT_STOCK_CODE = "123456";
        public static final int NOT_IMPORTANT_STOCK_SHARE = 3500;
        public static final int NOT_IMPORTANT_CURRENT_STOCK_PRICE = 10000;
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

            verify(mockStockBroker, never()).sell(anyString(), anyInt(), anyInt());
        }
    }
}