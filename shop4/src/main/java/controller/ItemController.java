package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

@Controller //@Component + Controller 기능
@RequestMapping("item") //item/xxx.shop
public class ItemController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list") //item/list.shop 요청
	public ModelAndView list() {
		//itemlist : item 테이블의 모든 레코드와 모든 컬럼 정보를 저장
		List<Item> itemList = service.getItemList();
		ModelAndView mav = new ModelAndView(); //뷰 : item/list
		mav.addObject("itemList", itemList);
		return mav; // /WEB-INF/view/item/list.jsp
	}
	@RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView("item/add");
		mav.addObject(new Item());
		return mav;
	}
	@RequestMapping("register") //@Valid : 유효성검증해, 이거 안넣으면 그냥 null값으로 들어감
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/add");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemCreate(item,request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;	
	}
	@RequestMapping("update")
	//@Valid : 유효성검증
	public ModelAndView update(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit");
		//유효성 검증했을떄 에러가 발생하면
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemUpdate(item,request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;		
	}	
	
	@GetMapping("*") //그 외 GET 방식 요청시 호출되는 메서드
	public ModelAndView itemSelect(String id) {
		Item item = service.getItem(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("item",item);
		return mav;
	}
	
	@GetMapping("delete")
	public ModelAndView itemDelete(String id) {
		ModelAndView mav = new ModelAndView();
		service.itemDelete(id);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
		
	}
}
