package cn.moquan.test;

import java.io.File;
import java.net.Socket;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public class ImgThread extends Thread{

    private Socket socket;
    private String imgPath;

    public ImgThread(Socket socket, String imgPath) {
        this.socket = socket;
        this.imgPath = imgPath;
    }

    @Override
    public void run() {

//        new File()


    }
}
