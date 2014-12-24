package rest.account.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
public class Transaction implements Serializable{
	
	private static final long serialVersionUID = -3224430790044307427L;
	private String accountNumber;
	private String fromAccount;
	private String transactionDate;
	private double transactionAmount;
	
	public Transaction(){}

	public Transaction(String accountNumber, String fromAccount,
			String transactionDate, double transactionAmount) {
		
		this.accountNumber = accountNumber;
		this.fromAccount = fromAccount;
		this.transactionDate = transactionDate;
		this.transactionAmount = transactionAmount;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

}
