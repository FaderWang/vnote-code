package com.fader.vnote.net;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * @author FaderW
 * 2019/10/24
 */

public class URISimple {


    /**
     * https://www.cnblogs.com/throwable/p/9740425.html
     * @throws URISyntaxException
     * @throws MalformedURLException
     */

    static void uriAnalyze() throws URISyntaxException, MalformedURLException {
        //结构 scheme:scheme specific part
        URI absolute = new URI("http://localhost:8080/index.html");
        URI relative = new URI("/hello.html");
        URI uri = absolute.resolve(relative);
        System.out.println(uri.toString());

    }

    static void urlAnalyze() {
        // protocol://userInfo@host:port/path?query#fragment

    }

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
//        URL url = new URL("http://www.http.com");
//        System.out.println(url);
//        URI uri = url.toURI();
//        System.out.println(uri);
        String str1 = "str";
        String str2 = "ing";

        String str3 = "str" + "ing";//常量池中的对象
        String str5 = "string";
        String str4 = str1 + str2;
        System.out.println(str5 == str4);
//        ByteBuffer.allocateDirect();

//        URI uri = null;
//        try {
//            uri = new URI("www.baidu.com");
//        } catch (URISyntaxException e) {
//            System.out.println("true");
//            e.printStackTrace();
//        }
//        try {
////            uriAnalyze();
////            URI uri = URI.create("www.baidu.com");
//            System.out.println(uri);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
