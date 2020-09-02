package test;

import tools.MapleAESOFB;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class TestMain {
    public static void main (String [] args) {
        final byte ivRecv[] = {70, 114, 122, 82};
        final byte ivSend[] = {82, 48, 120, 115};
        ivRecv[3] = (byte) (Math.random() * 255);
        ivSend[3] = (byte) (Math.random() * 255);

        try {
            int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
            System.out.print (maxKeyLen);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace ();
        }
        MapleAESOFB ivSnd = new MapleAESOFB(ivSend, (short) (0xFFFF - 79)); // Sent Cypher
        MapleAESOFB ivRcv = new MapleAESOFB(ivRecv, (short) 79); // Recv Cypher

        try {
            int x = System.in.read();
        } catch (IOException ex) {

        }
    }
}
