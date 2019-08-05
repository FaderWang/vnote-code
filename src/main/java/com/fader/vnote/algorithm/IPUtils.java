package com.fader.vnote.algorithm;

/**
 * @author FaderW
 * 2019/4/16
 */

public class IPUtils {

    public static int ip2Int(String ip) {
        if (null == ip) {
            throw new NullPointerException();
        }
        String[] array = ip.split("\\.");
        int length = array.length;
        int rs = 0;
        for (int i = 0; i < length; i++) {
            int splices = Integer.parseInt(array[i]) << 8 * (length-1-i);
            rs = rs | splices;
        }

        return rs;
    }


    public static String toIpString(int ipValue) {
        String[] array = new String[4];
        for (int i = 0; i < array.length; i++) {
            int pos = 8 * i;
            int splice = ipValue & (255 << pos);
            array[array.length-1-i] = String.valueOf(splice >>> pos);

        }
        for (String s : array) {
            System.out.println(s);
        }

        return String.join(".", array);

    }

    public static void main(String[] args) {
        String ip = "192.168.5.1";
        int ipInt = ip2Int(ip);
        System.out.println(ipInt);
        System.out.println(toIpString(ipInt));
    }
}
