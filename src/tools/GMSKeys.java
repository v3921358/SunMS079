/*
 * This file was designed for Titanium.
 * Do not redistribute without explicit permission from the
 * developer(s).
 */
package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class was directly translated from the MapleShark GitHub. Credits to
 * Diamondo25.
 *
 */
public class GMSKeys {

    private static final HashMap<Short, byte[]> MapleStoryGlobalKeys = new HashMap<>();

    private static void InitByContents(String pContents) {
        String[] lines = pContents.split("\r\n");
        for (int i = 0; i < lines.length; i += 2) {
            short version = Short.parseShort(lines[i]);
            String tmpkey = lines[i + 1];
            byte[] realkey = new byte[8];
            int tmp = 0;
            for (int j = 0; j < 4 * 8 * 2; j += 4 * 2) {
                realkey[tmp++] = (byte) Integer.parseInt(tmpkey.charAt(j) + "" + tmpkey.charAt(j + 1), 16);
            }
            MapleStoryGlobalKeys.put(version, realkey);
        }
    }

    public static void Initialize() throws FileNotFoundException {
        try {
            File f = new File("./noupdate.txt");
            if (f.exists()) {
                throw new Exception(); // Trigger offline file loading
            }
            URL req = new URL("http://direct.craftnet.nl/app_updates/get_keys.php");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(req.openStream()))) {
                String line = "";
                StringBuilder sb = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    sb.append(line).append("\r\n");
                }
                InitByContents(sb.toString());

            }
        } catch (Exception ex) {
            // Fail, w/e
            MapleStoryGlobalKeys.clear();
            File f = new File("./cached_keys.txt");
            if (f.exists()) {
                Scanner s = new Scanner(new FileReader("./cached_keys.txt"));
                StringBuilder sb = new StringBuilder();
                while (s.hasNext()) {
                    sb.append(s.nextLine()).append("\r\n");
                }
                InitByContents(sb.toString());
            } else {
                System.out.println("Unable to load GMS Keys, because there were no cached keys stored and I failed retrieving them from the webserver! D:\r\nYou might want to check your internet connection and see if you can access http://direct.craftnet.nl/ directly.");
                System.exit(1);
            }
        }

        MapleStoryGlobalKeys.put((short) 118, new byte[]{
            0x5A, // Full key's lost
            0x22,
            (byte) 0xFB,
            (byte) 0xD1,
            (byte) 0x8F,
            (byte) 0x93,
            (byte) 0xCD,
            (byte) 0xE6,});
    }

    static {
        try {
            Initialize();
        } catch (FileNotFoundException ex) {
        }
    }

    public static byte[] GetKeyForVersion(short pVersion) {
        // Get first version known
        for (; pVersion > 0; pVersion--) {
            if (MapleStoryGlobalKeys.containsKey(pVersion)) {
                byte[] key = MapleStoryGlobalKeys.get(pVersion);
                byte[] ret = new byte[32];
                for (int i = 0; i < 8; i++) {
                    ret[i * 4] = key[i];
                }
                return ret;
            }
        }
        return null;
    }
}
