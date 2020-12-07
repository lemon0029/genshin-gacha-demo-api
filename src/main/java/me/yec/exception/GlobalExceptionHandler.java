package me.yec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Spring Boot 全局异常处理
 *
 * @author imtin
 * @date 2020/12/7 10:55
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 应用全局异常处理
     *
     * @param e 应用异常（自定义）
     * @return HTTP响应体
     */
    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String appExceptionHandler(AppException e) {
        return e.getMessage();
    }

    /**
     * 请求参数丢失异常处理
     *
     * @param e 异常对象
     * @return HTTP响应体
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String missingParameterHandler(MissingServletRequestParameterException e) {
        return e.getMessage();
    }

    /**
     * 空指针异常处理（一般不会抛出这个异常...）
     *
     * @return HTTP响应体
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String nullPointerHandler() {
        return "NullPointerException";
    }
}
