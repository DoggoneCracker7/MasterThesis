import com.pl.masterthesis.utils.IpAddress;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.fail;

public class IpAddressTest {
    @Test
    public void testValidStringAddress() {
        IpAddress ipAddress = new IpAddress("192.168.1.1");
        Assert.assertArrayEquals(new short[]{192, 168, 1, 1}, ipAddress.getAddress());
    }

    @Test
    public void testValidShortArrayAddress() {
        IpAddress ipAddress = new IpAddress(new short[]{192, 168, 1, 1});
        Assert.assertEquals("192.168.1.1", ipAddress.getAddressAsString());
    }

    @Test
    public void testValidatorForStringAddress() {
        IpAddress ipAddress = new IpAddress("1.1.1.1");
        try {
            Method method = ipAddress.getClass().getDeclaredMethod("validateGivenAddress", String.class);
            method.setAccessible(true);
            String validationMessage = (String) method.invoke(ipAddress, "4.8.545.1");
            Assert.assertEquals("Adres 4.8.545.1 nie może zostać poprawnie przekonwertowany.", validationMessage);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail();
        }
    }
}
