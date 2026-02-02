import edu.ntnu.idi.bidata.group5.Share;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShareTest {
    @Test
    void testGetQuantity (){
        Share share = new Share(BigDecimal.valueOf(10.00), BigDecimal.valueOf(150.00));
        assertEquals(BigDecimal.valueOf(10.00), share.getQuantity());
    }
}
