package com.webserver.core;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.servlet.HttpServlet;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * @author LeafDust
 * @create 2019-09-01 22:42
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = new HttpRequest(socket);
            HttpResponse httpResponse = new HttpResponse(socket);
            String path = httpRequest.getRequestURI();
            if (ServletContext.servletMap.containsKey(path)) {
                HttpServlet httpServlet = ServletContext.getServlet(path);
                httpServlet.service(httpRequest, httpResponse);
            } else {
                File file = new File("./webapps" + path);
                if (file.exists()) {
                    httpResponse.setFile(file, 200, "ok");
                } else {
                    file = new File("./404.html");
                    httpResponse.setFile(file, 404, "Not Found!");
                }
            }
            httpResponse.respond();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
