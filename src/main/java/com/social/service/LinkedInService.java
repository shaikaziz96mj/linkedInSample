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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.domain.LinkedInTokenObject;
import com.social.domain.ResponseObject;

@Service
public class LinkedInService implements ILinkedInService {

	private String accessToken;

	@Value("${spring.social.linkedin.app-id}")
	private String linkedinAppId;
	@Value("${spring.social.linkedin.app-secret}")
	private String linkedinAppSecret;
	@Value("${spring.social.linkedin.scopes}")
	private String linkedinAppScopes;
	@Value("${spring.social.linkedin.redirect-url}")
	private String linkedinRedirectUrl;
	@Value("${spring.social.linkedin.state}")
	private String linkedinState;

	private LinkedInConnectionFactory createConnection() {
		return new LinkedInConnectionFactory(linkedinAppId, linkedinAppSecret);
	}

	/*private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}*/

	@Override
	public String generateLinkedInAuthorizeURL() {
		OAuth2Parameters params=new OAuth2Parameters();
		params.setRedirectUri(linkedinRedirectUrl);
		params.setScope(linkedinAppScopes);
		params.setState(linkedinState);
		return createConnection().getOAuthOperations().buildAuthenticateUrl(params);
	}

	@Override
	public void generateLinkedInAccessToken(String code) {
		accessToken=createConnection().getOAuthOperations().exchangeForAccess(code,linkedinRedirectUrl,null).getAccessToken();
		Long accessTokenLife=createConnection().getOAuthOperations().exchangeForAccess(code,linkedinRedirectUrl,null).getExpireTime();
		System.out.println("Access token life::"+accessTokenLife);
		System.out.println("Access Token::"+accessToken);
	}

	@Override
	public ResponseObject linkedInAccessToken(String authCode) {

		try {

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost("https://www.linkedin.com/oauth/v2/accessToken");
			// Google uses form-encoded parameters.
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("grant_type", "authorization_code"));
			formparams.add(new BasicNameValuePair("code", authCode));
			formparams.add(new BasicNameValuePair("redirect_uri", linkedinRedirectUrl));
			formparams.add(new BasicNameValuePair("client_id", linkedinAppId));
			formparams.add(new BasicNameValuePair("client_secret", linkedinAppSecret));
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
			LinkedInTokenObject tokenObject = mapper.readValue(responseData, LinkedInTokenObject.class);
			return new ResponseObject(tokenObject, null, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseObject(null, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}