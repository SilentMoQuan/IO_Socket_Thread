package cn.moquan.img.server;

import cn.moquan.img.server.thread.MenuThread;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public class MainServer implements CommonServer {

    private boolean isRun = false;
    private ServerSocket mainServer;
    private Map<Integer, Socket> allClient = new HashMap<>();
    private int id = 0;

    public MainServer(ServerSocket mainServer) {
        this.mainServer = mainServer;
    }

    private void startServe() throws IOException {
        System.out.println("[SYS] 服务已开启...");
        while (isRun) {

            System.out.println("[SYS] 等待用户连接...");

            Socket accept = mainServer.accept();

            (new MenuThread(this, ++id, accept)).start();

            allClient.put(id, accept);

            System.out.println("[SYS] 第 " + id + " 位用户以连接");
        }

    }

    public void openService() throws IOException {

        if (!isRun) {
            isRun = true;
            startServe();
        }
    }

    public void closeService() {
        isRun = false;
    }

    public synchronized void closeClient(int id) throws IOException {
        System.out.println("[SYS] 第 "+id+" 位用户退出连接");
        Socket remove = allClient.remove(id);
        remove.close();
    }

}
