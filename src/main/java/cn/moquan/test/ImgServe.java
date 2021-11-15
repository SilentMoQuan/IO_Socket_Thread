package cn.moquan.test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public class ImgServe {

    private ServerSocket imgSocket;
    private boolean isRun = false;

    public void startServe() throws IOException {

        if(isRun){
            return;
        }

        isRun = true;
        imgSocket = new ServerSocket(9998);

        while (isRun){

            Socket socket = imgSocket.accept();

            //()

        }

    }

    public void stopImgServe(){
        isRun = false;
    }

}
