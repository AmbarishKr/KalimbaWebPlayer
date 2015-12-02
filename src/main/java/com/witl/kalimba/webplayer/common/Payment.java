package com.witl.kalimba.webplayer.common;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table
public class Payment implements Serializable {

	@Id
	private String tokenid;
	
	private String emailid;
	
	private String songidstring;
	float amonut;
	public String createtokenstatus;

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public String getSongidstring() {
		return songidstring;
	}

	public void setSongidstring(String songidstring) {
		this.songidstring = songidstring;
	}

	public float getAmonut() {
		return amonut;
	}

	public void setAmonut(float amonut) {
		this.amonut = amonut;
	}

	public String getCreatetokenstatus() {
		return createtokenstatus;
	}

	public void setCreatetokenstatus(String createtokenstatus) {
		this.createtokenstatus = createtokenstatus;
	}

}
