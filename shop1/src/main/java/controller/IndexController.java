package controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

//index.shop 요청시 호출되는 클래스
public class IndexController {
	private ShopService shopService;
	//itemDao 객체를 저장하고 있는 ShopService 객체 주입
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	@RequestMapping //index.shop 요청시 호출되는 메서드
	public ModelAndView itemList() {
		//ModelAndView : model : view에 전달될 데이터 저장하는 객체
		//               view  : view 설정 객체
		//itemList : item 테이블의 모든 컬럼, 모든 레코드 정보를 Item 객체의 List객체로 저장
		List<Item> itemList = shopService.getItemList();
		ModelAndView mav = new ModelAndView("index"); //view 설정
		mav.addObject("itemList",itemList); //데이터 설정
		return mav;
	}
}
