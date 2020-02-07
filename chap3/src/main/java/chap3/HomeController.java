package chap3;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component // homeController 객체 생성
public class HomeController {
	private AlarmDevice alarmDevice;
	private Viewer viewer;
	@Resource(name="camera1") //이름으로 객체를 주입
	private Camera camera1;
	@Resource(name="camera2")
	private Camera camera2;
	@Resource(name="camera3")
	private Camera camera3;
	@Resource(name="camera4")
	private Camera camera4;
	
	private List<InfraredRaySensor> sensors;
	@Autowired(required=false) //Recorder 객체가 없으면 null로 주입
	private Recorder rocorder;
	@Autowired //컨테이너에서 AlarmDevice 객체와 Viewer 객체
	public void prepare(AlarmDevice alarmDevice, Viewer viewer) {
		this.alarmDevice = alarmDevice;
		this.viewer = viewer;
	}
	@PostConstruct //객체의 생성시 모든 객체의 주입이 완료된 후. 객체생성 이후
	public void init() {
		System.out.println("init() 메서드 호출");
		viewer.add(camera1); viewer.add(camera2);
		viewer.add(camera3); viewer.add(camera4);
		viewer.draw();
	}
	@Autowired
	@Qualifier("intrusionDetection") // 별명 설정
	public void setSensors(List<InfraredRaySensor> sensors) {
		System.out.println("setSensors() 메서드 호출");
		this.sensors = sensors;
		for(InfraredRaySensor s : sensors) {
			System.out.println("센서 등록 :" + s);
		}
	}
	public void checkSensorAndAlarm() {
		for(InfraredRaySensor s : sensors) {
			if(s.isObjectFounded()) {
				alarmDevice.alarm(s.getName());
			}
		}
	}
}
/*
 * 기본 어노테이션
 * 1. 객체 생성 : @Component 
 * xml : <context:component-scan base-package="chap3" />
 * 2. 객체 주입 
 * @Autowired : 객체 선택의 기준이 클래스의 자료형임
 *              (required=false) : 객체가 없는 경우 가능
 * @Resource(이름) : 객체 중 이름에 해당하는 객체를 주입 
 * @Required : 객체 선택의 기준이 클래스의 자료형임. 반드시 객체가 필요함
 * 3. 그외
 * @PostConstruct : 객체 생성이 완료된 후에 호출되는 메서드의 위에 설정
 * @Qualifier : 객체의 이름에 별명을 설정. @Autowired와 함께 사용됨.
 * @Scope(..) : 생성된 객체의 지속가능한 영역 설정.
 *              @Component와 함께 사용됨.
 */











