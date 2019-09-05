package com.webserver.http;

import com.webserver.exception.EmptyRequestException;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LeafDust
 * @create 2019-09-01 22:47
 */
public class HttpRequest {
    private Socket socket;
    private InputStream in;

    //请求行相关
    private String method;
    private String uri;
    private String protocol;
    private String requestURI;
    private String queryString;
    private Map<String, String> parameter = new HashMap<>();
    //消息头相关
    private Map<String, String> header = new HashMap<>();


    public HttpRequest(Socket socket) throws EmptyRequestException {
        try {
            this.socket = socket;
            in = socket.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        parseRequest();
        pareHeader();
        parseContent();
    }

    //解析请求行
    private void parseRequest() throws EmptyRequestException {
        String line = readLine();
        if ("".equals(line)) {
            throw new EmptyRequestException("EmptyRequestException!");
        } else {
            String[] requestInfo = line.split("\\s");
            method = requestInfo[0];
            uri = requestInfo[1];
            protocol = requestInfo[2];
            parseURI();
        }
    }

    //针对业务和非业务进行二次解析
    private void parseURI() {
        int index = uri.indexOf("?");
        if (index != -1) {
            requestURI = uri.substring(0, index);
            try {
                queryString = URLDecoder.decode(uri.substring(index + 1), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            putParameter();
        } else {
            requestURI = uri;
        }
        // System.out.println(requestURI);

        //System.out.println("query" + queryString);
    }

    //解析消息头
    private void pareHeader() {
        String line = null;
        while (!"".equals(line = readLine())) {
            String[] headers = line.split(": ");
            if (headers.length > 1) {
                header.put(headers[0], headers[1]);
            } else
                header.put(headers[0], null);
        }
        System.out.println(header);
    }

    //解析消息正文
    private void parseContent() {
        if (header.containsKey("Content-Length")) {
            int length = Integer.parseInt(header.get("Content-Length"));
            if ("application/x-www-form-urlencoded".equals(header.get("Content-Type"))) {
                byte[] data = new byte[length];
                try {
                    in.read(data);
                    queryString = URLDecoder.decode(new String(data), "UTF-8");
                    putParameter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //readLine
    private String readLine() {
        int d = -1;
        char c = 'a';
        StringBuilder builder = new StringBuilder();
        try {
            while ((d = in.read()) != -1) {
                if (c == 13 && (char) d == 10) {
                    break;
                }
                c = (char) d;
                builder.append(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString().trim();
    }

    //putParameter
    private void putParameter() {
        String[] paras = queryString.split("&");
        System.out.println(Arrays.toString(paras));
        for (String para : paras) {
            String[] paraInfo = para.split("=");
            //System.out.println("info+" + Arrays.toString(paraInfo));
            if (paraInfo.length > 1) {
                parameter.put(paraInfo[0], paraInfo[1]);
            } else parameter.put(paraInfo[0], null);
        }
        System.out.println("par" + parameter);
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String key) {
        return parameter.get(key);
    }

    public String getHeader(String key) {
        return header.get(key);
    }
}
