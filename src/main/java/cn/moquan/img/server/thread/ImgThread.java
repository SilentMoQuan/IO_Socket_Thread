package cn.moquan.img.server.thread;

import cn.moquan.img.message.CommonMessage;
import cn.moquan.img.server.ImgDownloadServer;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public class ImgThread extends Thread{

    private ImgDownloadServer imgDownloadServer;
    private int id;
    private Socket client;
    private String downloadFilePath = "G:\\JavaSocket\\src\\main\\resources\\file\\in";
    private String updateFilePath = "G:\\JavaSocket\\src\\main\\resources\\file\\out";
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ImgThread(ImgDownloadServer imgDownloadServer, int id, Socket client) {
        this.imgDownloadServer = imgDownloadServer;
        this.id = id;
        this.client = client;
    }

    @SneakyThrows
    @Override
    public void run() {
        // 获取文件位置信息
        ois = new ObjectInputStream(client.getInputStream());
        CommonMessage msg = (CommonMessage) ois.readObject();
        String sourcePath = downloadFilePath+"\\"+ msg.getMessage();
        File file = new File(sourcePath);
        // 配置读取流和输出流
        byte[] buffer = new byte[1024 * 10];
        int len = 0;
        OutputStream outputStream = client.getOutputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        // 将信息写入到网络流
        while ((len = bufferedInputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0,len);
        }
        outputStream.flush();
        client.shutdownOutput();

        imgDownloadServer.removeClient(id);
    }
}
