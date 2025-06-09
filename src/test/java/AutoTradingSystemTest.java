import org.junit.jupiter.api.Assertions;
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
}