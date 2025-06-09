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

        @Test
        void Kiwer_로그인_시_ID가_Null이면_예외발생() {
            AutoTradingSystem autoTradingSystem = new AutoTradingSystem();
            autoTradingSystem.selectStockBroker("Kiwer");

            assertThatThrownBy(() -> autoTradingSystem.login(null, "PASSWORD"))
                    .isInstanceOf(Exception.class);
        }

        @Test
        void Nemo_로그인_시_ID가_Null이면_예외발생() {
            AutoTradingSystem autoTradingSystem = new AutoTradingSystem();
            autoTradingSystem.selectStockBroker("Nemo");

            assertThatThrownBy(() -> autoTradingSystem.login(null, "PASSWORD"))
                    .isInstanceOf(Exception.class);
        }
    }
}