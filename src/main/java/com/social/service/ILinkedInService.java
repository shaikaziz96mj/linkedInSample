package com.social.service;

import com.social.domain.ResponseObject;

public interface ILinkedInService {

	public String generateLinkedInAuthorizeURL();

	public void generateLinkedInAccessToken(String code);
	
	public ResponseObject linkedInAccessToken(String authCode);
	
}