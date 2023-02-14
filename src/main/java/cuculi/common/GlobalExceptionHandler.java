package cuculi.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> mysqlExceptionHandler(SQLIntegrityConstraintViolationException ex){
        String msg = ex.getMessage();
        if (msg.contains("Duplicate entry")){
            String[] words = msg.split(" ");
            String returnMsg = words[2] + "已存在";
            return R.error(returnMsg);
        }

        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public R<String> CustomExceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }
}
