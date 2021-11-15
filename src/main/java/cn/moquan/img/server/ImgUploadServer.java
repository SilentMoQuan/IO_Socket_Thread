package cn.moquan.img.server;

import cn.moquan.img.server.thread.ImgUploadThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/15
 */
public class ImgUploadServer implements CommonServer{

    private boolean isRun = false;
    private ServerSocket imgUploadServer;
    private Map<Integer, Socket> allClient = new HashMap<>();
    private int id = 0;

    public ImgUploadServer(ServerSocket imgUpdateServer) {
        this.imgUploadServer = imgUpdateServer;
    }

    private void startServe() throws IOException {

        System.out.println("[IMG_UPLOAD] 图片上传服务开启...");

        while (isRun){

            System.out.println("[IMG_UPLOAD] 等待用户连接...");

            Socket client = imgUploadServer.accept();

            (new ImgUploadThread(this, ++id, client)).start();

            allClient.put(id, client);

            System.out.println("[IMG_UPLOAD] 第 "+ id +" 位用户已连接");

        }

    }

    @Override
    public void openService() throws IOException {

        if(!isRun){
            isRun = true;
            startServe();
        }

    }

    @Override
    public void closeService() {
        isRun = false;
    }

    public void removeClient(int id) throws IOException {
        System.out.println("[IMG_UPLOAD] 图片上传结束, 用户退出...");
        Socket remove = allClient.remove(id);
        remove.close();
    }
}
