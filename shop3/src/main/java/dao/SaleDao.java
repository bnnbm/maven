package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Item;
import logic.Sale;

@Repository
public class SaleDao {
	private NamedParameterJdbcTemplate template;
	private RowMapper<Sale> mapper = new BeanPropertyRowMapper<Sale>(Sale.class);
	private Map<String, Object> param = new HashMap<>();
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public int getMaxSaleId() {
		String sql = "select ifnull(max(saleid),0) from sale";
		Integer max = template.queryForObject(sql, param, Integer.class); //sale 테이블에 저장된 saleid값의 최대값
		return max+1;
	}
	public void insert(Sale sale) {
		String sql = "insert into sale (saleid, userid, updatetime)"
				+ " values (:saleid, :userid, :updatetime)";
		SqlParameterSource proparam = new BeanPropertySqlParameterSource(sale);
		template.update(sql, proparam);
	}
	public List<Sale> list(String id) {
		String sql = "select * from sale where userid = :userid";
		param.clear();
		param.put("userid", id);
		return template.query(sql, param, mapper);
	}
}
