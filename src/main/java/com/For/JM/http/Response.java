package com.For.JM.http;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: For
 * Time: 15:49
 */
public class Response {
    private OutputStream os;//输出流
    public Status status;//标志当前的状态
    public Map<String ,String> headers=new HashMap<>();
    private final byte[] buf=new byte[8192];
    private int off=0;//记录当前写到哪里了

    public Response(OutputStream os) {
        this.os = os;
    }

    public static Response build(OutputStream os){
        Response response=new Response(os);
        response.setServer();//HTTP服务器编号
        response.setStatus(Status.OK);//设置默认状态OK
        response.setDate();//响应时间
        response.setContentType("text/html");//响应体格式说明，为默认状态
        return response;
    }
    //首行
    private void setServer() {
        setHeader("Server","JM/1.0");
    }

    public void setStatus(Status status) {
        this.status=status;
    }

    public void setDate(){
        SimpleDateFormat sdf=new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z",Locale.ENGLISH);
        setHeader("Date",sdf.format(new Date()));
    }

    public void setContentType(String s){//设置文章类型
        setHeader("Content-Type",s+";charset=UTF-8");
    }

    public void setHeader(String key, String value) {
        headers.put(key,value);
    }
    public void flush() throws IOException {
        setHeader("Content-Length",String.valueOf(off));//设置响应体长度
        sendResponseLine();
        sendResponseHeaders();
        sendResponseBody();
    }

    private void sendResponseHeaders() throws IOException {
        for (Map.Entry<String,String>  entry : headers.entrySet()) {
            String key=entry.getKey();
            String value=entry.getValue();
            String headerLine=String.format("%s: %s\r\n",key,value);
            os.write(headerLine.getBytes("UTF-8"));
        }
        os.write("\r\n".getBytes("UTF-8"));
    }

    private void sendResponseLine() throws IOException {//响应行分为版本号和状态码
        String responseLine=String.format("HTTP/1.0 %d %s\r\n",status.getId(),status.getReason());
        os.write(responseLine.getBytes("UTF-8"));
    }

    //直接过来
    private void sendResponseBody() throws IOException {//响应正文部分
        os.write(buf);
    }

    public void print(Object o) throws UnsupportedEncodingException {
        byte[] src=o.toString().getBytes("UTF-8");
        System.arraycopy(src,0,buf,off,src.length);
        off+=src.length;//之后加到off里面
    }

    public void println(Object o)throws IOException {
        print(String.format("%s%n",o));//换行

    }

    public void printf(String format,Object...o) throws IOException {
        print(new Formatter().format(format, o));//？最终得到一个字符串
    }

    public void write(byte[] b, int i, int len) {
        System.arraycopy(b,0,buf,off,len);
        off+=len;
    }
}
