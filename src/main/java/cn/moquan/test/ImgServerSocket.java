package cn.moquan.test;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/12
 */
public class ImgServerSocket {

    private Map<Integer, ClientInfo> clientMap = new HashMap<Integer, ClientInfo>();
    private RunTime runTime = RunTime.getRunTime();
    private boolean isRun = false;
    private ServerSocket server;
    private String basePath = "G:\\JavaSocket\\src\\main\\resources\\file\\in";

    @Test
    public void startService() throws InterruptedException, IOException, ClassNotFoundException {
        isRun = true;
        // 开启时间服务
        runTime.startService();
        // 开启主服务
        server = new ServerSocket(9999);
        System.out.println("[SYS] 服务已开启...");
        while (isRun){
            // 获取客户端连接
            Socket cline = server.accept();

            // 向用户容器中添加用户
            ObjectInputStream clineOIS = new ObjectInputStream(cline.getInputStream());
            UserInfo userInfo = (UserInfo) clineOIS.readObject();
            clientMap.put(userInfo.getId(), new ClientInfo(userInfo.getName(), cline));
            System.out.println("[SYS] 新连接用户: "+ userInfo);
            // 服务线程
            (new ServeThread(userInfo.getId(), basePath, cline, clineOIS)).start();
        }

        runTime.startService();
    }

    public void stopServer(){
        isRun = false;
    }

}
