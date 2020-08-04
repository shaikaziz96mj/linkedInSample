package com.social.domain;

public class AdAccountIds {

	private String name;
	private String accountId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public AdAccountIds(String name, String accountId) {
		super();
		this.name = name;
		this.accountId = accountId;
	}
	
	public AdAccountIds() {
		super();
	}
	
	@Override
	public String toString() {
		return "AdAccountIds [name=" + name + ", accountId=" + accountId + "]";
	}

}