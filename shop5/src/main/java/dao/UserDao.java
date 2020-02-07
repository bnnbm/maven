package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.UserMapper;
import exception.LoginException;
import logic.User;

@Repository
public class UserDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	private Map<String, Object> param = new HashMap<String,Object>();
	
	public void insert(User user) {
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount "
				+ " (userid, username, password, birthday, phoneno, postcode, address, email )"
				+ "values (:userid,:username,:password,:birthday,:phoneno,:postcode,:address,:email )";
		sqlSession.getMapper(UserMapper.class).insert(user);
	}
	public User selectOne(String userid) {
		param.clear();
		param.put("userid", userid);
		List<User> list = sqlSession.getMapper(UserMapper.class).select(param);
		if(list == null || list.isEmpty()) {
			throw new LoginException("해당 아이디가 없습니다.","");
		} else return list.get(0);
	}
	public void update(User user) {
		sqlSession.getMapper(UserMapper.class).update(user);
	}
	public void delete(String userid) {
		param.clear();
		param.put("userid", userid);
		sqlSession.getMapper(UserMapper.class).delete(param);
	}
	public List<User> list() {
		return sqlSession.getMapper(UserMapper.class).select(null);
	}
	public List<User> list(String[] idchks) {
		String ids= "";
		for(int i=0; i<idchks.length; i++) {
			ids +="'" + idchks[i] + ((i==idchks.length-1)?"'":"',");
		}
		String sql = ids;
		param.clear();
		param.put("sql",sql);
		return sqlSession.getMapper(UserMapper.class).select(param);
	}
	public void userPasswordUpadate(String userid, String chgpass) {
		param.clear();
		param.put("userid", userid);
		param.put("chgpass", chgpass);
		sqlSession.getMapper(UserMapper.class).passupdate(param);
	}
}
