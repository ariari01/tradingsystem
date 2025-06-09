import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UnitTest {
    @Mock
    KiwerDriver kiwerMock;

    @Mock
    NemoDriver nemoDriver;

    @Test
    void kiwer를_선택하면_KiwerDriver_반환() {
        StockBrocker broker = StockBrocker.selectStockBroker("kiwer");

        assertThat(broker).isInstanceOf(KiwerDriver.class);
    }

    @Test
    void Nemo를_선택하면_NemoDriver_반환() {
        StockBrocker broker = StockBrocker.selectStockBroker("Nemo");

        assertThat(broker).isInstanceOf(NemoDriver.class);
    }

    @Test
    void Kiwer로그인_동작_확인() {
        StockBrocker broker = new KiwerDriver(); // 아직 구현 안 된 상태
        broker.login("username", "1234");
    }

    @Test
    void Kiwer로그인시_ID가_null이면_예외발생() {
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
    void Nemo로그인시_ID가_null이면_예외발생() {
        StockBrocker broker = new NemoDriver(); // 구현 예정

        assertThatThrownBy(() -> broker.login(null, "1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID");
    }
}
