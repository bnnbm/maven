package main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Configuration //설정 java 소스. xml설정 대체 하는 소스
@ComponentScan(basePackages = {"annotation","main"})
@EnableAspectJAutoProxy //AOP 설정
public class AppCtx {
	/*
	 * @Bean //<bean id='memberService2' class="main.MemberService" /> public
	 * MemberService memberService2() { return new MemberService(); }
	 */
}
