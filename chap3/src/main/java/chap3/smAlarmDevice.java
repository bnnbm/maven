package chap3;

import org.springframework.stereotype.Component;

@Component
public class smAlarmDevice implements AlarmDevice {
	public void alarm(String name) {
		System.out.println(name + "에서 침입이 탐지됨. 신고 요망");
	}	
}
