package cuculi.common;

/**
 * 工具类 用ThreadLocal来实现存储和获取用户id
 * 为什么不使用session呢?
 * 因为有些类无法访问（我也不懂,反正就是这样）
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
