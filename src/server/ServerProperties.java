package server;

import constants.JobConstants;
import constants.WorldConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import tools.EncodingDetect;
import tools.FileoutputUtil;

/**
 *
 * @author Pungin
 */
public class ServerProperties {

    private static final Properties props = new Properties();

    /**
     * 获取配置文件名.
     * @return 配置文件名.
     */
    public static String getPath() {
        return "./conf/settings.ini";
    }

    /**
     * 加载配置文件。配置文件名为 getPath();
     */
    public static void loadProperties() {
        try {
            InputStream in = new FileInputStream(getPath());
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, EncodingDetect.getJavaEncode(getPath())));
            props.load(bf);
            bf.close();
        } catch (IOException ex) {
            System.out.println("讀取\"" + getPath() + "\"檔案失敗 " + ex);
        }
    }

    public static void setProperty(String prop, String newInf) {
        props.setProperty(prop, newInf);
    }

    public static void setProperty(String prop, boolean newInf) {
        props.setProperty(prop, String.valueOf(newInf));
    }

    public static void setProperty(String prop, byte newInf) {
        props.setProperty(prop, String.valueOf(newInf));
    }

    public static void setProperty(String prop, short newInf) {
        props.setProperty(prop, String.valueOf(newInf));
    }

    public static void setProperty(String prop, int newInf) {
        props.setProperty(prop, String.valueOf(newInf));
    }

    public static void setProperty(String prop, long newInf) {
        props.setProperty(prop, String.valueOf(newInf));
    }

    public static void removeProperty(String prop) {
        props.remove(prop);
    }

    public static String getProperty(String s, String def) {
        return props.getProperty(s, def);
    }

    public static boolean getProperty(String s, boolean def) {
        return getProperty(s, def ? "true" : "false").equalsIgnoreCase("true");
    }

    public static byte getProperty(String s, byte def) {
        String property = props.getProperty(s);
        if (property != null) {
            return Byte.parseByte(property);
        }
        return def;
    }

    public static short getProperty(String s, short def) {
        String property = props.getProperty(s);
        if (property != null) {
            return Short.parseShort(property);
        }
        return def;
    }

    public static int getProperty(String s, int def) {
        String property = props.getProperty(s);
        if (property != null) {
            return Integer.parseInt(property);
        }
        return def;
    }

    public static long getProperty(String s, long def) {
        String property = props.getProperty(s);
        if (property != null) {
            return Long.parseLong(property);
        }
        return def;
    }

    static {
        loadProperties();
    }
}
