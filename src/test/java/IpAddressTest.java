import com.pl.masterthesis.models.IpAddress;
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

            Assert.assertEquals(3232235777l, ipAddress.getAddress());
        } catch (WrongIpAddressFormatException e) {
            fail();
        }
    }

    @Test
    public void testValidShortArrayAddress() {
        try {
            IpAddress ipAddress = new IpAddress(new int[]{192, 168, 1, 1});

            Assert.assertEquals("192.168.1.1", ipAddress.getAddressAsString());
        } catch (WrongIpAddressFormatException e) {
            fail();
        }

    }

    @Test
    public void testValidatorForStringAddress() {
        try {
            IpAddress ipAddress = new IpAddress("1.1.1.1");
            Method method = ipAddress.getClass().getDeclaredMethod("validateGivenAddress", String.class, int.class);
            method.setAccessible(true);
            String validationMessage = (String) method.invoke(ipAddress, "4.8.545.1", 29);

            Assert.assertEquals("Adres 4.8.545.1 nie może zostać poprawnie przekonwertowany.", validationMessage);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | WrongIpAddressFormatException e) {
            fail();
        }
    }

    @Test
    public void testAddressPool() {
        try {
            IpAddress networkAddress = new IpAddress("192.168.1.0", 29);
            IpAddress testAddress1 = new IpAddress("192.168.1.3", 29);
            IpAddress testAddress2 = new IpAddress("192.168.1.5", 29);
            IpAddress testAddress3 = new IpAddress("192.168.1.9", 29);

            Assert.assertTrue(networkAddress.containsAddress(testAddress1));
            Assert.assertTrue(networkAddress.containsAddress(testAddress2));
            Assert.assertFalse(networkAddress.containsAddress(testAddress3));
        } catch (WrongIpAddressFormatException e) {
            fail();
        }
    }
}
