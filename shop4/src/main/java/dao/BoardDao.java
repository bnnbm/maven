package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.BoardMapper;
import logic.Board;

@Repository
public class BoardDao {
	@Autowired
	private SqlSessionTemplate sqlSession;
	private Map<String, Object> param = new HashMap<>();
	private String boardcolumn = "select num, name, pass, subject,"
			+ "content, file1 fileurl, regdate, readcnt, grp,"
			+ "grplevel, grpstep from board";
	
	public int count(String searchtype, String searchcontent) { //지금 등록되어있는 건수
		param.clear();
		param.put("searchtype",searchtype);
		param.put("searchcontent", "'%" + searchcontent + "%'");
		return sqlSession.getMapper(BoardMapper.class).count(param);
	}
	public List<Board> list(Integer pageNum, int limit, String searchtype, String searchcontent) {
		String sql = boardcolumn;
		param.clear();
		param.put("sql", sql);
		param.put("searchtype",searchtype);
		param.put("searchcontent", "'%" + searchcontent + "%'");
		param.put("startrow", (pageNum - 1) * limit);
		param.put("limit", limit);
		return sqlSession.getMapper(BoardMapper.class).select(param);
	}
	public int maxnum() {
		return sqlSession.getMapper(BoardMapper.class).maxnum();
	}
	public void insert(Board board) {
		sqlSession.getMapper(BoardMapper.class).insert(board);
	}
	public void readcntadd(Integer num) {
		param.clear();
		param.put("num", num);
		sqlSession.getMapper(BoardMapper.class).updatereadcntadd(param);
	}
	public Board selectOne(Integer num) {
		String sql = boardcolumn;
		param.clear();
		param.put("sql", sql);
		param.put("num", num);
		return sqlSession.getMapper(BoardMapper.class).selectOne(param);
	}
	public void updateGrpStep(Board board) {
		param.clear();
		param.put("grp", board.getGrp());
		param.put("grpstep", board.getGrpstep());
		sqlSession.getMapper(BoardMapper.class).updateGrpStep(param);
	}
	public void update(Board board) {
		sqlSession.getMapper(BoardMapper.class).update(board);
	}
	public void delete(int num) {
		param.clear();
		param.put("num", num);
		sqlSession.getMapper(BoardMapper.class).delete(param);
	}
}
