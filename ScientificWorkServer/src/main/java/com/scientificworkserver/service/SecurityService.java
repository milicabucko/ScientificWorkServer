package com.scientificworkserver.service;

import com.scientificworkserver.model.User;

public interface SecurityService {
	
	User login(User user);
	
	void logout();
	
	User getLoggedUser();
	
	String getRoleOfLoggedUser();

}
