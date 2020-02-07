package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import logic.SaleItem;

public interface SaleitemMapper {

	@Insert("insert into saleitem" + 
			"(saleid, saleitemid, itemid, quantity)" + 
			" values(#{saleid}, #{saleitemid}, #{itemid}, #{quantity})")
	void insert(SaleItem si);

	@Select("select * from saleitem where saleid = #{saleid}")
	List<SaleItem> select(int saleid);
	
}
