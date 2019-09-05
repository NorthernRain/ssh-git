package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;

/**
 * @author LeafDust
 * @create 2019-09-03 22:09
 */
public abstract class HttpServlet {
    public abstract void service(HttpRequest httpRequest, HttpResponse httpResponse);

    protected void setRepose(String fileName, HttpResponse httpResponse) {
        File file=new File("./webapps/myweb/"+fileName);
        httpResponse.setFile(file,200,"ok");
    }

}
