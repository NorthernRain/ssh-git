package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author LeafDust
 * @create 2019-09-01 22:38
 */
public class WebServer {
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public WebServer() {
        try {
            serverSocket = new ServerSocket(9600);
            //创建定长线程池
            executorService = Executors.newFixedThreadPool(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                executorService.execute(clientHandler);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.start();
    }
}
