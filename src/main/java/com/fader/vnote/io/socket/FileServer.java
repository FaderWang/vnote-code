package com.fader.vnote.io.socket;

import com.alibaba.fastjson.JSON;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author FaderW
 * 2019/8/5
 */

public class FileServer {

    private static final String BASE_DIR = "src/main/java/com/fader/vnote";
    private static final String HTTP_OK = "HTTP/1.1 200 OK";
    private static final Pattern ALLOWED_FILE_NAME = Pattern
            .compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
    private int port;

    public FileServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket server = new ServerSocket(port);

        while (true) {
            try {
                Socket client = server.accept();
                new ClientHandler(client).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClientHandler extends Thread {

        private Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try (InputStream in = client.getInputStream();
                 OutputStream out = client.getOutputStream()) {

                HttpUtils.Request request = new HttpUtils.Request();
                HttpUtils.decodeRequest(request, in);

                System.out.println(JSON.toJSONString(request));
                handleAndResponse(request, out);

            } catch (IOException e) {
                System.out.println("client codec error " + e.getMessage());
            }
        }

        private void handleAndResponse(HttpUtils.Request request, OutputStream out) {
            String path = request.getPath();
            if (null == path) {
                return;
            } else {
                path = BASE_DIR + path;
            }
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("file path : " + file.getAbsolutePath() + "not exists");
                return;
            }
            if (file.isDirectory()) {
                sendList(file, out);
            } else if (file.isFile()) {
                sendFile(file, out);
            }
        }

        private void sendList(File dir, OutputStream out) {
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(HTTP_OK);
            writer.println("Content-Type: " + MIME.getMime("html"));
            writer.println();

            StringBuilder buf = new StringBuilder();
            String dirPath = dir.getPath();
            buf.append("<!DOCTYPE html>\r\n");
            buf.append("<html><head><title>");
            buf.append(dirPath);
            buf.append(" 目录：");
            buf.append("</title></head><body>\r\n");
            buf.append("<h3>");
            buf.append(dirPath).append(" 目录：");
            buf.append("</h3>\r\n");
            buf.append("<ul>");
            buf.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
            for (File f : dir.listFiles()) {
                if (f.isHidden() || !f.canRead()) {
                    continue;
                }
                String path = f.getPath();
                String name = path.substring(BASE_DIR.length());
                if (!ALLOWED_FILE_NAME.matcher(f.getName()).matches()) {
                    continue;
                }
                buf.append("<li>链接：<a href=\"");
                buf.append(name);
                buf.append("\">");
                buf.append(f.getName());
                buf.append("</a></li>\r\n");
            }
            buf.append("</ul></body></html>\r\n");

            writer.write(buf.toString());
            writer.flush();
        }

        private void sendFile(File file, OutputStream out) {
            PrintWriter printWriter = new PrintWriter(out, true);
            printWriter.println(HTTP_OK);
            printWriter.println("Content-Type: " + MIME.getMime("txt"));
            printWriter.println();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String msg;
                while ((msg = reader.readLine()) != null) {
                    printWriter.println(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        try {
            new FileServer(20000).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
