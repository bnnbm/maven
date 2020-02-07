package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect //프로그램 중간에 끼어듬
@Order(1)
public class UserLoginAspect {
	@Around //advice : 핵심 로직에서 중간에 끼어들어가는 시점을 잡아냄(핵심로직 전, 후)
	//컨트롤러 패키지에 User로 시작하는 클래스 check로 시작하는메서드  그리고 session으로 끝나야함 ex)checkmain메서드
	("execution(* controller.User*.check*(..)) && args(..,session)") //pointcut : 핵심로직 설정
	public Object userLoginCheck (ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("로그인 후 이용하세요","login.shop");
		}
		Object ret = joinPoint.proceed();
		return ret;		
	}
	@Around 
	("execution(* controller.User*.check*(..)) && args(id,session)") //pointcut : 핵심로직 설정
	public Object userIdCheck (ProceedingJoinPoint joinPoint, String id, HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("2.로그인 후 이용하세요","login.shop");
		}
		if(!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(id)) { 
			throw new LoginException("본인 정보만 조회 가능합니다.","main.shop");
		}
		Object ret = joinPoint.proceed();
		return ret;		
	}
}
