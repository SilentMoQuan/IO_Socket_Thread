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

@Data
@AllArgsConstructor
@ToString
public class UserInfo implements Serializable {

    private int id;
    private String name;

}
