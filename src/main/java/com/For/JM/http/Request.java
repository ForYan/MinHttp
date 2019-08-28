package com.For.JM.http;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: For
 * Date: 2019/8/14
 * Time: 15:49
 */
public class Request {
    private String url;
    private String protocol;
    private String method;
    public Map<String,String> requestParams=new HashMap<>();//存放请求参数
    private Map<String,String> headers=new HashMap<>();//存放请求头
    public Map<String,String> params=new HashMap<>();


    public static Request parse(InputStream is) throws IOException {
        //InputStream是字节流的输入，InputStreamReader是字节流和字符流之间的转换，BufferedReader是格式化输出
        BufferedReader reader=new BufferedReader(new InputStreamReader(is));//将字节流通过InputStreamReader转为字符输入流
        Request request=new Request();//创建请求对象
        parseRequestLine(reader,request);//解析请求行
        parseRequestHeader(reader,request);//解析请求参数
        //只有当method是post的时候，才会解析请求体
        if (request.method.toUpperCase().equals("POST")){
            parseRequestBody(reader,request);
        }
        return request;
    }

    private static void parseRequestBody(BufferedReader reader, Request request) throws IOException {
        //取得文章内容长度
        int len=Integer.parseInt(request.headers.get("Content-Length"));
        char[] buf=new char[len];
        reader.read(buf,0,len);//将内容读到buf里面
        request.setRequestParams(String.valueOf(buf));//将buf里面的内容变为String
    }

    private void setRequestParams(String s) {//请求体分割
    for(String kv:s.split("&")){
        String[] kvs= kv.split("=");
        String key=kvs[0];
        String value=kvs[1];
        params.put(key,value);
        }
    }

    private static void parseRequestLine(BufferedReader reader,Request request) throws IOException {
        String line=reader.readLine();//读一个请求行
        if (line==null){
            throw new IOException("读到空行");
        }
        String[] segments=line.split(" ");//这三个之间以空格进行分割
        request.setMethod(segments[0]);//第一个是方法
        request.setUrl(segments[1]);//第二个是url
        request.setProtocol(segments[2]);//第三个版本号
    }

    private void setMethod(String method) throws IOException {
        //将method放在属性里面
        this.method=method.toUpperCase();
        if (method.equals("POST")||method.equals("GET")){
            return;
        }else{
            throw new IOException("不支持的方法");
        }
    }

    private void setUrl(String url) throws UnsupportedEncodingException {
        //判断url中有没有queryString,?后面的
        String[] minsegments = url.split("\\?");
        this.url = URLDecoder.decode(minsegments[0], "UTF-8");
        if (minsegments.length > 1) {
            //说明存在后面的键值对，需要进一步处理（动态页面用）
            setRequestParam(minsegments[1]);
        }
    }

    //?后面的，第一行的键值对
    private void setRequestParam(String param) throws UnsupportedEncodingException {
        for (String kv : param.split("&")) {

            String[] segments = kv.split("=");
            String key = segments[0];
            String value = "";//因为有一种可能就是value没有值
            if (segments.length > 1) {
                value = URLDecoder.decode(segments[1],"UTF-8");
                requestParams.put(key, value);//将数据存到Map里面
            }
        }
    }

    //版本号http/1.1
    private void setProtocol(String protocol) throws IOException {
        if (protocol.toUpperCase().startsWith("HTTP")){
            this.protocol=protocol.toUpperCase();
        }else{
            throw new IOException("错误的版本号");
        }
    }
//以下是提供属性的get方法
    public String getMethod() {
        return this.method;
    }

    public String getUrl() {
        return this.url;
    }

    public String getProtocol(){
        return this.protocol;
    }

    public Map<String,String> getHeaders(){
        return this.headers;
    }

    public Map<String,String> getRequestParams(){
        return this.requestParams;
    }

    private static void parseRequestHeader(BufferedReader reader,Request request) throws IOException {
        //按行解析，\r\n结束
        String line;
        //读到空标识InputStream结束，第二种表示\r\n,不会被读进来，所以去掉trim之后，长度为0表示已经读完了。
        while((line=reader.readLine())!=null&&line.trim().length()!=0){
            String[] segment=line.split(":");
            String key=segment[0].trim();
            String value=segment[1].trim();
            request.setHeader(key,value);//存放请求头的相关参数
        }
    }

    private void setHeader(String key,String value){
        headers.put(key,value);
    }

}
