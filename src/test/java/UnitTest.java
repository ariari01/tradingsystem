import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitTest {
    @Mock
    KiwerAPI kiwerAPI;

    @Mock
    NemoAPI nemoAPI;




    @Test
    void checkCurrentPrice() {
        kiwerAPI=new KiwerAPI();
        //doNothing().when(kiwerAPI).login(anyString(), anyString());

        int actual=kiwerAPI.currentPrice("S&P500");
        assertNotNull(actual);

    }

    @Test
    void checkCurrentPrice2() throws InterruptedException {
        nemoAPI=new NemoAPI();

        int actual=nemoAPI.getMarketPrice("S&P500",1);
        assertNotNull(actual);

    }

}