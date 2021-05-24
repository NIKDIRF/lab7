package util;

import log.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.logging.LogManager;

public class SHA224CryptoModule implements CryptoModule{
    @Override
    public String hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-224");
            byte[] messageDigest = md.digest(str.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.getLogger().error("Can't encrypt string.");
            System.exit(1);
            return null;
        }
    }
}
