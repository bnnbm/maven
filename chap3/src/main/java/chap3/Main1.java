package chap3;

import org.springframework.context.support.GenericXmlApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext("classpath:annotation.xml");
		Executor exec = ctx.getBean("executor", Executor.class);
		exec.addUnit(new WorkUnit());
		exec.addUnit(new WorkUnit());
		
		HomeController home = ctx.getBean("homeController",HomeController.class);
		home.checkSensorAndAlarm();
		System.out.println("침입 없음 ========");
		System.out.println("========");
		//창문에 침입함
		InfraredRaySensor sensor = ctx.getBean("windowSensor", InfraredRaySensor.class);
		sensor.foundObject();
		home.checkSensorAndAlarm();
		System.out.println("========");
		sensor = new InfraredRaySensor("현관센서");
		sensor.foundObject();
		home.checkSensorAndAlarm();
	}
}
