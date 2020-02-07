package controller;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

public class LoginController {
	private ShopService shopService;
	private Validator validator;
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	@GetMapping //4.0 이후 버전에서 가능
	//@RequestMapping(method=RequestMethod.GET)을 바꾼거임
	public String loginForm(Model model) {
		model.addAttribute(new User());
		return "login";
	}
	@PostMapping
	public ModelAndView login(User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		validator.validate(user, bresult);
		if(bresult.hasErrors() ) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			User dbuser = shopService.getUser(user.getUserid());
			if(user.getPassword().equals(dbuser.getPassword())) {
				session.setAttribute("loginUser", dbuser);
			} else { //비밀번호가 틀린경우
				bresult.reject("error.login.password");
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
		} catch(EmptyResultDataAccessException e) {
			//db에 조회된 레코드가 없는 경우
			bresult.reject("error.login.id");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("loginSuccess");
		return mav;		
	}
}
