package com.For.JM.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA
 * Description:
 * User: For
 * Time: 16:54
 */
public class getClassLoader extends ClassLoader{
    private static final String HOME = System.getenv("JM_HOME");

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        // HOME/webapp/WEB-INF/classes/
        // 1. 根据类名称，找到 name 对应的 *.class 文件
        File classFile = getClassFile(name);
        // 2. 读取该文件的内容
        byte[] buf;
        try {
            buf = readClassBytes(classFile);//buf里面存的是文件内容
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
        return defineClass(name, buf, 0, buf.length);//将字节流数组变为字节码文件
    }

    private byte[] readClassBytes(File classFile) throws IOException {
        int len = (int)classFile.length();
        byte[] buf = new byte[len];
        InputStream is = new FileInputStream(classFile);
        is.read(buf, 0, len);
        return buf;
    }

    private File getClassFile(String name) {
        String filename = HOME + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + name + ".class";
        return new File(filename);
    }

}
