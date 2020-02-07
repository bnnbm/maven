package chap3;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component //객체화됨 이름은 "worker" 이름으로 컨테이너에 저장
@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)
//일회성 객체로 주입됨.
public class Worker {
	public void work (WorkUnit unit) {
		System.out.println(this + ":work :" + unit);
	}
}
