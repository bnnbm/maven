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

@Controller 
@RequestMapping("item")
public class ItemController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView list() {
		List<Item> itemList = service.getItemList();
		ModelAndView mav = new ModelAndView();
		mav.addObject("itemList",itemList);
		return mav;
	}
	@RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView("item/add");
		mav.addObject(new Item());
		return mav;
	}
	@RequestMapping("register")
	public ModelAndView register
	(@Valid Item item, BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/add");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemCreate(item,request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	@PostMapping("update")
	public ModelAndView update	(@Valid Item item, 
			BindingResult bresult,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/edit");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemUpdate(item,request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	
	@GetMapping("*")
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
