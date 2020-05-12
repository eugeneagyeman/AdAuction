package login;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Encryption {

    private static Cipher cipher;
    private static SecretKey key;

    public Encryption() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        key = KeyGenerator.getInstance("AES").generateKey();
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }

    public byte[] encrypt(String password) throws BadPaddingException, IllegalBlockSizeException {
        return cipher.doFinal(password.getBytes());
    }

}


