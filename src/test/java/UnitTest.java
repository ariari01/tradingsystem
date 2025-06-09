import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitTest {
    @Mock
    KiwerAPI kiwerAPI;

    @Mock
    NemoAPI nemoAPI;

    @Nested
    class LoginTest {

        public static final String NOT_IMPORTANT_ID = "ID";
        public static final String NOT_IMPORTANT_PASSWORD = "PASSWORD";
        public static final String EMPTY_STRING = "";

        @Test
        void 시스템_로그인_시_ID가_Null이면_예외발생() {
            AutoTradingSystem autoTradingSystem = new AutoTradingSystem();

            assertThatThrownBy(() -> autoTradingSystem.login(null, NOT_IMPORTANT_PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 시스템_로그인_시_ID가_Empty이면_예외발생() {
            AutoTradingSystem autoTradingSystem = new AutoTradingSystem();

            assertThatThrownBy(() -> autoTradingSystem.login(EMPTY_STRING, NOT_IMPORTANT_PASSWORD))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 시스템_로그인_시_Password가_Null이면_예외발생() {
            AutoTradingSystem autoTradingSystem = new AutoTradingSystem();

            assertThatThrownBy(() -> autoTradingSystem.login(NOT_IMPORTANT_ID, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 시스템_로그인_시_Password가_Empty이면_예외발생() {
            AutoTradingSystem autoTradingSystem = new AutoTradingSystem();

            assertThatThrownBy(() -> autoTradingSystem.login(NOT_IMPORTANT_ID, EMPTY_STRING))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }
}