package chap2;

import java.util.Arrays;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationContext.xml");
		Project pro = ctx.getBean("project",Project.class);
		pro.build();
		pro = new Project();
//		pro.build();
		BuildRunner br = ctx.getBean("runner",BuildRunner.class);
		br.build(Arrays.asList("src/","srcResource/"), "/bin2");
		br = new BuildRunner();
		br.build(Arrays.asList("src/","srcResource/"), "/bin2");
		WriteImpl wi = ctx.getBean("write",WriteImpl.class);
		wi.write();
	}
}