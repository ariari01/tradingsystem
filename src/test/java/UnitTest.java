import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UnitTest {

    @Test
    void Kiwer로그인_동작_확인() {
        StockBrocker broker = new KiwerDriver(); // 아직 구현 안 된 상태
        broker.login("username", "1234");
    }

    @Test
    void 로그인시_ID가_null이면_예외발생() {
        StockBrocker broker = new KiwerDriver(); // 구현 예정

        assertThatThrownBy(() -> broker.login(null, "1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID");
    }

    @Test
    void Nemo로그인_동작_확인() {
        StockBrocker broker = new NemoDriver(); // 아직 구현 안 된 상태
        broker.login("username", "1234");
    }

    @Test
    void 로그인시_ID가_null이면_예외발생() {
        StockBrocker broker = new NemoDriver(); // 구현 예정

        assertThatThrownBy(() -> broker.login(null, "1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID");
    }
}
