package logic;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.BoardDao;
import dao.ItemDao;
import dao.SaleDao;
import dao.SaleItemDao;
import dao.UserDao;
import dao.AdminDao;

@Service //@Component + service 기능
public class ShopService {
	@Autowired //itemDao 객체 주입
	private ItemDao itemDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SaleDao saleDao;
	@Autowired
	private SaleItemDao saleItemDao;
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private AdminDao adminDao;
	
	public List<Item> getItemList() {
		return itemDao.list();
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		//업로드된 이미지 파일이 존재?
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			//파일 업로드 : 업로드된 파일의 내용을 파일에 저장
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.insert(item);
	}
	private void uploadFileCreate(MultipartFile picture, HttpServletRequest request, String path) {
		//picture : 업로드된 파일의 내용
		String orgFile = picture.getOriginalFilename();
		String uploadPath=request.getServletContext().getRealPath("/") + path;
		File fpath = new File(uploadPath);
		if(!fpath.exists()) fpath.mkdirs();
		try {
			//파일로 생성
			picture.transferTo(new File(uploadPath + orgFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Item getItem(String id) {
		return itemDao.selectOne(id);
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			uploadFileCreate(item.getPicture(),request,"item/img/");
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}

	public void itemDelete(String id) {
		itemDao.delete(id);
	}

	public void userInsert(User user) {
		userDao.insert(user);
	}

	public User getUser(String userid) {
		return userDao.selectOne(userid);
	}

	public Sale checkend(User loginUser, Cart cart) {
		Sale sale = new Sale();
		sale.setSaleid(saleDao.getMaxSaleId()); //saleid값 + 1
		sale.setUser(loginUser); //구매자 정보
		sale.setUserid(loginUser.getUserid()); //구매자아이디값
		sale.setUpdatetime(new Date()); //주문시간
		saleDao.insert(sale);
		//주문 상품 정보를 cart에서 조회 해서 itemList로 가져옴
		List<ItemSet> itemList = cart.getItemSetList();
		int i = 0;
		for (ItemSet is : itemList) {
			int saleItemId = ++i; //1->2
			SaleItem saleItem = new SaleItem
					//   1              1->2       ItemSet객체
					(sale.getSaleid(),saleItemId,is);
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem);
		}
		return sale;
	}

	public List<Sale> salelist(String id) {
		return saleDao.list(id); //사용자 id
	}

	public List<SaleItem> saleItemList(int saleid) {
		return saleItemDao.list(saleid); //saleid 주문번호
	}

	public void userUpdate(User user) {
		userDao.update(user);		
	}

	public void userDelete(String userid) {
		userDao.delete(userid);
	}

	public int boardcount(String searchtype, String searchcontent) {
		return boardDao.count(searchtype, searchcontent);
	}

	public List<Board> boardlist(Integer pageNum, int limit, String searchtype, String searchcontent) {
		Map<String,Object> map = new HashMap<String,Object>();
		return boardDao.list(pageNum,limit,searchtype,searchcontent);
	}

	public void boardWrite(Board board, HttpServletRequest request) {
		//첨부파일이 존재하는 경우
		if(board.getFile1() != null && !board.getFile1().isEmpty()) {
			uploadFileCreate(board.getFile1(), request, "board/file/");
			//업로드 파일의 이름 지정
			board.setFileurl(board.getFile1().getOriginalFilename());
		}
		int max = boardDao.maxnum();
		board.setNum(++max);
		board.setGrp(max);		
		boardDao.insert(board);
		
	}

	public Board getBoard(Integer num, HttpServletRequest request) {
	      if(request.getRequestURI().contains("detail.shop")) {
	         boardDao.readcntadd(num);
	      }
	      return boardDao.selectOne(num);
	   }

	public void boardReply(Board board) {
		boardDao.updateGrpStep(board);
	    int max = boardDao.maxnum();
	    board.setNum(++max);
	    board.setGrplevel(board.getGrplevel() + 1);
	    board.setGrpstep(board.getGrpstep() + 1);
	    boardDao.insert(board);
	}

	public Board getBoard(int num) {
		return boardDao.selectOne(num);
	}

	public void boardUpdate(Board board, HttpServletRequest request) {
        if(board.getFile1() != null && !board.getFile1().isEmpty()) {
           uploadFileCreate(board.getFile1(), request, "board/file/");
           //업로드 파일의 이름 지정
           board.setFileurl(board.getFile1().getOriginalFilename());
        }
        boardDao.update(board);
     }

	public void boardDelete(Board board) {
		boardDao.delete(board.getNum());
	}

	public List<User> userList() {
		return userDao.list();
	}

	public List<User> userList(String[] idchks) {
		return userDao.list(idchks);
	}

	public void userPasswordUpdate(String userid, String chgpass) {
		userDao.userPasswordUpadate(userid, chgpass);
	}
}
