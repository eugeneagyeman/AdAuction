package login;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LoginTest {
    private static Login login;

    @BeforeEach
    public void setupTest() {
        LoginTest.login=new Login();
    }

    @Test
    @DisplayName("Test login")
    public void loginTest() {

        //test default account
        assertNull(login.login("admin", "Admin1999", "admin"));
        assertNotNull(login.login("admin", "Admin2020", "admin"));

        //test partition & boundary
        assertNull(login.addUser("","123","admin"));
        assertNull(login.addUser("user","","admin"));

        login.addUser("admin0","A12345678","admin");
        assertNotNull (login.login("admin0","A12345678","admin"));
        assertNull (login.login("admin0","B12345678","admin"));

        login.addUser("admin1","A123456789123456","admin");
        assertNotNull (login.login("admin1","A123456789123456","admin"));
        assertNull (login.login("admin0","A123456789123456","admin"));

        login.addUser("admin2","A123456789123456","admin");
        assertNotNull (login.login("admin2","A123456789123456","admin"));

        login.addUser("admin3","Aabcdefghijklmnop","admin");
        assertNull (login.login("admin3","Aabcdefghijklmnop","admin"));

        login.addUser("admin4","123456789","admin");
        assertNull (login.login("admin4","123456789","admin"));

        login.addUser("admin5","a12345678","admin");
        assertNull (login.login("admin5","a12345678","admin"));
    };

}
