package cn.moquan.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/12
 */
public class TestMain {

    @Test
    public void method01() throws IOException {

        ServerSocket serverSocket = new ServerSocket(9999);
        // 获取客户端连接
        Socket socket = serverSocket.accept();

        // 获取
        InputStream inputStream = socket.getInputStream();
        byte[] buff = new byte[1024 * 10];
        int len = 0;
        while ((len = inputStream.read(buff)) != -1){
            System.out.println(new String(buff, 0, len, StandardCharsets.UTF_8));
        }
        System.out.println();
        System.out.println("数据接收完毕");
        socket.shutdownInput();

        // 发送
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("你好, 再见!".getBytes(StandardCharsets.UTF_8));
        socket.shutdownOutput();

        // 关流
        inputStream.close();
        outputStream.close();
        // 关闭客户端
        socket.close();
        // 关闭服务器
        serverSocket.close();
    }

    @Test
    public void cline() throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 9999);

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello, server!".getBytes(StandardCharsets.UTF_8));
        socket.shutdownOutput();

        byte [] buff = new byte[1024 * 5];
        int len = 0;
        InputStream inputStream = socket.getInputStream();
        while((len = inputStream.read(buff)) != -1){
            System.out.println(new String(buff, 0, len, StandardCharsets.UTF_8));
        }
        socket.shutdownInput();
        // 关流
        inputStream.close();
        outputStream.close();
        // 关闭客户端
        socket.close();
    }
}
