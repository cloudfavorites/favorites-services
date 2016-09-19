package com.favorites.service;

import com.favorites.domain.result.ResponseData;
import com.favorites.domain.result.UserInformationResult;
import com.favorites.param.UserParam;

public interface UserService {

	public ResponseData updateUserInfo(UserParam userParam);
	
	public ResponseData getUserInformation(UserInformationResult ret,UserParam userParam);
	
}