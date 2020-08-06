package com.social.service;

import org.springframework.stereotype.Service;

import com.social.domain.ResponseObject;

@Service
public interface IHubSpotService {

	public String authorizeUrl();

	public ResponseObject userAccessToken(String authCode);

	public ResponseObject refreshUserAccessToken(String refreshToken);

	public ResponseObject userAccessTokenInfo(String accessToken);

	public ResponseObject userRefreshTokenInfo(String refreshToken);

}
