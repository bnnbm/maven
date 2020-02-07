package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import logic.User;

@Repository
public class AdminDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	private Map<String, String> param = new HashMap<String,String>();
	@Autowired
	public void setDataSource(DataSource dataSource) { 
		this.template = new NamedParameterJdbcTemplate(dataSource); //db와 연결
	}
	public List<User> list() {
		return template.query("select * from useraccount", mapper);
	}
	
	
}
