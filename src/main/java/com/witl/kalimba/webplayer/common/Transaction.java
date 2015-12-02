package com.witl.kalimba.webplayer.common;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table
public class Transaction {
    
    private String pnrId;
    @Id
	private String tnsId;
	
	private String ccDapproval;
	private String companyRef;
//	@Column(columnDefinition="false")
	
	@Column(name="isDownloaded")
	private String isDownloaded="NO";
	
	public String getIsDownloaded() {
		return isDownloaded;
	}
	public void setIsDownloaded(String isDownloaded) {
		this.isDownloaded = isDownloaded;
	}
	@Column
	@Type(type="date")
	private Date date;
	//private String tokenid;
	
	 @OneToOne(targetEntity = Payment.class)
	  @JoinColumn(name = "tokenid", referencedColumnName = "tokenid")	
	private Payment payment;
	
	
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getTnsId() {
		return tnsId;
	}
	public void setTnsId(String tnsId) {
		this.tnsId = tnsId;
	}
	public String getPnrId() {
		return pnrId;
	}
	public void setPnrId(String pnrId) {
		this.pnrId = pnrId;
	}
	public String getCcDapproval() {
		return ccDapproval;
	}
	public void setCcDapproval(String ccDapproval) {
		this.ccDapproval = ccDapproval;
	}
	public String getCompanyRef() {
		return companyRef;
	}
	public void setCompanyRef(String companyRef) {
		this.companyRef = companyRef;
	}
	
	
}
