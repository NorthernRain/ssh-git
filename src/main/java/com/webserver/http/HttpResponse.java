package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LeafDust
 * @create 2019-09-02 21:50
 */
public class HttpResponse {
    private Socket socket;
    private OutputStream out;
    //状态行相关
    private int statusCode;
    private String statusReason;
    private Map<String, String> header = new HashMap<>();
    //响应正文相关
    private File file;

    public HttpResponse(Socket socket) {
        try {
            this.socket = socket;
            out = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void respond() {
        respondStatus();
        respondHeader();
        respondContent();
    }

    //响应状态行
    private void respondStatus() {
        writeOut("HTTP/1.1 " + statusCode + " " + statusReason);
    }

    //响应响应头 Content-Type  Content-Length
    private void respondHeader() {
        try {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                writeOut(entry.getKey() + entry.getValue());
            }
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //相应正文
    private void respondContent() {
        try (FileInputStream fis = new FileInputStream(file)) {
            int len = -1;
            byte[] data = new byte[10 * 1024];
            while ((len = fis.read(data)) != -1) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //writeOut
    private void writeOut(String string) {
        try {
            out.write(string.getBytes("ISO-8859-1"));
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFile(File file, int statusCode, String statusReason) {
        this.file = file;
        String ext = file.getName().substring(file.getName().indexOf(".") + 1);
        header.put("Content-Type: ", HttpContext.getMimeType(ext));
        header.put("Content-Length: ", file.length() + "");
        this.statusCode = statusCode;
        this.statusReason = statusReason;
    }
}
