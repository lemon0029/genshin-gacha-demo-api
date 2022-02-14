package me.xuqu.exception;

/**
 * 项目异常定义类
 * 由于项目比较小，整个项目统一抛出此异常
 *
 * @author imtin
 * @date 2020/12/7 10:51
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = -3746813657890820606L;

    public AppException(String msg) {
        super(msg);
    }
}
