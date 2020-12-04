package me.yec.model.support;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author yec
 * @date 12/4/20 9:37 PM
 */
@Data
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public static <S> Result<S> ok(int code, String message, S data) {
        Result<S> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <S> Result<S> ok(S data) {
        return ok(HttpStatus.OK.value(), "success", data);
    }
}
