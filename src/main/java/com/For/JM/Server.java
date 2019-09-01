package com.For.JM;
import com.For.JM.http.*;
import com.For.JM.controllers.StaticController;
import org.dom4j.DocumentException;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: For
 * Time: 15:37
 */
public class Server {
    private static final String HOME = System.getenv("JM_HOME");
    private final Controller staticController = new StaticController();
    private final WebApp webApp = WebApp.parseXML();

    public Server() throws DocumentException, IOException {
    }

    public void run(int port) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        ServerSocket serverSocket = new ServerSocket(port);//监听port这个端口
        while (true) {
            Socket socket = serverSocket.accept();//建立连接
            pool.execute(new Runnable() {
                @Override
                public void run() {
            try {

                Request request = Request.parse(socket.getInputStream());//解析一个请求
                Response response = Response.build(socket.getOutputStream());//返回里面只是一些响应格式的说明
                // 如果 url 对应静态文件存在，就当成静态文件处理，否则当成动态文件处理
                String filename = getFilename(request.getUrl());
                File file = new File(filename);
                Controller controller = null;
                if (file.exists()) {
                    // 如果url对应的静态文件存在，就当做静态文件处理
                    controller = staticController;
                } else {
                    // /list        => ListController
                    // 否则就当成动态 controller 处理
                    controller = webApp.findController(request);//将工作留给webApp，给一个响应就得到一个controller，若配置里面不支持url，最终的controller是空
                }//得到的是一个对象，所以这里需要用到反射
                System.out.println(request.getUrl());
                System.out.println(controller);
                //404没有找到资源，输出对应的状态码及原因
                //走下面是因为动态文件没有找到，返回404回去,既不是静态文件
                if (controller == null) {
                    response.setStatus(Status.Not_Found);
                    response.println(Status.Not_Found.getReason());
                } else {
                    switch (request.getMethod()) {
                        case "GET":
                            controller.doGet(request, response);
                            break;
                        case "POST":
                            controller.doPost(request, response);
                            break;
                        default://405方法不支持
                            response.setStatus(Status.Method_Not_Allowed);
                            response.println(Status.Method_Not_Allowed.getReason());
                    }
                }
                response.flush();//将结果刷新到socket
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
                }
            });
        }
    }

    private String getFilename(String url) {
        //静态文件的最终路径的获取
        if (url.equals("/")) {//url是空
            url = "/index.html";
        }
        return HOME + File.separator + "webapp" + File.separator + url.replace("/", File.separator);
    }

    public static void main(String[] args) throws DocumentException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        new Server().run(8081);
    }
}

