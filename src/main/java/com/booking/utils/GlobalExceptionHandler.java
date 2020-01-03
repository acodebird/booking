package com.booking.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException e, Model model) {
        System.out.println("参数解析失败");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("StackTrace",e.getStackTrace());
        return "exception";
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,Model model) {
        System.out.println("不支持当前请求方法");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("StackTrace", e.getStackTrace());
        return "exception";
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public String handleHttpMediaTypeNotSupportedException(Exception e,Model model) {
        System.out.println("不支持当前媒体类型");
        model.addAttribute("message", e.getMessage());
        model.addAttribute("StackTrace", e.getStackTrace());
        return "exception";
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e,Model model) {
        System.out.println("服务运行异常：" + e.getMessage());
        model.addAttribute("message", e.getMessage());
        model.addAttribute("StackTrace",e.getStackTrace());
        return "exception";
    }
}
