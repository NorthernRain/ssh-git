package com.webserver.core;

import com.webserver.servlet.HttpServlet;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LeafDust
 * @create 2019-09-03 23:00
 */
public class ServletContext {
    public static Map<String, HttpServlet> servletMap = new HashMap<>();

    static {
        initServletMap();
    }

    private static void initServletMap() {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read("./conf/servlet.xml");
            Element root = document.getRootElement();
            List<Element> servlets = root.elements("servlet-Mapping");
            for (Element e : servlets) {
                Class cls = Class.forName(e.elementTextTrim("para-value"));
                Object obj = cls.newInstance();
                servletMap.put(e.elementTextTrim("para-key"), (HttpServlet) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServlet getServlet(String key) {
        return servletMap.get(key);
    }
}
