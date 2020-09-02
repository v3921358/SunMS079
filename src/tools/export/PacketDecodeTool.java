/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.export;

import java.io.ByteArrayOutputStream;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;

/**
 *
 * @author Itzik
 */
public class PacketDecodeTool {

    //public static final String[] hex = {"0", "|", "1", "}", "2", "~", "3",  "4",  "5", "?", "6", "7", "8", "9", "@", "?", "A",  "B", "?", "C",  "D", "?", "E", "?½", "F",  "G",  "H",  "I",  "J",  "K",  "L",  "M", "?", "N",  "O", "?¡", "P",  "Q", "?", "R", "?¾", "S", "?¸", "T", "?¿½?, "U", " ", "V", "!", "W", "#", "X", "$", "Y", "%", "Z", "^", "[", "&", "'\'", "'", "]", "(", "^", ")", "_", "*", "`", "+", "a", ",", "b", "-", "c", ".", "d", "/", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{"};

    /*
     * Ex.
     * 0A 00 31 30 32 30 33 30 34 30 35 30 to mapleasciistring
     */
    public static void main(String[] args) {
        String packet = "0A 00 31 30 40 30 32 30 33 30 34 30 35 30";
        byte[] bytes = getByteArrayFromHexString(packet);
        final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream(bytes));
        byte next;
        short next_short;
        int next_int;
        long next_long;
        for (int length = 0; length < bytes.length; length++) {
            if (slea.readInt() % 100 < 10 && slea.available() >= 4) {
                System.out.println(slea.readLastInt());
            } else {
                slea.unReadInt();
                System.out.println(slea.readByte());
            }
        }
        //System.out.println(slea.readMapleAsciiString());
    }

    public static byte[] getByteArrayFromHexString(final String hex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int nexti = 0;
        int nextb = 0;
        boolean highoc = true;
        outer:
        for (;;) {
            int number = -1;
            while (number == -1) {
                if (nexti == hex.length()) {
                    break outer;
                }
                char chr = hex.charAt(nexti);
                if (chr >= '0' && chr <= '9') {
                    number = chr - '0';
                } else if (chr >= 'a' && chr <= 'f') {
                    number = chr - 'a' + 10;
                } else if (chr >= 'A' && chr <= 'F') {
                    number = chr - 'A' + 10;
                } else {
                    number = -1;
                }
                nexti++;
            }
            if (highoc) {
                nextb = number << 4;
                highoc = false;
            } else {
                nextb |= number;
                highoc = true;
                baos.write(nextb);
            }
        }
        return baos.toByteArray();
    }
}
