package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import logic.Board;

public interface BoardMapper {

	@Select({"<script>",
			"select count(*) from board",
			"<if test='searchtype != null'> where ${searchtype} like ${searchcontent} </if>"+
			"</script>"})
	int count(Map<String, Object> param);

	@Select({"<script>",
		" ${sql}" +
		"<if test='searchtype != null'> where ${searchtype} like ${searchcontent} </if>",
		" order by grp desc, grpstep limit #{startrow}, #{limit}",
		"</script>"
	})
	List<Board> select(Map<String, Object> param);

	@Select("select ifnull(max(num),0) from board")
	int maxnum();
	
	@Select("${sql} where num=#{num}")
	Board selectOne(Map<String,Object> param);

	@Insert("insert into board(num,name,pass,subject,content,file1,regdate,readcnt,grp,grplevel,grpstep)" + 
			" values(#{num},#{name},#{pass},#{subject},#{content},#{fileurl},now(),0,#{grp},#{grplevel},#{grpstep})")
	void insert(Board board);

	@Update("update board set readcnt=readcnt+1 where num=#{num}")
	void updatereadcntadd(Map<String, Object> param);

	@Update("update board set name=#{name}, subject=#{subject}, content=#{content}, file1=#{fileurl} where num=#{num}")
	void update(Object boar);

	@Update("update board set grpstep=grpstep+1 where grp=#{grp} and grpstep > #{grpstep}")
	void updateGrpStep(Map<String, Object> param);

	@Delete("delete from board where num=#{num}")
	void delete(Map<String, Object> param);

}
