package logic;

import java.util.List;

import dao.ItemDao;

public class ShopService {
	private ItemDao itemDao;
	//db와 연결된 ItemDao 객체 주입
	public void setItemDao(ItemDao itemDao) {
		this.itemDao = itemDao;
	}
	public List<Item> getItemList() {
		return itemDao.list();
	}
	public Item getItemById(Integer id) {
		return itemDao.selectOne(id);
	}
}
