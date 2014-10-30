package edu.upc.eetac.dsa.urtasun.urtasun.api.model;

public class UrtasunError {
	private int status;
	private String message;

	public UrtasunError() {
		super();
	}

	public UrtasunError(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
