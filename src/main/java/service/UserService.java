package service;

import java.util.List;

import model.User;

public interface UserService {
	public void add(User user);
	public void update(User user);
	public User findById(int id);
	public User login(String username, String password);
	public void delete(User user);
	public List<User> findAll();
}
