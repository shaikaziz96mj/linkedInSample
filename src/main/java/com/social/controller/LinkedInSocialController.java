package com.social.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.social.domain.ResponseObject;
import com.social.service.IHubSpotService;
import com.social.service.ILinkedInService;
import com.social.service.ISnapChatService;

@RestController
@RequestMapping("/api/v1")
public class LinkedInSocialController {
	
	@Autowired
	private ILinkedInService linkedinService;
	
	@Autowired
	private ISnapChatService snapchatService;
	
	@Autowired
	private IHubSpotService hubspotService;

	@GetMapping
	public List<String> welcome() {
		List<String> urls=new ArrayList<String>();
		urls.add("http://localhost:8081/api/v1/generateLinkedInAuthorizeURL");
		urls.add("http://localhost:8081/api/v1/linkedInAccessToken");
		urls.add("=====================================================================");
		urls.add("http://localhost:8081/api/v1/authenticationUrl");
		urls.add("http://localhost:8081/api/v1/userAccessToken");
		urls.add("http://localhost:8081/api/v1/userNewAccessToken");
		urls.add("http://localhost:8081/api/v1/organizationId");
		urls.add("http://localhost:8081/api/v1/userAdAccounts");
		urls.add("=====================================================================");
		urls.add("http://localhost:8081/api/v1/hubspotAuthorizeUrl");
		urls.add("http://localhost:8081/api/v1/hubspotUserToken");
		urls.add("http://localhost:8081/api/v1/hubspotRefreshAccessToken");
		urls.add("http://localhost:8081/api/v1/hubspotAccessTokenInfo");
		urls.add("http://localhost:8081/api/v1/hubspotRefreshTokenInfo");
		return urls;
	}
	
	@GetMapping("/generateLinkedInAuthorizeURL")
	public String generateLinkedInAuthorizeURL() {
		return linkedinService.generateLinkedInAuthorizeURL();
	}
	
	@PostMapping("/linkedInAccessToken")
	public ResponseObject linkedInAccessToken(@RequestParam("authCode") String authCode) {
		return linkedinService.linkedInAccessToken(authCode);
	}
	
	@GetMapping("/authenticationUrl")
	public String snapchatAuthorizationUrl() {
		return snapchatService.getAuthorizationUrl();
	}
	
	@PostMapping("/userAccessToken")
	public ResponseObject snapchatAccessToken(String authCode) {
		return snapchatService.userAccessToken(authCode);
	}
	
	@PostMapping("/userNewAccessToken")
	public ResponseObject snapchatNewAccessToken(String refreshToken) {
		return snapchatService.userNewAccessToken(refreshToken);
	}
	
	@GetMapping("/organizationId")
	public ResponseObject snapchatOrganizationId(@RequestParam("token") String accessToken) {
		return snapchatService.organizationId(accessToken);
	}
	
	@GetMapping("/userAdAccounts")
	public ResponseObject userAdAccounts(@RequestParam("token") String accessToken) {
		return snapchatService.userAdAccounts(accessToken);
	}
	
	@GetMapping("/hubspotAuthorizeUrl")
	public String hubspotAuthorizeUrl(){
		return hubspotService.authorizeUrl();
	}
	
	@PostMapping("/hubspotUserToken")
	public ResponseObject hubspotUserAccessToken(@RequestParam("authCode") String authCode) {
		return hubspotService.userAccessToken(authCode);
	}
	
	@PostMapping("/hubspotRefreshAccessToken")
	public ResponseObject hubspotUserRefreshAccessToken(@RequestParam("refreshToken") String refreshToken) {
		return hubspotService.refreshUserAccessToken(refreshToken);
	}
	
	@GetMapping("/hubspotAccessTokenInfo")
	public ResponseObject hubspotAccessTokenInfo(@RequestParam("token") String accessToken) {
		return hubspotService.userAccessTokenInfo(accessToken);
	}
	
	@GetMapping("/hubspotRefreshTokenInfo")
	public ResponseObject hubspotRefreshTokenInfo(@RequestParam("token") String refreshToken) {
		return hubspotService.userRefreshTokenInfo(refreshToken);
	}
	
}