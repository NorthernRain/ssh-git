package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @author LeafDust
 * @create 2019-09-03 22:29
 */
public class RegServlet extends HttpServlet {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        try (RandomAccessFile raf = new RandomAccessFile("./user.dat", "rw")) {
            String[] regInfo = {httpRequest.getParameter("username"), httpRequest.getParameter("nickname"),
                    httpRequest.getParameter("password"), httpRequest.getParameter("age")};
            byte[] data = new byte[32];
            for (int i = 0; i < raf.length(); i++) {
                raf.seek(100 * i);
                raf.read(data);
                if (new String(data, "UTF-8").trim().equals(regInfo[0])) {
                    setRepose("have_user.html", httpResponse);
                    return;
                }
            }

            raf.seek(raf.length());
            for (int i = 0; i < regInfo.length; i++) {
                if (i < regInfo.length - 1) {
                    data = Arrays.copyOf(regInfo[i].getBytes("UTF-8"), 32);
                    raf.write(data);
                } else raf.writeInt(Integer.parseInt(regInfo[regInfo.length - 1]));
            }
            setRepose("reg_success.html", httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
