package test.blog2.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import test.blog2.dto.ResponseDto;

//@ControllerAdvice //Exception이 발생하면 여기로 들어온다
//@RestController
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(value = Exception.class)
//    public ResponseDto<String> HandleArgumentException(Exception e) {
//        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
//    }
//
//}
