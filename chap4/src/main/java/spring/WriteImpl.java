package spring;


public class WriteImpl {
	private ArticleDao dao;
	//dao : MariadbArticleDao 객체 저장
	public WriteImpl(ArticleDao dao) {
		this.dao = dao;
	}
	public void write() { //핵심 로직
		System.out.println("WriteImpl.write 메서드 호출");
		dao.insert();
	}
}
