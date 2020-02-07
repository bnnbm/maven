package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;
import util.UserValidator;

public class UserEntryController {
	private ShopService shopService;
	//userDao 객체를 저장하고 있는 ShopService 객체 주입
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	private UserValidator userValidator;
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}
	@ModelAttribute
	public User getUser() {
		User user = new User();
		user.setUsername("홍길동");
		return user;
	}
	@RequestMapping(method=RequestMethod.GET)
	public String userEntryForm() {
	//	return "userEntry"; //view만 설정
		return null;
	}
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView userEntry(User user, BindingResult bindResult) {
		//user : 파라미터값(입력된값)을 저장하고 있는 객체
		ModelAndView mav = new ModelAndView();
		userValidator.validate(user, bindResult); //validate 에서 에러 정보 가져옴
		if(bindResult.hasErrors()) {
			mav.getModel().putAll(bindResult.getModel());
			return mav;
		}
		try {
			shopService.insertUser(user);
			mav.addObject("user",user);
		} catch (DataIntegrityViolationException e) { //키가 중복된값이 들어왔을때 에러 발생(Spring jdbc에서만 가능한 예외)
			e.printStackTrace();
			bindResult.reject("error.duplicate.user"); //messages.~~ 에서 해당에러가 글로벌 에러로 나타남 (여기선 중복된 id입니다.)
			mav.getModel().putAll(bindResult.getModel());
			return mav;
		}
		mav.setViewName("userEntrySuccess"); //에러없이 등록 되면 user 객체에 실어서 userEntrySuccess 로~
		return mav;
	}
	@InitBinder //파라미터값 형 변환
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		/*
		 * Date.class : 형변환 대상이 되는 자료형
		 * format : 형식지정
		 * true/false : 비입력허용(선택입력)/비입력불허(필수입력)
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,false));
	}
}
