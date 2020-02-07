package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;

@Component
@Aspect
@Order(2)
public class CartAspect {
	@Around("execution(* controller.Cart*.check*(..))")
	public Object cartChect(ProceedingJoinPoint joinPoint) throws Throwable {
		//핵심로직에서 session 객체를 첫번째 매개변수로 설정해야 함
		HttpSession session = (HttpSession)joinPoint.getArgs()[0];
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) {
			throw new CartEmptyException("장바구니에 주문 상품이 없습니다.","../item/list.shop");
		}
		if(session.getAttribute("loginUser") == null) {
			throw new LoginException("로그인 한 고객만 상품 주문이 가능합니다.","../user/login.shop");
		}
		Object ret = joinPoint.proceed(); //핵심로직으로 감(CartController)
		return ret;
	}
}
