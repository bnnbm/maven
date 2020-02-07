package logic;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface ShopService {
	public List<Item> getItemList();
	public void itemCreate(Item item, HttpServletRequest request);
	public Item getItem(String id);
	public void itemUpdate(Item item, HttpServletRequest request);
	public void itemDelete(String id);
}
