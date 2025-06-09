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

    @Mock
    StockBroker mockStockBroker;

    @Nested
    class LoginTest {

        AutoTradingSystem autoTradingSystem;

        public static final String NOT_IMPORTANT_ID = "ID";
        public static final String NOT_IMPORTANT_PASSWORD = "PASSWORD";
        public static final String EMPTY_STRING = "";

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
        AutoTradingSystem autoTradingSystem;

        @BeforeEach
        void setUp() {
            autoTradingSystem = new AutoTradingSystem();
            autoTradingSystem.selectStockBroker(mockStockBroker);
        }

        @Test
        void 변동이_없을_때_매도하지_않는_경우() {
            doReturn(10000).when(mockStockBroker).currentPrice(anyString());

            autoTradingSystem.sellNiceTiming("151515", 3500);

            verify(mockStockBroker, never()).sell(anyString(), anyInt(), anyInt());
        }
    }
}