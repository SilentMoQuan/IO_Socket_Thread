package cn.moquan.img.server;

import java.io.IOException;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
public interface CommonServer {

    void openService() throws IOException;

    void closeService();

}
