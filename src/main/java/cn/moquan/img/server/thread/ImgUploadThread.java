package cn.moquan.img.server.thread;

import cn.moquan.img.message.CommonMessage;
import cn.moquan.img.server.ImgDownloadServer;
import cn.moquan.img.server.ImgUploadServer;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public class ImgUploadThread extends Thread{

    private ImgUploadServer imgUploadServer;
    private int id;
    private Socket client;
    private String uploadFilePath = "G:\\JavaSocket\\src\\main\\resources\\file\\upload\\";
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ImgUploadThread(ImgUploadServer imgUploadServer, int id, Socket client) {
        this.imgUploadServer = imgUploadServer;
        this.id = id;
        this.client = client;
    }

    @SneakyThrows
    @Override
    public void run() {

        // 接收上传提示
        ois = new ObjectInputStream(client.getInputStream());
        CommonMessage msg = (CommonMessage) ois.readObject();
        // 获取类输出流
        oos = new ObjectOutputStream(client.getOutputStream());

        // 确认文件上传服务
        if(msg.getOrder() == 3){
            // 告知准备接收
            oos.writeObject(new CommonMessage(3, null));
            // 接收文件
            // 获取读入流
            InputStream inputStream = client.getInputStream();
            // 获取输出流
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(uploadFilePath+ UUID.randomUUID()+".jpg")));
            // 缓存 长度
            byte [] buff = new byte[1024 * 10];
            int len = -1;
            while ((len = inputStream.read(buff)) != -1){
                bufferedOutputStream.write(buff, 0, len);
            }
            // 刷新
            bufferedOutputStream.flush();
            // 关流-输出流
            bufferedOutputStream.close();

        }else {
            // 告知服务错误
            oos.writeObject(new CommonMessage(-1, "服务错误!"));
        }
        // 告知 服务结束 退出
        oos.writeObject(new CommonMessage(0, "服务结束, 正在退出!"));

        imgUploadServer.removeClient(id);
    }
}
