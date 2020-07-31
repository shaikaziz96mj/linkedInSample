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
import com.social.service.ILinkedInService;

@RestController
@RequestMapping("/api/v1")
public class LinkedInSocialController {
	
	@Autowired
	private ILinkedInService linkedinService;

	@GetMapping
	public List<String> welcome() {
		List<String> urls=new ArrayList<String>();
		urls.add("http://localhost:8081/api/v1/generateLinkedInAuthorizeURL");
		urls.add("http://localhost:8081/api/v1/linkedInAccessToken");
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
	
}