package cn.moquan.test;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/12
 */
@AllArgsConstructor
public class ServeThread extends Thread {

    private int id;
    private String basePath;
    private Socket socket;
    private CommonMessage message;
    private ObjectInputStream ois;
    private String menu01 =
            ">>>> 请选择指令 <<<<\n" +
                    "   1. 查看图片列表\n" +
                    "   2. 下载图片\n" +
                    "   0. 退出\n";

    @SneakyThrows
    @Override
    public void run() {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        // 发送菜单
        oos.writeObject((new CommonMessage(0, null, menu01)));


        // 接收客户端数据
        message = (CommonMessage) ois.readObject();


        while (message.getOrder() != 0) {
            // 处理指令信息
            switch (message.getOrder()) {
                case 0:
                    break;
                case 1:
                    // 将文件列表列出
                    File file = new File(basePath);
                    System.out.println(file);
                    File[] files = file.listFiles();
                    StringBuilder fileList = new StringBuilder();
                    for (int i = 0; i < files.length; i++) {
                        fileList.append("\t").append(i).append(": ").append(files[i].getName()).append("\n");
                    }
                    oos.writeObject(new CommonMessage(0, null, fileList + "\n" + menu01));
                    break;
                case 2:
                    // 获取文件信息
                    oos.writeObject(new CommonMessage(0, null, "\t请输入文件名:"));

                    // 接收客户端数据
                    message = (CommonMessage) ois.readObject();

                    File imgFile = new File(basePath +"\\"+ message.getFileName());
                    // 检查文件存在
                    if (!imgFile.exists()) {
                        // 不存在返回信息
                        oos.writeObject(new CommonMessage(0, null, "\t没有该文件请检查!\n" + menu01));

                        break;
                    }
                    // 存在通知客户端接收数据
                    oos.writeObject(new CommonMessage(1, null, "\t正在下载...\n"));

                    // 接收肯定信息 标识客户端准备好接收数据
                    message = (CommonMessage) ois.readObject();

                    // 发送
                    if (message.getOrder() == 3) {
                        // 开始发送数据 1.jpg
                        FileInputStream fileInputStream = new FileInputStream(imgFile);
                        BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
                        byte[] buff = new byte[1024 * 100];
                        int len = 0;
                        while ((len = fileInputStream.read(buff)) != -1) {
                            os.write(buff, 0, len);
                        }

                        os.flush();
                        // 关闭写流

                        fileInputStream.close();
                        System.out.println("[SYS] 发送完成 ");
                    }
                    break;
                default:
                    oos.writeObject(
                            new CommonMessage(0, null, "指令有误, 请重新选择!\n" + menu01));
            }

            if (message.getOrder() == 0) {
                break;
            }

            // 接收客户端数据
            message = (CommonMessage) ois.readObject();

        }

        // 通知主线程退出
        System.out.println("通知主线程退出!");

    }

    public ServeThread(int id, String basePath, Socket socket, ObjectInputStream ois) {
        this.id = id;
        this.basePath = basePath;
        this.socket = socket;
        this.ois = ois;
    }
}
