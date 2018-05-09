import com.pl.masterthesis.utils.IpAddress;
import com.pl.masterthesis.utils.exceptions.WrongIpAddressFormatException;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.TestCase.fail;

public class IpAddressTest {
    @Test
    public void testValidStringAddress() {
        try {
            IpAddress ipAddress = new IpAddress("192.168.1.1");
            Assert.assertArrayEquals(new short[]{192, 168, 1, 1}, ipAddress.getAddress());
        } catch (WrongIpAddressFormatException e) {
            fail();
        }
    }

    @Test
    public void testValidShortArrayAddress() {
        try {
            IpAddress ipAddress = new IpAddress(new short[]{192, 168, 1, 1});
            Assert.assertEquals("192.168.1.1", ipAddress.getAddressAsString());
        } catch (WrongIpAddressFormatException e) {
            fail();
        }

    }

    @Test
    public void testValidatorForStringAddress() {
        try {
            IpAddress ipAddress = new IpAddress("1.1.1.1");
            Method method = ipAddress.getClass().getDeclaredMethod("validateGivenAddress", String.class);
            method.setAccessible(true);
            String validationMessage = (String) method.invoke(ipAddress, "4.8.545.1");
            Assert.assertEquals("Adres 4.8.545.1 nie może zostać poprawnie przekonwertowany.", validationMessage);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | WrongIpAddressFormatException e) {
            fail();
        }
    }
}
