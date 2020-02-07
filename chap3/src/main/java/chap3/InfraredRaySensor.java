package chap3;

public class InfraredRaySensor {
	private String name;
	private boolean objectFounded;
	public InfraredRaySensor(String name) {
		this.name = name;
	}
	public void foundObject() {
		this.objectFounded = true;
	}
	//getter, setter
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isObjectFounded() {
		return objectFounded;
	}
	public void setObjectFounded(boolean objectFounded) {
		this.objectFounded = objectFounded;
	}
	@Override
	public String toString() {
		return "InfrareRaySensor [name=" + name + ", objectFounded=" + objectFounded + "]";
	}
	
	
}
