package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.SaleMapper;
import logic.Sale;

@Repository
public class SaleDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	private Map<String, Object> param = new HashMap<>();
	
	public int getMaxSaleId() {
		Integer max = sqlSession.getMapper(SaleMapper.class).maxsalid(); //sale 테이블에 저장된 saleid값의 최대값
		return max + 1 ;
	}
	public void insert(Sale sale) {
		sqlSession.getMapper(SaleMapper.class).insert(sale);
	}
	public List<Sale> list(String id) {
		param.clear();
		if(!id.equals("admin"))
		param.put("userid", id);
		return sqlSession.getMapper(SaleMapper.class).select(param);
	}
}
