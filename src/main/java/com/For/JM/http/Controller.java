package com.For.JM.http;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: For
 * Date: 2019/8/14
 * Time: 15:47
 */
abstract public class Controller {
    public void doGet(Request request, Response response) throws IOException {
        response.setStatus(Status.Method_Not_Allowed);//不支持的方法
        response.println(Status.Method_Not_Allowed.getReason());
    }
    public void doPost(Request request, Response response) throws IOException {
        response.setStatus(Status.Method_Not_Allowed);//不支持的方法
        response.println(Status.Method_Not_Allowed.getReason());
    }
}
