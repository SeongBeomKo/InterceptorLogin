package com.example.springbootpracticeproject.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


// ACCEPT HEADER를 먼저 확인한다.
// HEADER가 application/json이면 BODY에 응답을 준다.
// HEADER가 다른 것이면 500.html 페이지로 응답을 준다.
// setStatus를 이용해 상태 코드만 설정해준다.
// ModelAndView에는 Body값을 작성할 수 없다.
// response.getWriter().writer를 통해서 Body에 값을 적어주었다.
// 바이트 코드를 String으로 작성해줄 때는 항상 어떤 값을 인코딩 했는지 알려줘야한다.
// new ModelAndView()로 View 없이 controller로 돌아간다.
// setStatus를 통해서 현재의 응답의 상태만 설정을 해준다.
// sendError를 치거나 Exception이 서블릿 컨테이너까지 올라가지 않았기 때문에
// 발생했던 Exception은 없어진 것이나 다름이 없다.
// 따라서 서블릿 컨테이너는 Exception 처리를 위해서 어떤 것도 수행하지 않는다.

@Slf4j
//@RestControlleradvice로 묶인 @ExceptionHandler가 더 우선순위에서 동작하기때문에 실행되지 않는다.
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler, Exception ex) {

        log.info("call resolver", ex);
        String acceptHeader = request.getHeader("accept");
        try {

            Map<String, Object> resultError = new HashMap<>();
            resultError.put("ex", ex.getClass());
            resultError.put("message", ex.getMessage());
            String result = objectMapper.writeValueAsString(resultError);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(result);

            if (ex instanceof IllegalArgumentException) {
                log.info("MyHanderExceptionResolver resolved 400 bad request");

               // if(acceptHeader.equals("application/json")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                    //String requestURI = request.getRequestURI();
                    return new ModelAndView();
              //  } else {
                    //html/text
                    // html 페이지 안만듬
                   // return new ModelAndView("error/400");
             //   }
            }

            if(ex instanceof NullPointerException) {
                log.info("MyHanderExceptionResolver resolved 404 Not Found");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return new ModelAndView();
            }

        } catch (IOException e) {
            log.error("other error", e);
        }

        return null;
    }
}

// 동작 순서

//    1. Handler에서 Error가 발생한다.
//    2. Error가 Dispatcher Servlet으로 넘어온다.
//    3. Dispatcher Servlet은 등록된 HandlerExcpetionResovler를 하나씩 살펴본다.
//    4. MyHandlerExceptionResolver의 application/json Header를 보고 HTML Body에 값을 적어주었다.
//    5. 이 때, setStatus로 상태값만 설정해주었기 때문에 서블릿 컨테이너는 어떤 Exception도 발생하지 않은 것으로 이해한다
//    6. ModelAndView()로 반환해주었기 때문에 서블릿 컨테이너까지 바로 나간다.
//    7. 서블릿 컨테이너에서 Exception 체크 시 문제 없기 때문에 응답이 내려진다.
//    8. 위 과정을 통해 BasicErrorController를 거치지 않고 HandlerExceptionResolver에서 Exception을 처리해버리고,
//       HTML BODY에 데이터를 내려줄 수 있었다.
