package com.webserver.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author LeafDust
 * @create 2019-09-02 23:50
 */
public class HttpContext {
    private static Map<String, String> mimeMapping = new HashMap<>();

    static {
        initMimeMapping();
    }

    private static void initMimeMapping() {
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read("./conf/web.xml");
            Element root = document.getRootElement();
            List<Element> mimes = root.elements("mime-mapping");
            for (Element e : mimes) {
                mimeMapping.put(e.elementTextTrim("extension"), e.elementTextTrim("mime-type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String key){
        return mimeMapping.get(key);
    }
}
