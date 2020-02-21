package com.appsdeveloperblog.app.ws.service;

import com.appsdeveloperblog.app.ws.exceptions.AuthenticationException;
import com.appsdeveloperblog.app.ws.shared.dto.UserDTO;

public interface AuthenticationService {
	
	UserDTO authenticate(String username, String password) throws AuthenticationException;

	String issueAccessToken(UserDTO userProfile) throws AuthenticationException;
	
	public void resetSecurityCredentials(String password, UserDTO userProfile);
}
