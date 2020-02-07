package logic;

import dao.UserDao;

public class ShopService {
	private UserDao userDao;
	//db와 연결된 UserDao 객체 주입
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	public void insertUser(User user) {
		userDao.insert(user);
	}
	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}
}
