package dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import logic.User;

public class UserDao {
	private NamedParameterJdbcTemplate template;
	public void setDataSource(DataSource dataSource) { 
		this.template = new NamedParameterJdbcTemplate(dataSource); //db와 연결
	}
	public void insert(User user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount "
				+ " (userid, username, password, birthday, phoneno, postcode, address, email )"
				+ "values (:userid,:username,:password,:birthday,:phoneno,:postcode,:address,:email )";
		template.update(sql,param);
	}
	public User selectOne(String userid) {
		String sql = "select * from useraccount where userid=:userid";
		RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
		Map<String,String> param = new HashMap<String,String>();
		param.put("userid", userid);
		return template.queryForObject(sql, param, mapper);
	}
}
