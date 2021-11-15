package cn.moquan.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.net.Socket;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/12
 */
@Data
@AllArgsConstructor
@ToString
public class ClientInfo {

    private String name;
    private Socket client;

}
