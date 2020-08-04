package com.social.service;

import org.springframework.stereotype.Service;

import com.social.domain.ResponseObject;

@Service
public interface ISnapChatService {

	public String getAuthorizationUrl();

	public ResponseObject userAccessToken(String authCode);

	public ResponseObject userNewAccessToken(String refreshToken);

	public ResponseObject organizationId(String accessToken);

	public ResponseObject userAdAccounts(String accessToken);

}