package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart") // cart가 들어오면 바로 나를 호출해
public class CartController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("cartAdd")
	public ModelAndView add(String id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id); //선택된 상품 객체
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
		cart.push(new ItemSet(item,quantity));
		mav.addObject("message",item.getName() + ":" + quantity + "개 장바구니 추가");
		mav.addObject("cart",cart);
		return mav;		
	}
	/*
	 * index 파라미터값에 해당하는 값을 Cart의 ItemSetList 객체에서 제거
	 * message에 해당 ooo상품을 장바구니에 제거 메세지 view(cart.jsp)에 전달
	 * 1. session에서 CART 객체 조회
	 * 2. cart 객체에서 itemSetList 객체에서 index에 해당하는 값을 제거
	 *    ItemSet itemSet = cart.getItemSetList().remove(index) => 제거하고 제거된 내용을 itemSet에 전달해줌
	 * 3. message, cart를 view에 전달
	 */
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet item = null;
		try {
		item = cart.getItemSetList().remove(index);
		mav.addObject("message",item.getItem().getName() + "상품 제거");
		} catch (Exception e) {
			mav.addObject("message", "장바구니 상품이 삭제되지 않았습니다.");
		}
		mav.addObject("cart",cart);
		return mav;		
	}
	/*
	 * 장바구니 상품이 없는 경우 CartEmptyException을 발생시킴
	 * "장바구니에 상품이 없습니다." 메세지를 alert창으로 출력
	 * /item/list.shop 페이지 이동
	 */
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) throws CartEmptyException {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) {
			throw new CartEmptyException("장바구니에 상품이 없습니다.","../item/list.shop");
		}
		mav.addObject("cart",cart);
		return mav;
	}
	//주문전 확인
	//반드시 로그인이 필요.
	//장바구니에 상품이 존재해야함.
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		return "cart/checkout";
	}
	/*
	 * 주문 확정
	 * 1. 세션에서 CART, loginUser 정보 조회
	 * 2. sale, saleitem 테이블 데이터 추가
	 * 3. 장바구니에서 상품 제거
	 * 4. cart/end.jsp 페이지로 이동
	 */
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		//1, 2
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("loginUser");
		//sale : 주문 정보 내역 저장
		Sale sale = service.checkend(loginUser,cart);
		//3
		long total = cart.getTotal(); //cart 세션 지우기전에 total 값을 가져와줌
		session.removeAttribute("CART"); //장바구니 내용 제거
		mav.addObject("sale",sale);
		mav.addObject("total",total);
		return mav;
	}
}
