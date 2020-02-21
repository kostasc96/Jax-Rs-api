package com.appsdeveloperblog.app.ws.service.impl;

import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.appsdeveloperblog.app.ws.exceptions.AuthenticationException;
import com.appsdeveloperblog.app.ws.io.dao.DAO;
import com.appsdeveloperblog.app.ws.io.dao.impl.MySQLDAO;
import com.appsdeveloperblog.app.ws.service.AuthenticationService;
import com.appsdeveloperblog.app.ws.service.UsersService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.utils.UserProfileUtils;

public class AuthenticationServiceImpl implements AuthenticationService{
	
	DAO database;

	public UserDTO authenticate(String userName, String password) throws AuthenticationException {
		UsersService usersService = new UsersServiceImpl();
		UserDTO storedUser = usersService.getUserByUserName(userName);
		
		if(storedUser == null){
			throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
		}
		
		String encryptedPassword = null;
		encryptedPassword = new UserProfileUtils().generateSecurePassword(password, storedUser.getSalt());
		
		boolean authenticated = false;
		if(encryptedPassword != null && encryptedPassword.equalsIgnoreCase(storedUser.getEncryptedPassword())){
			authenticated = true;
		}
		
		if(!authenticated){
			throw new AuthenticationException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
		}
		
		return storedUser;
	}

	
	public String issueAccessToken(UserDTO userProfile) throws AuthenticationException {
		String returnValue = null;
		
		String newSaltAsPostfix = userProfile.getSalt();
		String accessTokenMaterial = userProfile.getUserId() + newSaltAsPostfix;
		
		byte[] encryptedAccessToken = null;
		try{
			encryptedAccessToken = new UserProfileUtils().encrypt(userProfile.getEncryptedPassword(),accessTokenMaterial);
		} catch (InvalidKeySpecException ex) {
			Logger.getLogger(AuthenticationServiceImpl.class.getName()).log(Level.SEVERE,null,ex);
			throw new AuthenticationException("Failed to issue secure access token");
		}
		
		
		//save our byte array to string with Base64 encoder
		String encryptedAccessTokenBase64Encoded = Base64.getEncoder().encodeToString(encryptedAccessToken);
		
		//Split token into equal parts
		int tokenLength = encryptedAccessTokenBase64Encoded.length();
		
		//takes the part that starts at 0 index till the half
		String tokenToSaveToDatabase = encryptedAccessTokenBase64Encoded.substring(0, tokenLength/2);
		
		//takes the right side of the token and it will be returned to the authentication entry point
		returnValue = encryptedAccessTokenBase64Encoded.substring(tokenLength/2, tokenLength);
		
		//Save profile token
		userProfile.setToken(tokenToSaveToDatabase);
		
		//store it to database
		storeAccessToken(userProfile);
		
		return returnValue;
	}
	
	//store it to database
	private void storeAccessToken(UserDTO userProfile){
		this.database = new MySQLDAO();
		try {
			database.openConnection();
			database.updateUser(userProfile);
		} finally {
			database.closeConnection();
		}
	}
	
	private void updateUserProfile(UserDTO userProfile){
		this.database = new MySQLDAO();
		try {
			database.openConnection();
			database.updateUser(userProfile);
		} finally {
			database.closeConnection();
		}
	}
	
	public void resetSecurityCredentials(String password, UserDTO userProfile){
		// Generate a new salt
		UserProfileUtils userUtils = new UserProfileUtils();
		String salt = userUtils.getSalt(30);
		
		//Generate a new password
		String securePassword = userUtils.generateSecurePassword(password, salt);
		userProfile.setSalt(salt);
		userProfile.setEncryptedPassword(securePassword);
		
		// Update User profile
		updateUserProfile(userProfile);
	}
}
