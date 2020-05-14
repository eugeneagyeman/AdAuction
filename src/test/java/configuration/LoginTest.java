package configuration;

import login.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LoginTest {
    private static Login login;

    @BeforeEach
    public void setupTest() throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        LoginTest.login=new Login();
    }

    @Test
    public void register_1() throws BadPaddingException, IllegalBlockSizeException {
        //test default account
        assertNull(login.login("admin", "Admin1999", "admin"));
        assertNotNull(login.login("admin", "Admin2020", "admin"));
    }

    @Test
    public void register_2() throws BadPaddingException, IllegalBlockSizeException {
        //test partition & boundary
        assertNull(login.addUser("","123","admin"));
        assertNull(login.addUser("user","","admin"));
    }

    @Test
    public void register_3() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin0","A12345678","admin");
        assertNotNull (login.login("admin0","A12345678","admin"));
        assertNull (login.login("admin0","B12345678","admin"));
    }

    @Test
    public void register_4() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin0","A12345678","admin");
        assertNotNull (login.login("admin0","A12345678","admin"));
        assertNull (login.login("admin0","B12345678","admin"));
    }

    @Test
    public void register_5() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin1","A123456789123456","admin");
        assertNotNull (login.login("admin1","A123456789123456","admin"));
        assertNull (login.login("admin0","A123456789123456","admin"));
    }

    @Test
    public void register_6() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin1","A123456789123456","admin");
        assertNotNull (login.login("admin1","A123456789123456","admin"));
        assertNull (login.login("admin0","A123456789123456","admin"));
    }
    @Test
    public void register_7() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin2","A123456789123456","admin");
        assertNotNull (login.login("admin2","A123456789123456","admin"));
    }

    @Test
    public void register_8() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin1","A123456789123456","admin");
        assertNotNull (login.login("admin1","A123456789123456","admin"));
        assertNull (login.login("admin0","A123456789123456","admin"));
    }

    @Test
    public void register_9() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin1","A123456789123456","admin");
        assertNotNull (login.login("admin1","A123456789123456","admin"));
        assertNull (login.login("admin0","A123456789123456","admin"));
    }

    @Test
    public void register_10() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin3","Aabcdefghijklmnop","admin");
        assertNull (login.login("admin3","Aabcdefghijklmnop","admin"));
    }
    @Test
    public void register_11() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin4","123456789","admin");
        assertNull (login.login("admin4","123456789","admin"));
    }
    @Test
    public void register_12() throws BadPaddingException, IllegalBlockSizeException {
        login.addUser("admin5","a12345678","admin");
        assertNull (login.login("admin5","a12345678","admin"));
    }

    @Test
    public void register_13() throws BadPaddingException, IllegalBlockSizeException {
            assertNull(login.login("admin0", "A123456789123456", "admin"));
            assertNull(login.login("admin", "Admin1999", "admin"));

        }
    }
