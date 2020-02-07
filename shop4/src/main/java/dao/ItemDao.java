package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import dao.mapper.ItemMapper;
import logic.Item;
import logic.User;

@Repository //@Component + dao 기능
public class ItemDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	private Map<String, Object> param = new HashMap<>();
	
	public List<Item> list() {
		return sqlSession.getMapper(ItemMapper.class).select(null);
	}
	public void insert(Item item) {
		param.clear();
		//id : 등록된 id의 최대값
		int id = sqlSession.getMapper(ItemMapper.class).maxid();
		item.setId(++id+"");
		String sql = "insert into item (id, name, price, description, pictureUrl) "
				+ " values (:id, :name, :price, :description, :pictureUrl)";
		sqlSession.getMapper(ItemMapper.class).insert(item);
	}
	
	public Item selectOne(String id) {
		param.clear();
		param.put("id", id);
		return sqlSession.getMapper(ItemMapper.class).select(param).get(0);
	}
	public void update(Item item) {
		sqlSession.getMapper(ItemMapper.class).update(item);
	}
	public void delete(String id) {
		param.clear();
		param.put("id",id);
		sqlSession.getMapper(ItemMapper.class).delete(param);
	}
	
}
