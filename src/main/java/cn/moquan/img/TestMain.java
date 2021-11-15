package cn.moquan.img;

import cn.moquan.img.server.ImgDownloadServer;
import cn.moquan.img.server.ImgUploadServer;
import cn.moquan.img.server.MainServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public class TestMain {

    @Test
    public void runMainServer() throws IOException, InterruptedException {

        ServerSocket serverSocket = new ServerSocket(9999);
        ServerSocket imgSocket = new ServerSocket(9998);
        ServerSocket imgUploadSocket = new ServerSocket(9997);

        MainServer mainServer = new MainServer(serverSocket);
        ImgDownloadServer imgDownloadServer = new ImgDownloadServer(imgSocket);
        ImgUploadServer imgUploadServer = new ImgUploadServer(imgUploadSocket);

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                mainServer.openService();
            }
        }).start();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                imgDownloadServer.openService();
            }
        }).start();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                imgUploadServer.openService();
            }
        }).start();

        int i = 0;
        while (true) {
            ++i;
            if (i % 10 == 0)
                System.out.println(i);
            Thread.sleep(1000);
        }
    }


}
