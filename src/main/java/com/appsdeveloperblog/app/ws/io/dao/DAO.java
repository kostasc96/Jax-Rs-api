package com.appsdeveloperblog.app.ws.io.dao;

import java.util.List;

import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;

//data access object

public interface DAO {
	public void openConnection();
	
	UserDTO getUserByUserName(String userName);
	
	UserDTO saveUser(UserDTO user);
	
	UserDTO getUser(String id);
	
	List<UserDTO> getUsers(int start, int limit);
	
	void updateUser(UserDTO userProfile);
	
	void deleteUser(UserDTO userProfile);
	
	public void closeConnection();
}
