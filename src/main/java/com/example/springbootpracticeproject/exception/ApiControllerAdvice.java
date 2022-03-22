package com.example.springbootpracticeproject.exception;

import com.example.springbootpracticeproject.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
// 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler, @InitBinder 기능을 부여해주는 역할을 한다.
// 즉, 클래스를 분리해서 따로 관리할 수 있다
@RestControllerAdvice(assignableTypes = UserController.class) // 지정하지 않으면 글로벌
public class ApiControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExResolver(HttpServletRequest request,
                                         HttpServletResponse response,
                                         IllegalArgumentException e) {
        log.info("illegalExResolver Start!");
        return new ErrorResult(HttpStatus.BAD_REQUEST,
                e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalAccessException.class)
    public ErrorResult illegalAccessResolver(HttpServletRequest request,
                                         HttpServletResponse response,
                                         IllegalArgumentException e) {
        log.info("illegal Access Resolver Start!");
        return new ErrorResult(HttpStatus.UNAUTHORIZED,
                e.getMessage());
    }

    // messages.properties에서 reason과 동일하게 맵핑되는 값을 찾는다. → 있으면 출력
    // 없으면 error.bad 그 자체를 출력한다.
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "error.bad")
    public class BadRequestException extends RuntimeException{
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "error.bad")
    public class IllegalAccessException extends RuntimeException{
    }
}
