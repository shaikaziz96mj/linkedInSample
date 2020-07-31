package com.social.domain;

import org.springframework.http.HttpStatus;

public class ResponseObject {
	private Object response;
	private Object object;
	private String message;
	private HttpStatus status;

	public ResponseObject(Exception e) {
		super();
		message = e.getLocalizedMessage();
		status = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public ResponseObject(Object response) {
		this.response = response;
		status = HttpStatus.OK;
	}

	public ResponseObject(Object response, String message, HttpStatus status) {
		super();
		this.response = response;
		this.message = message;
		this.status = status;
	}

	public ResponseObject(Object response, Object object, String message, HttpStatus status) {
		super();
		this.response = response;
		this.object = object;
		this.message = message;
		this.status = status;
	}

	public ResponseObject(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	public Object getResponse() {
		return response;
	}

	public String getMessage() {
		return message;
	}

	public int getStatus() {
		return ((HttpStatus) this.status).value();
	}

	public Object getJwt() {
		return object;
	}

	@Override
	public String toString() {
		return "ResponseObject [response=" + response + ", object=" + object + ", message=" + message + ", status="
				+ status + "]";
	}

}
