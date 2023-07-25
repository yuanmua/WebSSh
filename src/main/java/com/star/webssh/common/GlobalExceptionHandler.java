package com.star.webssh.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /*异常处理*/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public  R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());

        //将重复的账号名返回前端
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg= split[2]+"已存在";
            return  R.error(msg);
        }


        return R.error("未知错误");
    }
}
