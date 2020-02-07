package util;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import logic.User;

public class UserValidator implements Validator {
	@Override
	public boolean supports(Class<?> cls) {
		//유효성 검증 대상이 되는 객체 여부 확인
		return User.class.isAssignableFrom(cls);
	}
	@Override
	public void validate(Object obj, Errors errors) {		
		User user = (User)obj;
		//입력이 안되있으면 에러메세지 호출하는 역할
		String group = "error.required";
		if(user.getUserid() == null || user.getUserid().length()==0) {
			errors.rejectValue("userid", group);
		}
		if(user.getPassword() == null || user.getPassword().length()==0) {
			errors.rejectValue("password", group);
		}
		if(user.getUsername() == null || user.getUsername().length()==0) {
			errors.rejectValue("username", group);
		}
		if(user.getPhoneno() == null || user.getPhoneno().length()==0) {
			errors.rejectValue("phoneno", group);
		}
		if(user.getAddress() == null || user.getAddress().length()==0) {
			errors.rejectValue("address", group);
		}
		if(user.getEmail() == null || user.getEmail().length()==0) {
			errors.rejectValue("email", group);
		}
		//한개라도 오류가 발생하면 글로벌에러 발생
		if(errors.hasErrors()) {
			errors.reject("error.input.user"); //글로벌오류
		}
	}	
}
