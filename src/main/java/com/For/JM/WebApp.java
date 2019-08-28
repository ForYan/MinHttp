package com.For.JM;
import com.For.JM.http.Controller;
import com.For.JM.http.Request;
import com.For.JM.http.getClassLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: For
 * Date: 2019/8/14
 * Time: 16:04
 */

public class WebApp {
    static Map<String,String> urlToName=new HashMap<>();
    static Map<String,String> nameToClassName=new HashMap<>();
    private final getClassLoader classLoader= new getClassLoader();
    //解析xml文件
    public static WebApp parseXML() throws DocumentException, IOException {
        WebApp webApp=new WebApp();
        SAXReader reader=new SAXReader();//解析xml文件
        URL url = new File("E:\\bitekeji\\project\\JM\\webapp\\WEB-INF\\web.xml").toURI().toURL();
        Document document=reader.read(url);//找资源文件
        Element root=document.getRootElement();//获取根结点
        for (Iterator<Element> it = root.elementIterator(); it.hasNext() ; ){
            Element element=it.next();
            switch(element.getName()){
                case "controller":{
                    String name=element.element("name").getText();//取得中间的类名
                    String className=element.element("class").getText();//获取类的全限定名
                    webApp.nameToClassName.put(name,className);
                    break;
                }
                case "mapping":{
                    String name=element.element("name").getText();//获取类名
                    String urlPattern=element.element("url-pattern").getText();//获取url
                    webApp.urlToName.put(urlPattern,name);
                    break;
                }
                default:
                    throw new IOException("xml文件错误");
            }
        }
        return webApp;
    }

    public Controller findController(Request request) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        String name=findName(request.getUrl());//根据url得到controller名称
        if (name==null){
            return null;
        }
        String className=findClassName(name);//根据类名称得到class名称

        if (className==null){
            return null;
        }
        Controller controller=findInstance(className);//根据class名称得到controller
        return controller;//返回该类的对象
    }

    private Controller findInstance(String className) throws IOException {
        try{
        Class<?> cls=classLoader.loadClass(className);//利用反射获取反射对象
        return  (Controller) cls.newInstance();//new一个对象
    }catch(IllegalAccessException|InstantiationException |ClassNotFoundException e){
            throw new IOException(e);
        }
    }

    private String findClassName(String name) {
        return nameToClassName.get(name);
    }

    private String findName(String url) {
        return urlToName.get(url);
    }
}
