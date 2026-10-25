package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result<String> exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<String> exceptionHandler(DuplicateKeyException ex){
        String message=ex.getMessage();
        if(message.contains("Duplicate entry"))
        {
            // 用正则精准提取 'xxx' 里的用户名，避免被 MyBatis 多行错误消息干扰
            Matcher m = Pattern.compile("Duplicate entry '(.+?)' for key").matcher(message);
            if(m.find()){
                String username = m.group(1);
                String msg = username + MessageConstant.ALREADY_EXISIT;
                return Result.error(msg);
            }
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }


}
