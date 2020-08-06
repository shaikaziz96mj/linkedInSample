package com.social.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.social.domain.HubSpotToken;
import com.social.domain.ResponseObject;

@Service
public class HubSpotService implements IHubSpotService {

	@Value("${hubspot.app-id}")
	private String hubspotAppId;

	@Value("${hubspot.client-id}")
	private String hubspotClientId;

	@Value("${hubspot.client-secret}")
	private String hubspotClientSecret;

	@Value("${hubspot.redirect-url}")
	private String hubspotRedirectUrl;

	@Value("${hubspot.scope}")
	private String hubspotScope;

	@Override
	public String authorizeUrl() {
		return "https://app.hubspot.com/oauth/authorize?" + "client_id=" +hubspotClientId+ 
				"&redirect_uri=" +hubspotRedirectUrl+ "&scope="+hubspotScope;
	}

	@Override
	public ResponseObject userAccessToken(String authCode) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://api.hubapi.com/oauth/v1/token");
			// Google uses form-encoded parameters.
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("grant_type", "authorization_code"));
			formparams.add(new BasicNameValuePair("client_id", hubspotClientId));
			formparams.add(new BasicNameValuePair("client_secret", hubspotClientSecret));
			formparams.add(new BasicNameValuePair("redirect_uri", hubspotRedirectUrl));
			formparams.add(new BasicNameValuePair("redirect_uri", hubspotRedirectUrl));
			formparams.add(new BasicNameValuePair("code", authCode));

			post.setEntity(new UrlEncodedFormEntity(formparams));

			CloseableHttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			if (status != 200) {
				return new ResponseObject(null, "failed with response code:"+response.getStatusLine().getStatusCode(),
						HttpStatus.BAD_REQUEST);
			}
			HttpEntity body = response.getEntity();
			String responseData = EntityUtils.toString(body);
			ObjectMapper mapper = new ObjectMapper();
			HubSpotToken tokenObject = mapper.readValue(responseData, HubSpotToken.class);
			return new ResponseObject(tokenObject, null, HttpStatus.OK);

		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseObject refreshUserAccessToken(String refreshToken) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://api.hubapi.com/oauth/v1/token");
			// Google uses form-encoded parameters.
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("grant_type", "refresh_token"));
			formparams.add(new BasicNameValuePair("client_id", hubspotClientId));
			formparams.add(new BasicNameValuePair("client_secret", hubspotClientSecret));
			formparams.add(new BasicNameValuePair("redirect_uri", hubspotRedirectUrl));
			formparams.add(new BasicNameValuePair("redirect_uri", hubspotRedirectUrl));
			formparams.add(new BasicNameValuePair("refresh_token", refreshToken));

			post.setEntity(new UrlEncodedFormEntity(formparams));

			CloseableHttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			if (status != 200) {
				return new ResponseObject(null, "failed with response code:"+response.getStatusLine().getStatusCode(),
						HttpStatus.BAD_REQUEST);
			}
			HttpEntity body = response.getEntity();
			String responseData = EntityUtils.toString(body);
			ObjectMapper mapper = new ObjectMapper();
			HubSpotToken tokenObject = mapper.readValue(responseData, HubSpotToken.class);
			return new ResponseObject(tokenObject, null, HttpStatus.OK);

		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseObject userAccessTokenInfo(String accessToken) {
		try {
			HttpResponse<String> response = Unirest.get("https://api.hubapi.com/oauth/v1/access-tokens/"+accessToken)
					.asString();
			String output=response.getBody();

			JSONParser parse = new JSONParser();
			JSONObject obj = (JSONObject)parse.parse(output);
			
			return new ResponseObject(obj, null, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseObject userRefreshTokenInfo(String refreshToken) {
		try {
			HttpResponse<String> response = Unirest.get("https://api.hubapi.com/oauth/v1/refresh-tokens/"+refreshToken)
					.asString();
			String output=response.getBody();

			JSONParser parse = new JSONParser();
			JSONObject obj = (JSONObject)parse.parse(output);
			
			return new ResponseObject(obj, null, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
