package com.example.springbootpracticeproject.interceptor;

import com.example.springbootpracticeproject.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //@RequestMapping: HandlerMethod
        //정적 리소스: ResourceHttpRequestHandler
        // handler 종류 확인 => HandlerMethod 타입인지 체크
        // HandlerMethod가 아니면 그대로 진행
        // 현재 프로젝트에서는 전부 헨들러메소드의 컨트롤러만있음음
       if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HttpSession session = request.getSession();
        if (session == null) {
            response.setStatus(401);
            response.getWriter().write("인증 실패");
            return false;
        }

        // 세션이 존재하면 유효한 유저인지 확인
        Optional<User> user = (Optional<User>) session.getAttribute("user");
        if (!user.isPresent()) {
            response.setStatus(401);
            response.getWriter().write("인증 실패");
            return false;
        }

        // 접근허가
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String logId = (String)request.getAttribute("user");
        log.info("RESPONSE [{}][{}]", logId, requestURI);
        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
