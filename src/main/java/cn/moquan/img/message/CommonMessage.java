package cn.moquan.img.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonMessage implements Serializable {

    private int order;
    private String message;

}
