package me.yec.exception;

/**
 * @author imtin
 * @date 2020/12/7 10:51
 */
public class AppException extends RuntimeException {

    private static final long serialVersionUID = -3746813657890820606L;

    public AppException(String msg) {
        super(msg);
    }
}
