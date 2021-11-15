package cn.moquan.img.server;

import cn.moquan.img.server.thread.ImgThread;

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
public class ImgDownloadServer implements CommonServer{

    private boolean isRun = false;
    private ServerSocket imgServer;
    private Map<Integer, Socket> allClient = new HashMap<>();
    private int id = 0;

    public ImgDownloadServer(ServerSocket mainServer) {
        this.imgServer = mainServer;
    }
    
    public void startServe() throws IOException {

        System.out.println("[IMG_DOWNLOAD] 图片下载服务启动...");

        while (isRun){

            Socket accept = imgServer.accept();

            (new ImgThread(this, id, accept)).start();

            allClient.put(++id, accept);

            System.out.println("[IMG_DOWNLOAD] 用户已连接...");
        }

    }

    public synchronized void removeClient(int id) throws IOException {
        System.out.println("[IMG_DOWNLOAD] 图片下载结束, 用户退出...");
        Socket remove = allClient.remove(id);
        remove.close();
    }
    
    @Override
    public void openService() throws IOException {

        if (!isRun) {
            isRun = true;
            startServe();
        }

    }

    @Override
    public void closeService() {
        isRun = false;
    }

}
