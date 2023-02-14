package cuculi.common;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
通用返回类，用来响应前端
熟悉泛型的使用
 */
@Data
public class R<T> implements Serializable {

    //约定编码 1为成功 0或其他数字为失败
    private int code;

    //错误信息
    private String msg;

    //数据
    private T data;

    //动态数据
    private Map map = new HashMap();

    //成功
    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.code = 1;
        r.data = object;
        return r;
    }

    //错误
    public static <T> R<T> error(String msg){
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    //动态数据填充，暂时没写
//    public R<T> add(String key, Object value) {
//        this.map.put(key, value);
//        return this;
//    }
}
