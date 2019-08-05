package com.fader.vnote.io.socket;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FaderW
 * 2019/8/5
 */

public class HttpUtils {

    @Data
    public static class Request {

        private String method;

        private String path;

        private Map<String, String> headers = new HashMap<>(16);

        private String msg;

        public void setHeader(String key, String value) {
            this.headers.put(key, value);
        }

        public String getHeader(String key, String defaultValue) {
            return this.headers.getOrDefault(key, defaultValue);
        }
    }

    public static void decodeRequest(Request request, InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        //读取请求行
        String reqLine = reader.readLine();
        String[] reqLineArr = reqLine.split(" ");
        request.setMethod(reqLineArr[0]);
        request.setPath(reqLineArr[1]);

        String headerLine = reader.readLine();
        //读取header
        while (StringUtils.isNotBlank(headerLine)) {
            String[] kv = headerLine.split(":");
            request.setHeader(kv[0], kv[1]);
            headerLine = reader.readLine();
        }

        //读取参数
        String contentLength = request.getHeader("Content-length", "0");
        if (!"0".equals(contentLength)) {
            char[] chars = new char[Integer.parseInt(contentLength)];
            reader.read(chars);
            request.setMsg(new String(chars));
        }
    }

}
