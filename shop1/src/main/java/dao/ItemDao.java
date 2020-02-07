package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource; //Connection 객체

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import logic.Item;

public class ItemDao {
	RowMapper<Item> mapper = new BeanPropertyRowMapper<Item>(Item.class);
	private NamedParameterJdbcTemplate template; //spring jdbc 프레임워크
	//Connection 객체 주입 => xml 설정
	//dataSource : DriverManagerDataSource 객체 주입
	public void setDataSource(DataSource dataSource) { 
		this.template = new NamedParameterJdbcTemplate(dataSource); //db와 연결
	}
	public List<Item> list() {
		/*
		   Item item = new Item();
		   item.setId(rs.getString("id"));
		   item.setName(rs.getString("name"));
		   하는것과 같은 역할을 mapper가 함 (조회된 정보를 mapper에 집어넣음)
		 */
		return template.query("select * from item", mapper);
	}
	public Item selectOne(Integer id) {
		Map<String,Integer> param = new HashMap<>();
		param.put("id", id);
		return template.queryForObject
				("select * from item where id=:id", param,mapper);
	}
}
