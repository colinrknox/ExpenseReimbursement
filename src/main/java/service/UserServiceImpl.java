package service;

import java.util.List;

import dao.UserDaoImpl;
import model.User;

public class UserServiceImpl implements UserService {
	
	private static UserDaoImpl userDao;
	
	public UserServiceImpl() {
		userDao = new UserDaoImpl();
	}

	@Override
	public void add(User user) {
		userDao.add(user);
	}

	@Override
	public void update(User user) {
		userDao.update(user);
	}

	@Override
	public User findById(int id) {
		User user = userDao.findById(id);
		return user;
	}
	
	@Override
	public User login(String username, String password) {
		return userDao.findByUsernamePassword(username, password);
	}

	@Override
	public void delete(User user) {
		userDao.delete(user);
	}

	@Override
	public List<User> findAll() {
		List<User> users = userDao.findAll();
		return users;
	}
}
