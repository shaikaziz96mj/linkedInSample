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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.social.domain.AdAccountIds;
import com.social.domain.ResponseObject;
import com.social.domain.SnapChatTokens;

@Service
public class SnapChatService implements ISnapChatService {

	@Value("${snapchat.app-id}")
	private String snapchatAppId;
	@Value("${snapchat.app-secret}")
	private String snapchatAppSecret;
	@Value("${snapchat.redirect-url}")
	private String snapchatRedirectUrl;

	@Override
	public String getAuthorizationUrl() {
		String url="https://accounts.snapchat.com/accounts/oauth2/auth?"
				+ "client_id=" +snapchatAppId+ "&redirect_uri=" +snapchatRedirectUrl+ 
				"&response_type=code&scope=snapchat-marketing-api";
		return url;
	}

	@Override
	public ResponseObject userAccessToken(String authCode) {
		try {

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://accounts.snapchat.com/login/oauth2/access_token");
			// Google uses form-encoded parameters.
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("grant_type", "authorization_code"));
			formparams.add(new BasicNameValuePair("client_id", snapchatAppId));
			formparams.add(new BasicNameValuePair("client_secret", snapchatAppSecret));
			formparams.add(new BasicNameValuePair("code", authCode));
			formparams.add(new BasicNameValuePair("redirect_uri", snapchatRedirectUrl));
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
			SnapChatTokens tokenObject = mapper.readValue(responseData, SnapChatTokens.class);
			return new ResponseObject(tokenObject, null, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseObject userNewAccessToken(String refreshToken) {
		try {

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://accounts.snapchat.com/login/oauth2/access_token");
			// Google uses form-encoded parameters.
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("refresh_token", refreshToken));
			formparams.add(new BasicNameValuePair("client_id", snapchatAppId));
			formparams.add(new BasicNameValuePair("client_secret", snapchatAppSecret));
			formparams.add(new BasicNameValuePair("grant_type", "refresh_token"));
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
			SnapChatTokens tokenObject = mapper.readValue(responseData, SnapChatTokens.class);
			return new ResponseObject(tokenObject, null, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseObject organizationId(String accessToken) {
		try {
			HttpResponse<String> response = Unirest.get("https://adsapi.snapchat.com/v1/me/organizations")
					.header("authorization", "Bearer "+accessToken)
					.asString();
			String output=response.getBody();

			//Read JSON file
			JSONParser parse = new JSONParser();
			JSONObject obj = (JSONObject)parse.parse(output);
			System.out.println(obj);
			JSONArray organisations = (JSONArray) obj.get("organizations");
			System.out.println(organisations);
			JSONObject organisation=null;
			for(int i=0;i<organisations.size();i++)
			{
				JSONObject jsonobj_1 = (JSONObject)organisations.get(i);
				System.out.println("Elements under results array");
				organisation = (JSONObject) jsonobj_1.get("organization");
			}
			System.out.println(organisation);
			String id=organisation.get("id").toString();
			System.out.println(organisation.get("id"));

			return new ResponseObject(id, null, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseObject(null, e.getMessage(),HttpStatus.BAD_REQUEST);
			//return e.getMessage();
		}
	}
	
	@Override
	public ResponseObject userAdAccounts(String accessToken) {
		try {
			
			List<AdAccountIds> ids=new ArrayList<AdAccountIds>();
			String orgId=(String) organizationId(accessToken).getResponse();
			HttpResponse<String> response = Unirest.get("https://adsapi.snapchat.com/v1/organizations/"+orgId+"/adaccounts")
					.header("authorization", "Bearer "+accessToken)
					.asString();
			String output=response.getBody();
			
			JSONParser parse = new JSONParser();
			JSONObject obj = (JSONObject)parse.parse(output);
			System.out.println(obj);
			JSONArray adAccounts = (JSONArray) obj.get("adaccounts");
			System.out.println(adAccounts);
			JSONObject adAccount=null;
			for(int i=0;i<adAccounts.size();i++)
			{
				JSONObject jsonobj_1 = (JSONObject)adAccounts.get(i);
				adAccount= (JSONObject) jsonobj_1.get("adaccount");
				String id=adAccount.get("id").toString();
				String name=adAccount.get("name").toString();
				AdAccountIds adIds=new AdAccountIds(name, id);
				ids.add(adIds);
			}
			
			return new ResponseObject(ids, null, HttpStatus.OK);
			
		}catch(Exception e) {
			return new ResponseObject(null, e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}

}