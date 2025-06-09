import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitTest {
    @Mock
    KiwerAPI kiwerAPI;

    @Mock
    NemoAPI nemoAPI;

    @Test
    void selectStockBroker() {
        KiwerDriver driver = new KiwerDriver(kiwerAPI);
        doNothing().when(kiwerAPI).login(anyString(), anyString());

        driver.login("qwe", "qwe");

        verify(kiwerAPI, times(1)).login(anyString(), anyString());
    }


    @Test
    void buyStockByKiwerDriver() {
        StockBroker kiwerDriver = new KiwerDriver(kiwerAPI);
        kiwerDriver.buy("APPL", 33, 123);
        verify(kiwerAPI, times(1)).buy(anyString(), anyInt(), anyInt());

    }

    @Test
    void buyStockByMockDriver() {
        //Arrange
        StockBroker mockDriver = new MockDriver();
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputContent));

        //Act
        mockDriver.buy("APPL", 33, 123);

        //Assert
        assertEquals("Mock Driver Buy Success", outputContent.toString());
        System.setOut(originalOut);


    }


    @Test
    void checkBuyWithIllegalArgumentByKiwerDriver() {
        StockBrocker kiwerDriver = new KiwerDriver(kiwerAPI);
        assertThrows(IllegalArgumentException.class,()-> {
            kiwerDriver.buy("APPL", -1, 123);
        });

        assertThrows(IllegalArgumentException.class,()-> {
            kiwerDriver.buy("APPL", 2, -1);
        });
        }

    @Test
    void buyStockByNemoDriver() {
        StockBroker nemoDriver = new NemoDriver(nemoAPI);
        nemoDriver.buy("APPL",33,123);
        verify(nemoAPI, times(1)).purchasingStock(anyString(), anyInt(), anyInt());
    }

    @Test
    void checkBuyWithIllegalArgumentByNemoDriver() {
        StockBrocker nemoDriver = new NemoDriver(nemoAPI);
        assertThrows(IllegalArgumentException.class,()-> {
            nemoDriver.buy("APPL", -1, 123);
        });
        assertThrows(IllegalArgumentException.class,()-> {
            nemoDriver.buy("APPL", 2, -1);
        });
    }
}
