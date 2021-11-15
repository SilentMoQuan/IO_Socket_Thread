package cn.moquan.img.server.thread;

import cn.moquan.img.message.CommonMessage;
import cn.moquan.img.server.MainServer;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public class MenuThread extends Thread {

    private MainServer mainServer;
    private Socket client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private String downloadFilePath = "G:\\JavaSocket\\src\\main\\resources\\file\\in";
    private String updateFilePath = "G:\\JavaSocket\\src\\main\\resources\\file\\out";
    private int id;
    private String menu = ">>>> 请选择指令 <<<<\n" +
            "   1. 查看图片列表\n" +
            "   2. 下载图片\n" +
            "   3. 上传图片\n" +
            "   0. 退出\n";

    public MenuThread(MainServer mainServer, int id, Socket client) {
        this.mainServer = mainServer;
        this.id = id;
        this.client = client;
    }

    @SneakyThrows
    @Override
    public void run() {
        // 发送确认信息
        oos = new ObjectOutputStream(client.getOutputStream());
        oos.writeObject(new CommonMessage(0, null));

        // 获取客户端确认信息
        ois = new ObjectInputStream(client.getInputStream());
        CommonMessage msg = (CommonMessage) ois.readObject();

        // 表示收到客户端确认连接
        if (msg.getOrder() == 0) {

            // 菜单服务
            menuService();

        }

        // 删除客户端连接记录
        // 关闭客户端连接
        mainServer.closeClient(id);
    }

    private void menuService() throws IOException, ClassNotFoundException {
        int order = 0;

        oos.writeObject(new CommonMessage(0, menu));

        while (true) {
            // 获取客户端指令
            CommonMessage msg = (CommonMessage) ois.readObject();
            order = msg.getOrder();
            // 进行服务
            switch (order) {
                case 0:
                    return;
                case 1:
                    // 获取文件列表
                    getFileList();
                    break;
                case 2:
                    downloadImg(msg.getMessage());
                    break;
                case 3:
                    uploadImg();
                    break;
                case 4:
                    break;
                default:
                    ;
            }
        }
    }

    private void getFileList() throws IOException {

        File file = new File(downloadFilePath);
        File[] filesArr = file.listFiles();
        if (filesArr != null) {
            StringBuilder filesName = new StringBuilder("\n>>>>> 图片列表 <<<<<\n");
            for (int i = 0; i < filesArr.length; i++) {
                filesName.append(i).append(". ").append(filesArr[i].getName()).append("\n");
            }
            // 发送数据, 顺便发送菜单
            oos.writeObject(new CommonMessage(0, filesName.toString() + "\n" + menu));
        } else {
            System.out.println("[SYS] 文件列表显示异常");
        }
    }

    private void downloadImg(String fileName) throws IOException, ClassNotFoundException {

        String sourcePath = downloadFilePath + "\\" + fileName;

        File imgFile = new File(sourcePath);

        if (!imgFile.exists()) {
            oos.writeObject(new CommonMessage(-1, "文件不存在, 请检查!"));
            oos.writeObject(new CommonMessage(0, menu));
            return;
        }

        // 获取文件下载服务
        // 开始下载
        oos.writeObject(new CommonMessage(0, "正在下载..."));
        // 接收下载结束通知
        CommonMessage msg = (CommonMessage) ois.readObject();
        // 发送菜单
        oos.writeObject(new CommonMessage(0, menu));
    }

    // 图片下载服务
    private void uploadImg() throws IOException, ClassNotFoundException {

        // 通知进入上传服务
        oos.writeObject(new CommonMessage(0, null));

        // 接收下载结束通知
        CommonMessage msg = (CommonMessage) ois.readObject();
        // 发送菜单
        oos.writeObject(new CommonMessage(0, menu));
    }
}
