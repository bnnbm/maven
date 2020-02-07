package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xml.Article;
import xml.Member;
import xml.MemberService;
import xml.ReadArticleService;
import xml.UpdateInfo;

public class Main3 {
	public static void main(String[] args) {
		String[] config = {"annotation.xml"};
		ApplicationContext ctx = new ClassPathXmlApplicationContext(config);
		ReadArticleService service = ctx.getBean("readArticleService",ReadArticleService.class);
		try {
			Article a1 = service.getArticleAndReadCnt(1);
			Article a2 = service.getArticleAndReadCnt(1);
			System.out.println("[main] a1==a2 : " + (a1==a2));
			service.getArticleAndReadCnt(0);
		} catch (Exception e) {
			System.out.println("[main] " + e.getMessage());
		}
		System.out.println("\n UpdateMemberInfoTrace Aspect 연습");
		annotation.MemberService msvc = ctx.getBean("memberService",annotation.MemberService.class);
		msvc.regist(new Member());
		msvc.update("hong",new UpdateInfo());
		msvc.delete("hong2", "test");
		
		System.out.println("\n main.MemberService 메서드 호출");
		main.MemberService msvc2 = ctx.getBean("memberService2",main.MemberService.class);
		msvc2.regist(new Member());
		msvc2.update("hong",new UpdateInfo());
		msvc2.delete("hong2", "test");
		
	}
}
