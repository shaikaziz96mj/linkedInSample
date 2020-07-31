package com.social.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedInTokenObject {

	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("expires_in")
	private String expiryTime;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getExpiryTime() {
		return expiryTime;
	}
	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}
	
	public LinkedInTokenObject(String accessToken, String expiryTime) {
		super();
		this.accessToken = accessToken;
		this.expiryTime = expiryTime;
	}
	
	public LinkedInTokenObject() {
		super();
	}
	
	@Override
	public String toString() {
		return "LinkedInTokenObject [accessToken=" + accessToken + ", expiryTime=" + expiryTime + "]";
	}
	
}