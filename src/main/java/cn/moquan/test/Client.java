package cn.moquan.test;

import org.junit.jupiter.api.Test;

import javax.xml.transform.Source;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/12
 */
public class Client {
    private String basePath = "G:\\JavaSocket\\src\\main\\resources\\file\\out\\";

    private Scanner scanner = new Scanner(System.in);
    private int order = -1;

    @Test
    public void local() throws IOException, ClassNotFoundException {

        Socket client = new Socket(InetAddress.getLocalHost(), 9999);

        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        oos.writeObject(new UserInfo(101, "mmm"));
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        CommonMessage message = (CommonMessage) ois.readObject();
        System.out.println(message.getMessage());

        while (order != 0) {

            System.out.println("请输入指令:");
            order = scanner.nextInt();
            switch (order) {
                case 0:
                    break;
                case 1:
                    oos.writeObject(new CommonMessage(order, null, null));
                    break;
                case 2:
                    oos.writeObject(new CommonMessage(order, null, null));
                    message = (CommonMessage) ois.readObject();
                    System.out.println(message.getMessage());
                    String fileName = scanner.next();
                    oos.writeObject(new CommonMessage(order, fileName, null));
                    message = (CommonMessage) ois.readObject();
                    System.out.println(message.getMessage());
                    // 发送确认信息
                    order = 3;
                    oos.writeObject(new CommonMessage(order, fileName, null));
                    // 准备接收信息
                    BufferedInputStream is = new BufferedInputStream(client.getInputStream());
                    File file = new File(basePath + fileName);
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buff = new byte[1024 * 100];
                    int len = 0;
                    // 接收最后一次时 卡主 1.jpg

                    /*

                        服务端使用
                        FileInputStream fileInputStream = new FileInputStream(imgFile);
                        将数据从本地读取到字节数组
                        BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
                        将数据从字节数组写入到流

                        客户端
                        BufferedInputStream is = new BufferedInputStream(client.getInputStream());
                        从流中获取数据
                        File file = new File(basePath + fileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        将数据写入本地

                        客户端
                        读取字节不足后, 再次去流中读取, 但是服务器读取结束, 不会再次发送一个数据, 客户端永远无法等到数据, 卡死
                        
                     */
                    while ((len = is.read(buff)) != -1){
                        fos.write(buff, 0, len);
                    }
                    fos.flush();
                    System.out.println("下载完成..");
                    fos.close();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("指令有误, 请检查!");
            }

            if (order == 0) {
                break;
            }
            message = (CommonMessage) ois.readObject();
            System.out.println(message.getMessage());
        }

    }

}
