package dao;

import java.util.List;
import logic.Item;

public interface ItemDao {
	public List<Item> list(); 
	public void insert(Item item); 
	public Item selectOne(String id);
	public void update(Item item);
	public void delete(String id); 
}
