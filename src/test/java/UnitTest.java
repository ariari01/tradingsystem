import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UnitTest {

    @Test
    void 키워를_선택하면_KiwerAPI를_반환한다() {
        Object o = StockSelector.selectStockBrocker("kiwer");

        assertThat(o).isInstanceOf(KiwerAPI.class);
    }

    @Test
    void Nemo를_선택하면_NemoAPI를_반환한다() {
        Object o = StockSelector.selectStockBrocker("Nemo");

        assertThat(o).isInstanceOf(KiwerAPI.class);
    }
}
