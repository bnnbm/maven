package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.User;

public interface UserMapper {

	@Insert("insert into userbackup" + 
			"(userid, username, password, birthday, phoneno, postcode, address, email)" + 
			"values (#{userid},#{username},#{password},#{birthday},#{phoneno},#{postcode},#{address},#{email})")
	public void insert(User user);


	@Update("update userbackup set username=#{username}, phoneno=#{phoneno}, postcode=#{postcode}," + 
			"address=#{address}, email=#{email}, birthday=#{birthday} where userid=#{userid}")
	public void update(User user);

	@Delete("delete from userbackup where userid=#{userid}")
	public void delete(Map<String, Object> param);

	@Select({"<script>",
        "select * from userbackup",
        "<if test='userid != null'>where userid=#{userid}</if>",
        "<if test='sql != null'> where userid in (${sql}) </if>",
        "</script>"})
    List<User> select(Map<String,Object> param);


	@Update("update userbackup set password=#{chgpass} where userid=#{userid}")
	public void passupdate(Map<String, Object> param);
	
	
	
}
