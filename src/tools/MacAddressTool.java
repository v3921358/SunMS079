/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * @author wubin
 */
public class MacAddressTool {

    public static String getMacAddress(boolean ipAddress) {
        String macs = null;
        String localip = null;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();

            boolean finded = false;
            while ((netInterfaces.hasMoreElements()) && (!finded)) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) address.nextElement();
                    String ip = inetAddress.getHostAddress();
                    //if ((ip.indexOf(":") != -1) || (ip.startsWith("221.231.")) || (ip.equalsIgnoreCase("127.0.0.1"))) {
                   //     continue;
                  //  }
                    if ((!inetAddress.isSiteLocalAddress()) && (!inetAddress.isLoopbackAddress())) {
                        localip = inetAddress.getHostAddress();
                        byte[] mac = ni.getHardwareAddress();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mac.length; i++) {
                            if (i != 0) {
                                sb.append("-");
                            }
                            String str = Integer.toHexString(mac[i] & 0xFF);
                            sb.append(str.length() == 1 ? new StringBuilder().append(0).append(str).toString() : str);
                        }
                        macs = sb.toString().toUpperCase();

                        finded = true;
                        break;
                    }
                    if ((inetAddress.isSiteLocalAddress()) && (!inetAddress.isLoopbackAddress())) {
                        localip = inetAddress.getHostAddress();
                        byte[] mac = ni.getHardwareAddress();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < mac.length; i++) {
                            if (i != 0) {
                                sb.append("-");
                            }
                            String str = Integer.toHexString(mac[i] & 0xFF);
                            sb.append(str.length() == 1 ? new StringBuilder().append(0).append(str).toString() : str);
                        }
                        macs = sb.toString().toUpperCase();

                        finded = true;
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress ? localip : macs;
    }
}
