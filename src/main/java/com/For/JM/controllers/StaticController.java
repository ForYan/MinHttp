package com.For.JM.controllers;

import com.For.JM.http.Controller;
import com.For.JM.http.Request;
import com.For.JM.http.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: For
 * Date: 2019/8/14
 * Time: 15:50
 */
public class StaticController extends Controller {
    //读环境变量
    private static final String HOME=System.getenv("JM_HOME");

    public void doGet(Request request, Response response) throws IOException {
        //根据url找到文件路径
        String filename=getFileName(request.getUrl());
        //根据文件名的后缀决定content-type
        String suffix=getSuffix(filename);
        String contentType=getContentType(suffix);//根据后缀名得到类型
        response.setContentType(contentType);
        //打开这个文件，把文件的所有内容作为response的body发送
        InputStream is=new FileInputStream(filename);
        byte[] buf=new byte[1024];
        int len=0;
        while((len=is.read(buf))!=-1){//等于-1的时候就读完了
            response.write(buf,0,len);//读到多少写多少
        }
    }

    private String getContentType(String suffix) {
        String contentType=CONTENT_TYPE.get(suffix);
        if (contentType==null){
            //默认是text/html文件
            contentType="text/html";
        }
        return contentType;
    }

    private final Map<String,String> CONTENT_TYPE=new HashMap<String,String>(){
        //匿名类+构造代码块
        {
            put("html","text/html");
            put("css","text/css");
            put("js","application/js");
        }
    };

    private String getSuffix(String filename) {
        int index=filename.lastIndexOf(".");//从后向前查找子字符串的位置，返回对应的下标，查不到，就返回-1
        if (index==-1){
            //没找到
            return null;
        }else{
            return filename.substring(index+1);//取得后缀
        }
    }

    private String getFileName(String url) {
        //如果是根路径，就特殊处理
        if (url.equals("/")){
            url="/index.html";
        }
        String fileName = HOME+File.separator+"webapp"+url.replace("/",File.separator);
        return fileName;
    }
}
