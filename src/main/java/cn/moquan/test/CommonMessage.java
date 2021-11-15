package cn.moquan.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/12
 */
@AllArgsConstructor
@ToString
@Data
public class CommonMessage implements Serializable {

    private int order;
    private String fileName;
    private String message;

}
