package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Cart;
import logic.Item;
import logic.Sale;
import logic.SaleItem;
import logic.ShopService;
import logic.User;
import util.CipherUtil;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public String form(Model model) {
		model.addAttribute(new User());
		return null;
	}
	//1. 비밀번호 : 해쉬화 db에 저장
	//2. email : id의 해쉬값에서 키 결정. 암호화 db에 저장
	@PostMapping("userEntry")
	public ModelAndView userEntry(@Valid User user, BindingResult bresult) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		//useraccount 테이블에 내용 등록. login.jsp로 이동
		try {
//암호화 부분 추가
	    String passwd = CipherUtil.makehash(user.getPassword());
	    user.setPassword(passwd);
	    String userid = CipherUtil.makehash(user.getUserid());
  		String email = CipherUtil.encrypt(user.getEmail(),userid.substring(0,16));
  		user.setEmail(email);
		service.userInsert(user);
		mav.setViewName("redirect:login.shop");
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
		}
		return mav;
	}
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		try {
			User dbUser = service.getUser(user.getUserid());
			String passwd = CipherUtil.makehash(user.getPassword());
			if(!dbUser.getPassword().equals(passwd)) {
				bresult.reject("error.login.password");
				return mav;
			} else {
				//로그인할때 이메일 복호화해서 로그인정보세션에 보내기
				try {
					String userid = CipherUtil.makehash(dbUser.getUserid());
					String email = CipherUtil.decrypt(dbUser.getEmail(), userid.substring(0,16));
					dbUser.setEmail(email);
				} catch (Exception e) {
					e.printStackTrace();
				}
				session.setAttribute("loginUser", dbUser);
				mav.setViewName("redirect:main.shop");
			}
		} catch(LoginException e) {
			e.printStackTrace();
			bresult.reject("error.login.id");
		}
		return mav;
	}
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}
	@RequestMapping("main") //UserLoginAspect 클래스에 해당하는 핵심로직
	public String checkmain(HttpSession session) {
		return "user/main";
	}
	@RequestMapping("mypage")
	public ModelAndView checkmypage(String id, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id); //admin인 경우 파라미터에 해당하는 id 조회
		//사용자가 주문한 모든 주문내역 조회
		//admin인 경우 모든 주문내역 조회
		List<Sale> salelist = service.salelist(id);		
		for(Sale sa : salelist) {
			//주문 번호에 해당하는 주문 상품 내역 조회
			List<SaleItem> saleitemlist = service.saleItemList(sa.getSaleid());
			for(SaleItem si : saleitemlist) {
				//주문 상품 한개에 해당하는 Item 조회
				//db에 외래키 필요함
				Item item = service.getItem(si.getItemid());
				si.setItem(item);
			}
			//사용자 정보
			try {
				User saleuser = service.getUser(sa.getUserid());
				sa.setUser(saleuser);
			} catch(LoginException e) {
			}
			sa.setItemList(saleitemlist);
		}
		try {
			String userid = CipherUtil.makehash(user.getUserid());
			String email = CipherUtil.decrypt(user.getEmail(),userid.substring(0,16));
			user.setEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mav.addObject("user",user);
		mav.addObject("salelist",salelist);
		return mav;
	}
	@GetMapping(value= {"update","delete"})
	public ModelAndView checkview(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
	try {
		String userid = CipherUtil.makehash(user.getUserid());
		String email = CipherUtil.decrypt(user.getEmail(),userid.substring(0,16));
		user.setEmail(email);
	} catch (Exception e) {
		e.printStackTrace();
	}
		mav.addObject("user",user);
		return mav;
	}
	   @PostMapping("update")
	   public ModelAndView checkupdate(@Valid User user, BindingResult bresult, HttpSession session) throws Exception {
	      ModelAndView mav = new ModelAndView();
	      if(bresult.hasErrors()) {
	         bresult.reject("error.input.user");
	         return mav;
	      }
	      //비밀번호 검증 : 입력된 비밀번호와 db에 저장된 비밀번호를 비교
	      //일치 : update
	      //불일치 : 메세지를 유효성 출력으로 화면에 출력을 해주자
	      User loginUser = (User)session.getAttribute("loginUser");
	      String passwd = CipherUtil.makehash(user.getPassword());
	      user.setPassword(passwd);
	      if(!loginUser.getPassword().equals(user.getPassword())) {
	         bresult.reject("error.login.password");
	         return mav;
	      }
	      try {
	    	 String key = CipherUtil.makehash(user.getUserid()).substring(0,16);
	    	 user.setEmail(CipherUtil.encrypt(user.getEmail(), key));
	         service.userUpdate(user);
	         mav.setViewName("redirect:mypage.shop?id="+user.getUserid());
	         if(!loginUser.getUserid().equals("admin")){
	            session.setAttribute("loginUser", user);
	         }
	      }catch(Exception e) {
	         e.printStackTrace();
	         bresult.reject("error.user.update");
	      }     
	      return mav;
	   }
	@PostMapping("delete")
	public ModelAndView checkdelete(String userid, String password, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User)session.getAttribute("loginUser");
		String passwd = CipherUtil.makehash(password);
		if(!loginUser.getPassword().equals(passwd)) {
			throw new LoginException("회원 탈퇴시 비밀번호가 틀립니다.","delete.shop?id="+userid);
		}
		try {
			service.userDelete(userid);
			if(loginUser.getUserid().equals("admin")) {
				mav.setViewName("redirect:/admin/list.shop");
			} else {
				session.invalidate();
				mav.addObject("msg",userid+ "회원님. 탈퇴 되었습니다.");
				mav.addObject("url","login.shop");
				mav.setViewName("alert");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LoginException("회원 탈퇴시 오류가 발생했습니다. 전산부 연락 요망","delete.shop?id="+userid);
		}
		return mav;
	}
	@PostMapping(value="password",produces="text/html;charset=UTF-8")
	//produces="text/html;charset=UTF-8 => @ResponseBody가 view에 전달이면 한글인코딩 처리, html 형식으로 전달
	@ResponseBody //return 데이터 자체를 view의 내용으로 전달
	public String checkpassword(@RequestParam HashMap<String,String> param, HttpSession session) { //@RequestParma 모든 파라미터 값들을 HashMap객체 저장해서 전달
		User login = (User)session.getAttribute("loginUser");
		String hashpass = null;
		try {
			//param.get("pass") : request.getParameter("pass")
			hashpass = CipherUtil.makehash(param.get("pass"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!login.getPassword().equals(hashpass)) {
			throw new LoginException("비밀번호 오류","password.shop");
		}
		String chgpass = null;
		try {
			chgpass = CipherUtil.makehash(param.get("chgpass"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		service.userPasswordUpdate(login.getUserid(), chgpass);
		login.setPassword(chgpass);
		return "<script>alert('비밀번호가 변경 되었습니다.')\n"
				+ "self.close()\n"
		        + "</script>";
	}
}
