package cn.moquan.img.client;

import cn.moquan.img.message.CommonMessage;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

import java.util.Scanner;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public class Client {

    private Socket client;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Scanner sc = new Scanner(System.in);

    @Test
    public void start() throws IOException, ClassNotFoundException {
        // 获取连接
        client = new Socket(InetAddress.getLocalHost(), 9999);

        ois = new ObjectInputStream(client.getInputStream());
        CommonMessage msg = (CommonMessage) ois.readObject();

        if (msg.getOrder() == 0) {
            oos = new ObjectOutputStream(client.getOutputStream());
            // 向服务器返回确认信息
            oos.writeObject(new CommonMessage(0, null));

            // 进入服务
            menuService();

        }

        // 服务器信息确认失败, 退出
        client.close();
    }

    public void menuService() throws IOException, ClassNotFoundException {

        int order = 0;

        while (true) {
            // 接收服务器信息
            CommonMessage msg = (CommonMessage) ois.readObject();
            System.out.println(msg.getMessage());
            System.out.print("请输入指令:");
            // 接收用户指令
            order = sc.nextInt();
            // 处理指令信息
            switch (order) {
                case 0:
                    oos.writeObject(new CommonMessage(order, null));
                    return;
                case 1:
                    oos.writeObject(new CommonMessage(order, null));
                    break;
                case 2:
                    downloadImg(order);
                    break;
                case 3:
                    uploadImg(order);
                    break;
            }

        }


    }

    public void downloadImg(int order) throws IOException, ClassNotFoundException {
        System.out.print("请输入文件名:");
        String filName = sc.next();
        oos.writeObject(new CommonMessage(order, filName));
        // 获取菜单服务的消息
        CommonMessage msg = (CommonMessage) ois.readObject();
        System.out.println(msg.getMessage());

        if (msg.getOrder() == 0) {
            // 开启文件下载服务
            Socket socket = new Socket(InetAddress.getLocalHost(), 9998);
            OutputStream outputStream = socket.getOutputStream();
            // 发送文件信息
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new CommonMessage(0, filName));
            // 获取文件信息 获取输出流
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024 * 10];
            int len = 0;
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("G:\\JavaSocket\\src\\main\\resources\\file\\out\\download\\" + filName));
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
            bos.close();
            // 下载完成
            System.out.println("[INF] 下载完成...");

            // 通知下载完成
            oos.writeObject(new CommonMessage(1, null));
        }

        // 结束后 读服务端信息
    }


    private void uploadImg(int order) throws IOException, ClassNotFoundException {

        // 通知 菜单 进入下载
        oos.writeObject(new CommonMessage(3, ""));
        // 接收 菜单 回执
        CommonMessage msg = (CommonMessage) ois.readObject();

        if (msg.getOrder() == 0) {
            System.out.println("菜单知道进行上传服务!");
        }

        System.out.print("请输入文件地址: ");
        String filePath = sc.next();

        File file = new File(filePath);
        if (!file.exists()) {
            // 文件不存在时
            System.out.println("[INF] 文件不存在, 请检查!");
            // 通知 菜单 上传服务结束
            oos.writeObject(new CommonMessage(0, ""));
            return;
        }

        Socket upload = new Socket(InetAddress.getLocalHost(), 9997);

        OutputStream outputStream = upload.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        // 向服务器发送上传提示
        objectOutputStream.writeObject(new CommonMessage(3, null));
        // 获取服务器回执
        ObjectInputStream objectInputStream = new ObjectInputStream(upload.getInputStream());
        msg = (CommonMessage) objectInputStream.readObject();

        // 保存返回状态码
        int status = 0;

        if (msg.getOrder() == 3) {
            // 上传文件
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buff = new byte[1024 * 10];
            int len = -1;
            while ((len = bufferedInputStream.read(buff)) != -1){
                outputStream.write(buff, 0, len);
            }
            bufferedInputStream.close();
            // 关闭 输出
            upload.shutdownOutput();
        }

        // 接收服务器 服务结束 通知
        msg = (CommonMessage) objectInputStream.readObject();
        // 通知 菜单 上传服务结束
        oos.writeObject(new CommonMessage(0, ""));
    }
}
